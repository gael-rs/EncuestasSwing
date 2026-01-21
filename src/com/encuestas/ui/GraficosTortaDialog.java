package com.encuestas.ui;

import com.encuestas.dao.OpcionRespuestaDAO;
import com.encuestas.dao.PreguntaDAO;
import com.encuestas.dao.RespuestaDAO;
import com.encuestas.dominio.Encuesta;
import com.encuestas.dominio.OpcionRespuesta;
import com.encuestas.dominio.Pregunta;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GraficosTortaDialog extends JDialog {
    private Encuesta encuesta;
    private PreguntaDAO preguntaDAO;
    private OpcionRespuestaDAO opcionDAO;
    private RespuestaDAO respuestaDAO;

    private JPanel panelGraficos;
    private JButton btnCerrar;

    public GraficosTortaDialog(Frame parent, Encuesta encuesta) {
        super(parent, "Graficos - " + encuesta.getTitulo(), true);
        this.encuesta = encuesta;
        this.preguntaDAO = new PreguntaDAO();
        this.opcionDAO = new OpcionRespuestaDAO();
        this.respuestaDAO = new RespuestaDAO();
        initComponents();
        cargarGraficos();
    }

    private void initComponents() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Graficos de Torta - " + encuesta.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        panelGraficos = new JPanel();
        panelGraficos.setLayout(new GridLayout(0, 2, 10, 10));
        JScrollPane scrollPane = new JScrollPane(panelGraficos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarGraficos() {
        try {
            List<Pregunta> preguntas = preguntaDAO.listarPorEncuesta(encuesta.getId());

            for (Pregunta pregunta : preguntas) {
                if (!pregunta.isEsTextoLibre()) {
                    JPanel panelGrafico = crearGraficoTorta(pregunta);
                    panelGraficos.add(panelGrafico);
                }
            }

            if (panelGraficos.getComponentCount() == 0) {
                JLabel lblSinGraficos = new JLabel("No hay graficos disponibles");
                lblSinGraficos.setHorizontalAlignment(SwingConstants.CENTER);
                panelGraficos.add(lblSinGraficos);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar graficos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearGraficoTorta(Pregunta pregunta) throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(pregunta.getTexto()));

        Map<Integer, Integer> conteo = respuestaDAO.contarRespuestasPorValor(pregunta.getId());
        List<OpcionRespuesta> opciones = opcionDAO.listarPorPregunta(pregunta.getId());
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (OpcionRespuesta opcion : opciones) {
            int cantidad = conteo.getOrDefault(opcion.getValor(), 0);
            dataset.setValue(opcion.getLabel() + " (" + cantidad + ")", cantidad);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            pregunta.getTexto(),
            dataset,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }
}
