package main;

import login.Login;
import fooddeliverysystem.server.javaconnect;
import java.sql.Connection;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Unable to set look and feel as windows!");
        }
        Connection connection = javaconnect.setconnection();
        new Login(connection).setVisible(true);
    }
}
