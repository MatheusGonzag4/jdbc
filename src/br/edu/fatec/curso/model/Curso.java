package br.edu.fatec.curso.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Curso {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_fatec";
    private static final String USER = "fatec";
    private static final String PASSWORD = "senha123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
