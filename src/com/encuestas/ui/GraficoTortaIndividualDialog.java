package com.encuestas.ui;

import com.encuestas.dao.OpcionRespuestaDAO;
import com.encuestas.dao.RespuestaDAO;
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

public class GraficoTortaIndividualDialog extends JDialog {
    private Pregunta pregunta;
    private OpcionRespuestaDAO opcionDAO;
    private RespuestaDAO respuestaDAO;

    public GraficoTortaIndividualDialog(Frame parent, Pregunta pregunta) {
        super(parent, "Grafico de Torta - " + pregunta.getTexto(), true);
        this.pregunta = pregunta;
        this.opcionDAO = new OpcionRespuestaDAO();
        this.respuestaDAO = new RespuestaDAO();
        initComponents();
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel(pregunta.getTexto());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo, BorderLayout.NORTH);

        try {
            JPanel panelGrafico = crearGraficoTorta();
            add(panelGrafico, BorderLayout.CENTER);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar grafico: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearGraficoTorta() throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());

        Map<Integer, Integer> conteo = respuestaDAO.contarRespuestasPorValor(pregunta.getId());
        List<OpcionRespuesta> opciones = opcionDAO.listarPorPregunta(pregunta.getId());
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (OpcionRespuesta opcion : opciones) {
            int cantidad = conteo.getOrDefault(opcion.getValor(), 0);
            if (cantidad > 0) {
                dataset.setValue(opcion.getLabel() + " (" + cantidad + ")", cantidad);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
            pregunta.getTexto(),
            dataset,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(550, 400));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }
}
