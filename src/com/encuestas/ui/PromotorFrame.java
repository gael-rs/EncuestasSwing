package com.encuestas.ui;

import com.encuestas.dao.EncuestaDAO;
import com.encuestas.dominio.Encuesta;
import com.encuestas.dominio.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PromotorFrame extends JFrame {
    private Usuario usuario;
    private EncuestaDAO encuestaDAO;

    private JList<Encuesta> listaEncuestas;
    private DefaultListModel<Encuesta> modeloLista;
    private JButton btnIniciarEncuesta, btnCerrarSesion;

    public PromotorFrame(Usuario usuario) {
        this.usuario = usuario;
        this.encuestaDAO = new EncuestaDAO();
        initComponents();
        cargarEncuestas();
    }

    private void initComponents() {
        setTitle("Panel de Promotor - " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
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

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        JLabel lblInstrucciones = new JLabel("Seleccione una encuesta activa para iniciar:");
        panelCentral.add(lblInstrucciones, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaEncuestas = new JList<>(modeloLista);
        listaEncuestas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaEncuestas);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnIniciarEncuesta = new JButton("Iniciar Encuesta");
        btnIniciarEncuesta.addActionListener(e -> iniciarEncuesta());
        panelBotones.add(btnIniciarEncuesta);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarEncuestas() {
        try {
            modeloLista.clear();
            List<Encuesta> encuestas = encuestaDAO.listarActivas();
            for (Encuesta e : encuestas) {
                modeloLista.addElement(e);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar encuestas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarEncuesta() {
        Encuesta encuestaSeleccionada = listaEncuestas.getSelectedValue();
        if (encuestaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una encuesta",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        new ResponderEncuestaFrame(encuestaSeleccionada).setVisible(true);
    }

    private void cerrarSesion() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
