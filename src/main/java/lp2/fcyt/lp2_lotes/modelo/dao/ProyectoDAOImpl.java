/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.dao;

import lp2.fcyt.lp2_lotes.modelo.entidad.Proyecto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lp2.fcyt.lp2_lotes.modelo.Conexion;

/**
 *
 * @author cmendieta
 */
public class ProyectoDAOImpl implements CrudDAO<Proyecto> {

    @Override
    public Proyecto buscarPorID(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Proyecto> listado(String valor) {
        List<Proyecto> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {

            String sql = "select * from proyectos where nombre ilike ? order by id asc";

            PreparedStatement st = cn.prepareStatement(sql); //
            st.setString(1, "%" + valor + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                //System.out.println(rs.getInt("id") +" - "+ rs.getString("nombre"));
                Proyecto proyecto = new Proyecto();
                proyecto.setId(rs.getInt("id"));
                proyecto.setNombre(rs.getString("nombre"));
                lista.add(proyecto);

            }

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
        return lista;
    }

    @Override
    public void insertar(Proyecto obj) {
        String sql = "insert into proyectos (nombre) values(?)";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setString(1, obj.getNombre());
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

    @Override
    public void modificar(Proyecto obj) {
        String sql = "update proyectos set nombre=? where id=?";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setString(1, obj.getNombre());
            st.setInt(2, obj.getId());
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "delete from proyectos where id=?";
          try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
        
    }

}
