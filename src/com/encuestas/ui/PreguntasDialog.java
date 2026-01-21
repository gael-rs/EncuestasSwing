package com.encuestas.ui;

import com.encuestas.dao.OpcionRespuestaDAO;
import com.encuestas.dao.PreguntaDAO;
import com.encuestas.dominio.OpcionRespuesta;
import com.encuestas.dominio.Pregunta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PreguntasDialog extends JDialog {
    private int encuestaId;
    private PreguntaDAO preguntaDAO;
    private OpcionRespuestaDAO opcionDAO;

    private JTable tablePreguntas;
    private DefaultTableModel modeloTabla;
    private JButton btnNueva, btnEditar, btnEliminar, btnOpciones, btnCerrar;

    public PreguntasDialog(Frame parent, int encuestaId, String tituloEncuesta) {
        super(parent, "Preguntas - " + tituloEncuesta, true);
        this.encuestaId = encuestaId;
        this.preguntaDAO = new PreguntaDAO();
        this.opcionDAO = new OpcionRespuestaDAO();
        initComponents();
        cargarPreguntas();
    }

    private void initComponents() {
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"ID", "Texto", "Tipo", "Valor Min", "Valor Max"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePreguntas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablePreguntas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnNueva = new JButton("Nueva Pregunta");
        btnNueva.addActionListener(e -> nuevaPregunta());
        panelBotones.add(btnNueva);

        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarPregunta());
        panelBotones.add(btnEditar);

        btnOpciones = new JButton("Gestionar Opciones");
        btnOpciones.addActionListener(e -> gestionarOpciones());
        panelBotones.add(btnOpciones);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarPregunta());
        panelBotones.add(btnEliminar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarPreguntas() {
        try {
            modeloTabla.setRowCount(0);
            List<Pregunta> preguntas = preguntaDAO.listarPorEncuesta(encuestaId);
            for (Pregunta p : preguntas) {
                modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getTexto(),
                    p.isEsTextoLibre() ? "Texto Libre" : "Opciones",
                    p.isEsTextoLibre() ? "-" : p.getValorMin(),
                    p.isEsTextoLibre() ? "-" : p.getValorMax()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar preguntas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevaPregunta() {
        JTextField txtTexto = new JTextField(30);
        JCheckBox chkTextoLibre = new JCheckBox("Es respuesta de texto libre");
        JSpinner spinMin = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JSpinner spinMax = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Texto:"));
        panel.add(txtTexto);
        panel.add(new JLabel("Tipo:"));
        panel.add(chkTextoLibre);
        panel.add(new JLabel("Valor Minimo:"));
        panel.add(spinMin);
        panel.add(new JLabel("Valor Maximo:"));
        panel.add(spinMax);

        chkTextoLibre.addActionListener(e -> {
            boolean esTextoLibre = chkTextoLibre.isSelected();
            spinMin.setEnabled(!esTextoLibre);
            spinMax.setEnabled(!esTextoLibre);
        });

        int result = JOptionPane.showConfirmDialog(this, panel, "Nueva Pregunta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String texto = txtTexto.getText().trim();
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El texto no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean esTextoLibre = chkTextoLibre.isSelected();
            int min = esTextoLibre ? 0 : (int) spinMin.getValue();
            int max = esTextoLibre ? 0 : (int) spinMax.getValue();

            if (!esTextoLibre && min >= max) {
                JOptionPane.showMessageDialog(this, "El valor minimo debe ser menor que el maximo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Pregunta pregunta = new Pregunta();
                pregunta.setTexto(texto);
                pregunta.setValorMin(min);
                pregunta.setValorMax(max);
                pregunta.setEncuestaId(encuestaId);
                pregunta.setEsTextoLibre(esTextoLibre);
                preguntaDAO.crear(pregunta);

                if (!esTextoLibre) {
                    for (int i = min; i <= max; i++) {
                        OpcionRespuesta opcion = new OpcionRespuesta();
                        opcion.setPreguntaId(pregunta.getId());
                        opcion.setValor(i);
                        opcion.setLabel("Opcion " + i);
                        opcionDAO.crear(opcion);
                    }
                }

                cargarPreguntas();
                JOptionPane.showMessageDialog(this, "Pregunta creada exitosamente");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al crear pregunta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarPregunta() {
        int fila = tablePreguntas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            Pregunta pregunta = preguntaDAO.buscarPorId(id);
            if (pregunta == null) return;

            JTextField txtTexto = new JTextField(pregunta.getTexto(), 30);
            JCheckBox chkTextoLibre = new JCheckBox("Es respuesta de texto libre", pregunta.isEsTextoLibre());

            int minInicial = pregunta.isEsTextoLibre() ? 1 : pregunta.getValorMin();
            int maxInicial = pregunta.isEsTextoLibre() ? 5 : pregunta.getValorMax();

            JSpinner spinMin = new JSpinner(new SpinnerNumberModel(minInicial, 1, 10, 1));
            JSpinner spinMax = new JSpinner(new SpinnerNumberModel(maxInicial, 1, 10, 1));

            spinMin.setEnabled(!pregunta.isEsTextoLibre());
            spinMax.setEnabled(!pregunta.isEsTextoLibre());
            chkTextoLibre.setEnabled(false);

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Texto:"));
            panel.add(txtTexto);
            panel.add(new JLabel("Tipo:"));
            panel.add(chkTextoLibre);
            panel.add(new JLabel("Valor Minimo:"));
            panel.add(spinMin);
            panel.add(new JLabel("Valor Maximo:"));
            panel.add(spinMax);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Pregunta", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String texto = txtTexto.getText().trim();
                if (texto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El texto no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pregunta.setTexto(texto);
                if (!pregunta.isEsTextoLibre()) {
                    pregunta.setValorMin((int) spinMin.getValue());
                    pregunta.setValorMax((int) spinMax.getValue());
                }
                preguntaDAO.actualizar(pregunta);
                cargarPreguntas();
                JOptionPane.showMessageDialog(this, "Pregunta actualizada exitosamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar pregunta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarOpciones() {
        int fila = tablePreguntas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int preguntaId = (int) modeloTabla.getValueAt(fila, 0);
        String texto = (String) modeloTabla.getValueAt(fila, 1);
        new OpcionesDialog(this, preguntaId, texto).setVisible(true);
    }

    private void eliminarPregunta() {
        int fila = tablePreguntas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una pregunta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
            "¿Esta seguro de eliminar esta pregunta?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            try {
                preguntaDAO.eliminar(id);
                cargarPreguntas();
                JOptionPane.showMessageDialog(this, "Pregunta eliminada exitosamente");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar pregunta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
