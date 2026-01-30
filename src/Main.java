import com.encuestas.ui.LoginFrame;
import org.h2.tools.Server;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Iniciar servidor web de H2
        try {
            Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
            server.start();
            System.out.println("==============================================");
            System.out.println("H2 Console iniciada en: " + server.getURL());
            System.out.println("==============================================");
            System.out.println("Para acceder a la base de datos:");
            System.out.println("  JDBC URL: jdbc:h2:./data/encuestas");
            System.out.println("  Usuario: sa");
            System.out.println("  Password: (dejar vacio)");
            System.out.println("==============================================");
        } catch (SQLException e) {
            System.err.println("Error al iniciar H2 Console: " + e.getMessage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }
