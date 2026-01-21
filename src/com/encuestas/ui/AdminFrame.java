package com.encuestas.ui;

import com.encuestas.dao.*;
import com.encuestas.dominio.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdminFrame extends JFrame {
    private Usuario usuario;
    private EncuestaDAO encuestaDAO;
    private PreguntaDAO preguntaDAO;
    private OpcionRespuestaDAO opcionDAO;
    private RespuestaDAO respuestaDAO;

    private JTable tableEncuestas;
    private DefaultTableModel modeloTabla;
    private JButton btnNueva, btnEditar, btnEliminar, btnPreguntas, btnResumen, btnDashboard, btnCerrarSesion;

    public AdminFrame(Usuario usuario) {
        this.usuario = usuario;
        this.encuestaDAO = new EncuestaDAO();
        this.preguntaDAO = new PreguntaDAO();
        this.opcionDAO = new OpcionRespuestaDAO();
        this.respuestaDAO = new RespuestaDAO();
        initComponents();
        cargarEncuestas();
    }

    private void initComponents() {
        setTitle("Panel de Administrador - " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuario.getNombre());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblBienvenida);

        btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelSuperior.add(btnCerrarSesion);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"ID", "Titulo", "Fecha Creacion", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableEncuestas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tableEncuestas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnNueva = new JButton("Nueva Encuesta");
        btnNueva.addActionListener(e -> nuevaEncuesta());
        panelBotones.add(btnNueva);

        btnEditar = new JButton("Editar Encuesta");
        btnEditar.addActionListener(e -> editarEncuesta());
        panelBotones.add(btnEditar);

        btnPreguntas = new JButton("Gestionar Preguntas");
        btnPreguntas.addActionListener(e -> gestionarPreguntas());
        panelBotones.add(btnPreguntas);

        btnResumen = new JButton("Ver Resumen");
        btnResumen.addActionListener(e -> verResumen());
        panelBotones.add(btnResumen);

        btnDashboard = new JButton("Dashboard");
        btnDashboard.addActionListener(e -> abrirDashboard());
        panelBotones.add(btnDashboard);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarEncuesta());
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarEncuestas() {
        try {
            modeloTabla.setRowCount(0);
            List<Encuesta> encuestas = encuestaDAO.listarTodas();
            for (Encuesta e : encuestas) {
                modeloTabla.addRow(new Object[]{
                    e.getId(),
                    e.getTitulo(),
                    e.getFechaCreacion(),
                    e.isActiva() ? "Activa" : "Inactiva"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar encuestas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevaEncuesta() {
        JTextField txtTitulo = new JTextField(20);
        JCheckBox chkActiva = new JCheckBox("Activa", true);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Titulo:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Estado:"));
        panel.add(chkActiva);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nueva Encuesta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String titulo = txtTitulo.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El titulo no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Encuesta encuesta = new Encuesta();
                encuesta.setTitulo(titulo);
                encuesta.setFechaCreacion(LocalDate.now());
                encuesta.setActiva(chkActiva.isSelected());
                encuestaDAO.crear(encuesta);
                cargarEncuestas();
                JOptionPane.showMessageDialog(this, "Encuesta creada exitosamente");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al crear encuesta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarEncuesta() {
        int fila = tableEncuestas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una encuesta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            Encuesta encuesta = encuestaDAO.buscarPorId(id);
            if (encuesta == null) return;

            JTextField txtTitulo = new JTextField(encuesta.getTitulo(), 20);
            JCheckBox chkActiva = new JCheckBox("Activa", encuesta.isActiva());

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            panel.add(new JLabel("Titulo:"));
            panel.add(txtTitulo);
            panel.add(new JLabel("Estado:"));
            panel.add(chkActiva);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Encuesta", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String titulo = txtTitulo.getText().trim();
                if (titulo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El titulo no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                encuesta.setTitulo(titulo);
                encuesta.setActiva(chkActiva.isSelected());
                encuestaDAO.actualizar(encuesta);
                cargarEncuestas();
                JOptionPane.showMessageDialog(this, "Encuesta actualizada exitosamente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar encuesta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarPreguntas() {
        int fila = tableEncuestas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una encuesta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int encuestaId = (int) modeloTabla.getValueAt(fila, 0);
        String titulo = (String) modeloTabla.getValueAt(fila, 1);
        new PreguntasDialog(this, encuestaId, titulo).setVisible(true);
    }

    private void verResumen() {
        int fila = tableEncuestas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una encuesta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int encuestaId = (int) modeloTabla.getValueAt(fila, 0);
        String titulo = (String) modeloTabla.getValueAt(fila, 1);
        new ResumenDialog(this, encuestaId, titulo).setVisible(true);
    }

    private void eliminarEncuesta() {
        int fila = tableEncuestas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una encuesta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
            "¿Esta seguro de eliminar esta encuesta? Se eliminaran tambien todas sus preguntas y respuestas.",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            try {
                encuestaDAO.eliminar(id);
                cargarEncuestas();
                JOptionPane.showMessageDialog(this, "Encuesta eliminada exitosamente");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar encuesta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirDashboard() {
        new DashboardFrame(this).setVisible(true);
    }

    private void cerrarSesion() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
