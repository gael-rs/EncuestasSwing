package com.encuestas.dao;

import com.encuestas.dominio.Encuesta;
import com.encuestas.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EncuestaDAO {

    public void crear(Encuesta encuesta) throws SQLException {
        String sql = "INSERT INTO Encuesta (titulo, fechaCreacion, activa) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, encuesta.getTitulo());
            pstmt.setDate(2, Date.valueOf(encuesta.getFechaCreacion()));
            pstmt.setBoolean(3, encuesta.isActiva());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    encuesta.setId(rs.getInt(1));
                }
            }
        }
    }

    public Encuesta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Encuesta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Encuesta(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getDate("fechaCreacion").toLocalDate(),
                        rs.getBoolean("activa")
                    );
                }
            }
        }
        return null;
    }

    public List<Encuesta> listarTodas() throws SQLException {
        List<Encuesta> encuestas = new ArrayList<>();
        String sql = "SELECT * FROM Encuesta ORDER BY fechaCreacion DESC";
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                encuestas.add(new Encuesta(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getDate("fechaCreacion").toLocalDate(),
                    rs.getBoolean("activa")
                ));
            }
        }
        return encuestas;
    }

    public List<Encuesta> listarActivas() throws SQLException {
        List<Encuesta> encuestas = new ArrayList<>();
        String sql = "SELECT * FROM Encuesta WHERE activa = TRUE ORDER BY fechaCreacion DESC";
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                encuestas.add(new Encuesta(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getDate("fechaCreacion").toLocalDate(),
                    rs.getBoolean("activa")
                ));
            }
        }
        return encuestas;
    }

    public void actualizar(Encuesta encuesta) throws SQLException {
        String sql = "UPDATE Encuesta SET titulo = ?, fechaCreacion = ?, activa = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, encuesta.getTitulo());
            pstmt.setDate(2, Date.valueOf(encuesta.getFechaCreacion()));
            pstmt.setBoolean(3, encuesta.isActiva());
            pstmt.setInt(4, encuesta.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Encuesta WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
