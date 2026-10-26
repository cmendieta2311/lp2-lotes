/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.dao;

import lp2.fcyt.lp2_lotes.modelo.entidad.Manzanas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lp2.fcyt.lp2_lotes.modelo.Conexion;
import lp2.fcyt.lp2_lotes.modelo.entidad.Proyecto;

/**
 * Implementación del DAO para la entidad Manzanas. En el patrón MVC, pertenece
 * a la capa de modelo y se encarga de realizar las operaciones de acceso a
 * datos en la tabla manzanas.
 *
 * @author cmendieta
 */
public class ManzanaDAOImpl implements CrudDAO<Manzanas> {

    @Override
    public Manzanas buscarPorID(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Manzanas> listado(String valor) {
        List<Manzanas> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {

            String sql = "SELECT m.*,p.nombre as proyecto\n"
                    + "FROM manzanas as m\n"
                    + "inner join proyectos as p on p.id=m.proyecto_id where numero ilike ? order by id asc";

            PreparedStatement st = cn.prepareStatement(sql); //
            st.setString(1, "%" + valor + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                //System.out.println(rs.getInt("id") +" - "+ rs.getString("nombre"));
                Manzanas manzana = new Manzanas();
                // cargando datos de proyecto
                Proyecto proyecto = new Proyecto();
                proyecto.setId(rs.getInt("proyecto_id"));
                proyecto.setNombre(rs.getString("proyecto"));

                //cargando datos de manzana
                manzana.setId(rs.getInt("id"));
                manzana.setProyecto(proyecto);
                manzana.setNumero(rs.getString("numero"));
                manzana.setDescripcion(rs.getString("descripcion"));
                lista.add(manzana);

            }

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
        return lista;
    }

    @Override
    public void insertar(Manzanas obj) {
        String sql = "INSERT INTO manzanas(\n"
                + "	proyecto_id, numero, descripcion)\n"
                + "	VALUES ( ?, ?, ?);";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setInt(1, obj.getProyecto().getId());
            st.setString(2, obj.getNumero());
            st.setString(3, obj.getDescripcion().trim());
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

    @Override
    public void modificar(Manzanas obj) {
        String sql = "update manzanas set proyecto_id=?, numero=?, descripcion=? where id=?";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setInt(1, obj.getProyecto().getId());
            st.setString(2, obj.getNumero());
            st.setString(3, obj.getDescripcion());
            st.setInt(4, obj.getId());
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "delete from manzanas where id=?";
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
