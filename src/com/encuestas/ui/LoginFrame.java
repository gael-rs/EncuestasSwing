package com.encuestas.ui;

import com.encuestas.dao.UsuarioDAO;
import com.encuestas.dominio.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LoginFrame extends JFrame {
    private JComboBox<Usuario> comboUsuarios;
    private JButton btnIngresar;
    private UsuarioDAO usuarioDAO;

    public LoginFrame() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        setTitle("Sistema de Encuestas - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(new JLabel("Seleccione Usuario:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        comboUsuarios = new JComboBox<>();
        panelCentral.add(comboUsuarios, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> ingresar());
        panelCentral.add(btnIngresar, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            comboUsuarios.removeAllItems();
            for (Usuario usuario : usuarios) {
                comboUsuarios.addItem(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar usuarios: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ingresar() {
        Usuario usuarioSeleccionado = (Usuario) comboUsuarios.getSelectedItem();
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un usuario",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        dispose();

        if ("ADMIN".equals(usuarioSeleccionado.getRol())) {
            new AdminFrame(usuarioSeleccionado).setVisible(true);
        } else if ("PROMOTOR".equals(usuarioSeleccionado.getRol())) {
            new PromotorFrame(usuarioSeleccionado).setVisible(true);
        }
    }
}
