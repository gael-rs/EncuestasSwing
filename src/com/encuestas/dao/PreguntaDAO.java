package com.encuestas.dao;

import com.encuestas.dominio.Pregunta;
import com.encuestas.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAO {

    public void crear(Pregunta pregunta) throws SQLException {
        String sql = "INSERT INTO Pregunta (texto, valorMin, valorMax, encuestaId, esTextoLibre) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, pregunta.getTexto());
            pstmt.setInt(2, pregunta.getValorMin());
            pstmt.setInt(3, pregunta.getValorMax());
            pstmt.setInt(4, pregunta.getEncuestaId());
            pstmt.setBoolean(5, pregunta.isEsTextoLibre());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pregunta.setId(rs.getInt(1));
                }
            }
        }
    }

    public Pregunta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Pregunta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Pregunta(
                        rs.getInt("id"),
                        rs.getString("texto"),
                        rs.getInt("valorMin"),
                        rs.getInt("valorMax"),
                        rs.getInt("encuestaId"),
                        rs.getBoolean("esTextoLibre")
                    );
                }
            }
        }
        return null;
    }

    public List<Pregunta> listarPorEncuesta(int encuestaId) throws SQLException {
        List<Pregunta> preguntas = new ArrayList<>();
        String sql = "SELECT * FROM Pregunta WHERE encuestaId = ? ORDER BY id";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, encuestaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    preguntas.add(new Pregunta(
                        rs.getInt("id"),
                        rs.getString("texto"),
                        rs.getInt("valorMin"),
                        rs.getInt("valorMax"),
                        rs.getInt("encuestaId"),
                        rs.getBoolean("esTextoLibre")
                    ));
                }
            }
        }
        return preguntas;
    }

    public void actualizar(Pregunta pregunta) throws SQLException {
        String sql = "UPDATE Pregunta SET texto = ?, valorMin = ?, valorMax = ?, esTextoLibre = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pregunta.getTexto());
            pstmt.setInt(2, pregunta.getValorMin());
            pstmt.setInt(3, pregunta.getValorMax());
            pstmt.setBoolean(4, pregunta.isEsTextoLibre());
            pstmt.setInt(5, pregunta.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Pregunta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
