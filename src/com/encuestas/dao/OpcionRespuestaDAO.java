package com.encuestas.dao;

import com.encuestas.dominio.OpcionRespuesta;
import com.encuestas.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OpcionRespuestaDAO {

    public void crear(OpcionRespuesta opcion) throws SQLException {
        String sql = "INSERT INTO OpcionRespuesta (preguntaId, valor, label) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, opcion.getPreguntaId());
            pstmt.setInt(2, opcion.getValor());
            pstmt.setString(3, opcion.getLabel());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    opcion.setId(rs.getInt(1));
                }
            }
        }
    }

    public OpcionRespuesta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM OpcionRespuesta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new OpcionRespuesta(
                        rs.getInt("id"),
                        rs.getInt("preguntaId"),
                        rs.getInt("valor"),
                        rs.getString("label")
                    );
                }
            }
        }
        return null;
    }

    public List<OpcionRespuesta> listarPorPregunta(int preguntaId) throws SQLException {
        List<OpcionRespuesta> opciones = new ArrayList<>();
        String sql = "SELECT * FROM OpcionRespuesta WHERE preguntaId = ? ORDER BY valor";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, preguntaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    opciones.add(new OpcionRespuesta(
                        rs.getInt("id"),
                        rs.getInt("preguntaId"),
                        rs.getInt("valor"),
                        rs.getString("label")
                    ));
                }
            }
        }
        return opciones;
    }

    public void actualizar(OpcionRespuesta opcion) throws SQLException {
        String sql = "UPDATE OpcionRespuesta SET valor = ?, label = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, opcion.getValor());
            pstmt.setString(2, opcion.getLabel());
            pstmt.setInt(3, opcion.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM OpcionRespuesta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
