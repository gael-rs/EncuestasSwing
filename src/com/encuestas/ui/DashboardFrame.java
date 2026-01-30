package com.encuestas.ui;

import com.encuestas.dao.EncuestaDAO;
import com.encuestas.dao.OpcionRespuestaDAO;
import com.encuestas.dao.PreguntaDAO;
import com.encuestas.dao.RespuestaDAO;
import com.encuestas.dominio.Encuesta;
import com.encuestas.dominio.OpcionRespuesta;
import com.encuestas.dominio.Pregunta;
import com.encuestas.dominio.Respuesta;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {
    private EncuestaDAO encuestaDAO;
    private PreguntaDAO preguntaDAO;
    private OpcionRespuestaDAO opcionDAO;
    private RespuestaDAO respuestaDAO;

    private JPanel panelDashboard;
    private JButton btnCerrar;

    public DashboardFrame(Frame parent) {
        this.encuestaDAO = new EncuestaDAO();
        this.preguntaDAO = new PreguntaDAO();
        this.opcionDAO = new OpcionRespuestaDAO();
        this.respuestaDAO = new RespuestaDAO();
        initComponents();
        cargarDashboard();
    }

    private void initComponents() {
        setTitle("Dashboard - Ultimas 4 Encuestas");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Resultados de las Ultimas 4 Encuestas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        panelDashboard = new JPanel();
        panelDashboard.setLayout(new BoxLayout(panelDashboard, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelDashboard);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDashboard() {
        try {
            List<Encuesta> encuestas = encuestaDAO.listarTodas();
            int limite = Math.min(4, encuestas.size());

            for (int i = 0; i < limite; i++) {
                Encuesta encuesta = encuestas.get(i);
                JPanel panelEncuesta = crearPanelEncuesta(encuesta);
                panelDashboard.add(panelEncuesta);
                panelDashboard.add(Box.createRigidArea(new Dimension(0, 10)));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar dashboard: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelEncuesta(Encuesta encuesta) throws SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder(encuesta.getTitulo() + " - " + encuesta.getFechaCreacion())
        ));

        List<Pregunta> preguntas = preguntaDAO.listarPorEncuesta(encuesta.getId());
        int totalRespuestas = respuestaDAO.listarPorEncuesta(encuesta.getId()).size();

        JLabel lblTotal = new JLabel("Total de respuestas: " + totalRespuestas);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblTotal);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (Pregunta pregunta : preguntas) {
            if (pregunta.isEsTextoLibre()) {
                JLabel lblPregunta = new JLabel(pregunta.getTexto() + " (Respuesta de texto libre)");
                lblPregunta.setFont(new Font("Arial", Font.BOLD, 11));
                panel.add(lblPregunta);

                List<Respuesta> respuestasTexto = respuestaDAO.listarPorEncuesta(encuesta.getId());
                for (Respuesta r : respuestasTexto) {
                    if (r.getPreguntaId() == pregunta.getId() && r.getRespuestaTexto() != null) {
                        JLabel lblRespuesta = new JLabel("  - " + r.getRespuestaTexto());
                        lblRespuesta.setFont(new Font("Arial", Font.PLAIN, 10));
                        panel.add(lblRespuesta);
                    }
                }
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            } else {
                Map<Integer, Integer> conteo = respuestaDAO.contarRespuestasPorValor(pregunta.getId());
                int totalPregunta = conteo.values().stream().mapToInt(Integer::intValue).sum();

                JLabel lblPregunta = new JLabel(pregunta.getTexto());
                lblPregunta.setFont(new Font("Arial", Font.BOLD, 11));
                panel.add(lblPregunta);

                if (totalPregunta > 0) {
                    List<OpcionRespuesta> opciones = opcionDAO.listarPorPregunta(pregunta.getId());
                    for (OpcionRespuesta opcion : opciones) {
                        int cantidad = conteo.getOrDefault(opcion.getValor(), 0);
                        double porcentaje = (cantidad * 100.0) / totalPregunta;

                        JPanel panelBarra = new JPanel(new BorderLayout(5, 0));
                        JLabel lblValor = new JLabel(String.format("%s: %d (%.1f%%)", opcion.getLabel(), cantidad, porcentaje));
                        lblValor.setPreferredSize(new Dimension(200, 20));
                        panelBarra.add(lblValor, BorderLayout.WEST);

                        JProgressBar barraProgreso = new JProgressBar(0, 100);
                        barraProgreso.setValue((int) porcentaje);
                        barraProgreso.setStringPainted(true);
                        panelBarra.add(barraProgreso, BorderLayout.CENTER);

                        panel.add(panelBarra);
                    }

                    // Boton para ver grafico de torta individual
                    JButton btnVerTorta = new JButton("Ver en modo torta");
                    btnVerTorta.setFont(new Font("Arial", Font.PLAIN, 10));
                    btnVerTorta.addActionListener(e -> {
                        new GraficoTortaIndividualDialog(this, pregunta).setVisible(true);
                    });
                    JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    panelBoton.add(btnVerTorta);
                    panel.add(panelBoton);
                } else {
                    panel.add(new JLabel("  Sin respuestas aun"));
                }

                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        return panel;
    }

}
