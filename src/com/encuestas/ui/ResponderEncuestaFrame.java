package com.encuestas.ui;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponderEncuestaFrame extends JFrame {
    private Encuesta encuesta;
    private PreguntaDAO preguntaDAO;
    private OpcionRespuestaDAO opcionDAO;
    private RespuestaDAO respuestaDAO;

    private List<Pregunta> preguntas;
    private Map<Integer, ButtonGroup> gruposRespuestas;
    private Map<Integer, JTextField> camposTexto;
    private JPanel panelPreguntas;
    private JButton btnGuardar, btnCancelar;

    public ResponderEncuestaFrame(Encuesta encuesta) {
        this.encuesta = encuesta;
        this.preguntaDAO = new PreguntaDAO();
        this.opcionDAO = new OpcionRespuestaDAO();
        this.respuestaDAO = new RespuestaDAO();
        this.gruposRespuestas = new HashMap<>();
        this.camposTexto = new HashMap<>();
        initComponents();
        cargarPreguntas();
    }

    private void initComponents() {
        setTitle("Responder Encuesta - " + encuesta.getTitulo());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitulo = new JLabel(encuesta.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo);
        add(panelSuperior, BorderLayout.NORTH);

        panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new BoxLayout(panelPreguntas, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelPreguntas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Respuestas");
        btnGuardar.addActionListener(e -> guardarRespuestas());
        panelBotones.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarPreguntas() {
        try {
            preguntas = preguntaDAO.listarPorEncuesta(encuesta.getId());

            if (preguntas.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Esta encuesta no tiene preguntas configuradas",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
                dispose();
                return;
            }

            for (Pregunta pregunta : preguntas) {
                JPanel panelPregunta = new JPanel();
                panelPregunta.setLayout(new BoxLayout(panelPregunta, BoxLayout.Y_AXIS));
                panelPregunta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createTitledBorder(pregunta.getTexto())
                ));

                if (pregunta.isEsTextoLibre()) {
                    JTextField txtRespuesta = new JTextField(40);
                    camposTexto.put(pregunta.getId(), txtRespuesta);
                    JPanel panelTexto = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    panelTexto.add(new JLabel("Respuesta: "));
                    panelTexto.add(txtRespuesta);
                    panelPregunta.add(panelTexto);
                } else {
                    List<OpcionRespuesta> opciones = opcionDAO.listarPorPregunta(pregunta.getId());
                    ButtonGroup grupo = new ButtonGroup();
                    gruposRespuestas.put(pregunta.getId(), grupo);

                    JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    for (OpcionRespuesta opcion : opciones) {
                        JRadioButton radioButton = new JRadioButton(opcion.getLabel());
                        radioButton.setActionCommand(String.valueOf(opcion.getValor()));
                        grupo.add(radioButton);
                        panelOpciones.add(radioButton);
                    }
                    panelPregunta.add(panelOpciones);
                }

                panelPregunta.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelPregunta.getPreferredSize().height + 20));
                panelPreguntas.add(panelPregunta);
                panelPreguntas.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            panelPreguntas.add(Box.createVerticalGlue());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar preguntas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void guardarRespuestas() {
        List<Respuesta> respuestas = new ArrayList<>();

        for (Pregunta pregunta : preguntas) {
            Respuesta respuesta = new Respuesta();
            respuesta.setEncuestaId(encuesta.getId());
            respuesta.setPreguntaId(pregunta.getId());
            respuesta.setFechaRespuesta(LocalDateTime.now());

            if (pregunta.isEsTextoLibre()) {
                JTextField campo = camposTexto.get(pregunta.getId());
                String textoRespuesta = campo.getText().trim();

                if (textoRespuesta.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Debe responder todas las preguntas",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                respuesta.setRespuestaTexto(textoRespuesta);
                respuesta.setValorSeleccionado(0);
            } else {
                ButtonGroup grupo = gruposRespuestas.get(pregunta.getId());
                String valorSeleccionado = null;

                if (grupo.getSelection() != null) {
                    valorSeleccionado = grupo.getSelection().getActionCommand();
                }

                if (valorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this,
                        "Debe responder todas las preguntas",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                respuesta.setValorSeleccionado(Integer.parseInt(valorSeleccionado));
            }

            respuestas.add(respuesta);
        }

        try {
            for (Respuesta respuesta : respuestas) {
                respuestaDAO.crear(respuesta);
            }

            JOptionPane.showMessageDialog(this,
                "Respuestas guardadas exitosamente",
                "Exito",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar respuestas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
