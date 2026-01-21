package com.encuestas.dao;

import com.encuestas.dominio.Respuesta;
import com.encuestas.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RespuestaDAO {

    public void crear(Respuesta respuesta) throws SQLException {
        String sql = "INSERT INTO Respuesta (encuestaId, preguntaId, valorSeleccionado, respuestaTexto, fechaRespuesta) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, respuesta.getEncuestaId());
            pstmt.setInt(2, respuesta.getPreguntaId());

            if (respuesta.getValorSeleccionado() == 0) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, respuesta.getValorSeleccionado());
            }

            pstmt.setString(4, respuesta.getRespuestaTexto());
            pstmt.setTimestamp(5, Timestamp.valueOf(respuesta.getFechaRespuesta()));
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    respuesta.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Respuesta> listarPorEncuesta(int encuestaId) throws SQLException {
        List<Respuesta> respuestas = new ArrayList<>();
        String sql = "SELECT * FROM Respuesta WHERE encuestaId = ? ORDER BY fechaRespuesta DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, encuestaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int valorSel = rs.getInt("valorSeleccionado");
                    if (rs.wasNull()) {
                        valorSel = 0;
                    }
                    respuestas.add(new Respuesta(
                        rs.getInt("id"),
                        rs.getInt("encuestaId"),
                        rs.getInt("preguntaId"),
                        valorSel,
                        rs.getString("respuestaTexto"),
                        rs.getTimestamp("fechaRespuesta").toLocalDateTime()
                    ));
                }
            }
        }
        return respuestas;
    }

    public List<Respuesta> listarPorEncuestaYFechas(int encuestaId, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        List<Respuesta> respuestas = new ArrayList<>();
        String sql = "SELECT * FROM Respuesta WHERE encuestaId = ? AND fechaRespuesta >= ? AND fechaRespuesta < ? ORDER BY fechaRespuesta DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, encuestaId);
            pstmt.setTimestamp(2, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            pstmt.setTimestamp(3, Timestamp.valueOf(fechaFin.plusDays(1).atStartOfDay()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int valorSel = rs.getInt("valorSeleccionado");
                    if (rs.wasNull()) {
                        valorSel = 0;
                    }
                    respuestas.add(new Respuesta(
                        rs.getInt("id"),
                        rs.getInt("encuestaId"),
                        rs.getInt("preguntaId"),
                        valorSel,
                        rs.getString("respuestaTexto"),
                        rs.getTimestamp("fechaRespuesta").toLocalDateTime()
                    ));
                }
            }
        }
        return respuestas;
    }

    public Map<Integer, Integer> contarRespuestasPorValor(int preguntaId) throws SQLException {
        Map<Integer, Integer> conteo = new HashMap<>();
        String sql = "SELECT valorSeleccionado, COUNT(*) as cantidad FROM Respuesta WHERE preguntaId = ? GROUP BY valorSeleccionado";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, preguntaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    conteo.put(rs.getInt("valorSeleccionado"), rs.getInt("cantidad"));
                }
            }
        }
        return conteo;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Respuesta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
