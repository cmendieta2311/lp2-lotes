/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cmendieta
 */
public class Conexion {

    private static final String ULR = "jdbc:postgresql://localhost:5432/fcyt";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";
    
    public static Connection conectar() throws SQLException{
        return DriverManager.getConnection(ULR, USER, PASSWORD);
    }

}
