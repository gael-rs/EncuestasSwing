package com.encuestas.ui;

import com.encuestas.dao.OpcionRespuestaDAO;
import com.encuestas.dominio.OpcionRespuesta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OpcionesDialog extends JDialog {
    private int preguntaId;
    private OpcionRespuestaDAO opcionDAO;

    private JTable tableOpciones;
    private DefaultTableModel modeloTabla;
    private JButton btnEditar, btnCerrar;

    public OpcionesDialog(Dialog parent, int preguntaId, String textoPregunta) {
        super(parent, "Opciones - " + textoPregunta, true);
        this.preguntaId = preguntaId;
        this.opcionDAO = new OpcionRespuestaDAO();
        initComponents();
        cargarOpciones();
    }

    private void initComponents() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        String[] columnas = {"ID", "Valor", "Label"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableOpciones = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tableOpciones);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEditar = new JButton("Editar Label");
        btnEditar.addActionListener(e -> editarOpcion());
        panelBotones.add(btnEditar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarOpciones() {
        try {
            modeloTabla.setRowCount(0);
            List<OpcionRespuesta> opciones = opcionDAO.listarPorPregunta(preguntaId);
            for (OpcionRespuesta o : opciones) {
                modeloTabla.addRow(new Object[]{
                    o.getId(),
                    o.getValor(),
                    o.getLabel()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar opciones: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarOpcion() {
        int fila = tableOpciones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una opcion", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            OpcionRespuesta opcion = opcionDAO.buscarPorId(id);
            if (opcion == null) return;

            String nuevoLabel = JOptionPane.showInputDialog(this, "Nuevo label:", opcion.getLabel());
            if (nuevoLabel != null && !nuevoLabel.trim().isEmpty()) {
                opcion.setLabel(nuevoLabel.trim());
                opcionDAO.actualizar(opcion);
                cargarOpciones();
                JOptionPane.showMessageDialog(this, "Label actualizado exitosamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar opcion: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
