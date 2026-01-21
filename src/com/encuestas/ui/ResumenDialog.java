package com.encuestas.ui;

import com.encuestas.dao.OpcionRespuestaDAO;
import com.encuestas.dao.PreguntaDAO;
import com.encuestas.dao.RespuestaDAO;
import com.encuestas.dominio.OpcionRespuesta;
import com.encuestas.dominio.Pregunta;
import com.encuestas.dominio.Respuesta;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ResumenDialog extends JDialog {
    private int encuestaId;
    private PreguntaDAO preguntaDAO;
    private OpcionRespuestaDAO opcionDAO;
    private RespuestaDAO respuestaDAO;

    private JTextField txtAnioInicio, txtMesInicio, txtDiaInicio;
    private JTextField txtAnioFin, txtMesFin, txtDiaFin;
    private JTextArea txtResumen;
    private JButton btnGenerar, btnCerrar;

    public ResumenDialog(Frame parent, int encuestaId, String tituloEncuesta) {
        super(parent, "Resumen - " + tituloEncuesta, true);
        this.encuestaId = encuestaId;
        this.preguntaDAO = new PreguntaDAO();
        this.opcionDAO = new OpcionRespuestaDAO();
        this.respuestaDAO = new RespuestaDAO();
        initComponents();
    }

    private void agregarValidacionDosDigitos(JTextField textField) {
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String texto = textField.getText().trim();
                if (!texto.isEmpty() && texto.length() == 1) {
                    textField.setText("0" + texto);
                }
            }
        });
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel panelFiltros = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fecha Inicio
        gbc.gridx = 0; gbc.gridy = 0;
        panelFiltros.add(new JLabel("Fecha Inicio:"), gbc);

        gbc.gridx = 1;
        panelFiltros.add(new JLabel("Año:"), gbc);
        gbc.gridx = 2;
        txtAnioInicio = new JTextField(4);
        panelFiltros.add(txtAnioInicio, gbc);

        gbc.gridx = 3;
        panelFiltros.add(new JLabel("Mes:"), gbc);
        gbc.gridx = 4;
        txtMesInicio = new JTextField(2);
        agregarValidacionDosDigitos(txtMesInicio);
        panelFiltros.add(txtMesInicio, gbc);

        gbc.gridx = 5;
        panelFiltros.add(new JLabel("Día:"), gbc);
        gbc.gridx = 6;
        txtDiaInicio = new JTextField(2);
        agregarValidacionDosDigitos(txtDiaInicio);
        panelFiltros.add(txtDiaInicio, gbc);

        // Fecha Fin
        gbc.gridx = 0; gbc.gridy = 1;
        panelFiltros.add(new JLabel("Fecha Fin:"), gbc);

        gbc.gridx = 1;
        panelFiltros.add(new JLabel("Año:"), gbc);
        gbc.gridx = 2;
        txtAnioFin = new JTextField(4);
        panelFiltros.add(txtAnioFin, gbc);

        gbc.gridx = 3;
        panelFiltros.add(new JLabel("Mes:"), gbc);
        gbc.gridx = 4;
        txtMesFin = new JTextField(2);
        agregarValidacionDosDigitos(txtMesFin);
        panelFiltros.add(txtMesFin, gbc);

        gbc.gridx = 5;
        panelFiltros.add(new JLabel("Día:"), gbc);
        gbc.gridx = 6;
        txtDiaFin = new JTextField(2);
        agregarValidacionDosDigitos(txtDiaFin);
        panelFiltros.add(txtDiaFin, gbc);

        // Botón Generar
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGenerar = new JButton("Generar Resumen");
        btnGenerar.addActionListener(e -> generarResumen());
        panelFiltros.add(btnGenerar, gbc);

        add(panelFiltros, BorderLayout.NORTH);

        txtResumen = new JTextArea();
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResumen);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);

        generarResumen();
    }

    private void generarResumen() {
        try {
            List<Respuesta> respuestas;

            String anioInicio = txtAnioInicio.getText().trim();
            String mesInicio = txtMesInicio.getText().trim();
            String diaInicio = txtDiaInicio.getText().trim();

            String anioFin = txtAnioFin.getText().trim();
            String mesFin = txtMesFin.getText().trim();
            String diaFin = txtDiaFin.getText().trim();

            boolean fechasCompletas = !anioInicio.isEmpty() && !mesInicio.isEmpty() && !diaInicio.isEmpty() &&
                                      !anioFin.isEmpty() && !mesFin.isEmpty() && !diaFin.isEmpty();

            if (fechasCompletas) {
                // Formatear mes y día a 2 dígitos si es necesario
                if (mesInicio.length() == 1) mesInicio = "0" + mesInicio;
                if (diaInicio.length() == 1) diaInicio = "0" + diaInicio;
                if (mesFin.length() == 1) mesFin = "0" + mesFin;
                if (diaFin.length() == 1) diaFin = "0" + diaFin;

                String strFechaInicio = anioInicio + "-" + mesInicio + "-" + diaInicio;
                String strFechaFin = anioFin + "-" + mesFin + "-" + diaFin;

                LocalDate fechaInicio = LocalDate.parse(strFechaInicio, DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate fechaFin = LocalDate.parse(strFechaFin, DateTimeFormatter.ISO_LOCAL_DATE);
                respuestas = respuestaDAO.listarPorEncuestaYFechas(encuestaId, fechaInicio, fechaFin);
            } else {
                respuestas = respuestaDAO.listarPorEncuesta(encuestaId);
            }

            StringBuilder resumen = new StringBuilder();
            resumen.append("RESUMEN DE RESPUESTAS\n");
            resumen.append("=====================\n\n");
            resumen.append("Total de respuestas: ").append(respuestas.size()).append("\n\n");

            List<Pregunta> preguntas = preguntaDAO.listarPorEncuesta(encuestaId);

            for (Pregunta pregunta : preguntas) {
                resumen.append("Pregunta: ").append(pregunta.getTexto()).append("\n");
                resumen.append("----------------------------------------\n");

                if (pregunta.isEsTextoLibre()) {
                    for (Respuesta r : respuestas) {
                        if (r.getPreguntaId() == pregunta.getId() && r.getRespuestaTexto() != null) {
                            resumen.append("  - ").append(r.getRespuestaTexto()).append("\n");
                        }
                    }
                } else {
                    // Contar respuestas de la lista filtrada (no de toda la BD)
                    Map<Integer, Integer> conteo = new java.util.HashMap<>();
                    for (Respuesta r : respuestas) {
                        if (r.getPreguntaId() == pregunta.getId() && r.getValorSeleccionado() != 0) {
                            int valor = r.getValorSeleccionado();
                            conteo.put(valor, conteo.getOrDefault(valor, 0) + 1);
                        }
                    }

                    List<OpcionRespuesta> opciones = opcionDAO.listarPorPregunta(pregunta.getId());

                    for (OpcionRespuesta opcion : opciones) {
                        int cantidad = conteo.getOrDefault(opcion.getValor(), 0);
                        resumen.append("  ").append(opcion.getLabel()).append(": ").append(cantidad).append(" respuestas\n");
                    }
                }
                resumen.append("\n");
            }

            txtResumen.setText(resumen.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al generar resumen: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error en el formato de fecha. Verifique que los valores sean válidos:\n" +
                "- Año: 4 dígitos (ej: 2024)\n" +
                "- Mes: 1-12 (ej: 1 o 01)\n" +
                "- Día: 1-31 (ej: 5 o 05)",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
