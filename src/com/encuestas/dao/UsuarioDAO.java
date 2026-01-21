package com.encuestas.dao;

import com.encuestas.dominio.Usuario;
import com.encuestas.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void crear(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (nombre, rol) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getRol());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rol")
                    );
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ORDER BY nombre";
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("rol")
                ));
            }
        }
        return usuarios;
    }

    public List<Usuario> listarPorRol(String rol) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE rol = ? ORDER BY nombre";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rol);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rol")
                    ));
                }
            }
        }
        return usuarios;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
