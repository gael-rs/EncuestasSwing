package com.encuestas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    private static final String URL = "jdbc:h2:./data/encuestas";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.h2.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                inicializarTablas();
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver H2 no encontrado", e);
            }
        }
        return connection;
    }

    private static void inicializarTablas() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Tabla Usuario
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Usuario (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "rol VARCHAR(20) NOT NULL)"
            );

            // Tabla Encuesta
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Encuesta (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "titulo VARCHAR(200) NOT NULL, " +
                "fechaCreacion DATE NOT NULL, " +
                "activa BOOLEAN NOT NULL)"
            );

            // Tabla Pregunta
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Pregunta (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "texto VARCHAR(500) NOT NULL, " +
                "valorMin INT NOT NULL, " +
                "valorMax INT NOT NULL, " +
                "encuestaId INT NOT NULL, " +
                "esTextoLibre BOOLEAN DEFAULT FALSE, " +
                "FOREIGN KEY (encuestaId) REFERENCES Encuesta(id) ON DELETE CASCADE)"
            );

            try {
                stmt.execute("ALTER TABLE Pregunta ADD COLUMN IF NOT EXISTS esTextoLibre BOOLEAN DEFAULT FALSE");
            } catch (SQLException e) {
            }

            // Tabla OpcionRespuesta
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS OpcionRespuesta (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "preguntaId INT NOT NULL, " +
                "valor INT NOT NULL, " +
                "label VARCHAR(100) NOT NULL, " +
                "FOREIGN KEY (preguntaId) REFERENCES Pregunta(id) ON DELETE CASCADE)"
            );

            // Tabla Respuesta
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Respuesta (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "encuestaId INT NOT NULL, " +
                "preguntaId INT NOT NULL, " +
                "valorSeleccionado INT, " +
                "respuestaTexto VARCHAR(500), " +
                "fechaRespuesta TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (encuestaId) REFERENCES Encuesta(id), " +
                "FOREIGN KEY (preguntaId) REFERENCES Pregunta(id))"
            );

            try {
                stmt.execute("ALTER TABLE Respuesta ADD COLUMN IF NOT EXISTS respuestaTexto VARCHAR(500)");
                stmt.execute("ALTER TABLE Respuesta ALTER COLUMN valorSeleccionado INT NULL");
            } catch (SQLException e) {
            }

            // Insertar usuarios de prueba si no existen
            stmt.execute(
                "MERGE INTO Usuario (id, nombre, rol) " +
                "KEY(id) VALUES (1, 'Admin', 'ADMIN')"
            );
            stmt.execute(
                "MERGE INTO Usuario (id, nombre, rol) " +
                "KEY(id) VALUES (2, 'Promotor', 'PROMOTOR')"
            );
        }
    }

    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
