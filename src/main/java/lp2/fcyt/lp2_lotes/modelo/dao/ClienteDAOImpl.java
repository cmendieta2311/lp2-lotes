/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.dao;

import lp2.fcyt.lp2_lotes.modelo.entidad.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lp2.fcyt.lp2_lotes.modelo.Conexion;

/**
 * Implementación del DAO para la entidad Cliente.
 * En el patrón MVC, pertenece a la capa de modelo y se encarga de realizar las operaciones de acceso a datos en la tabla clientes.
 *
 * @author cmendieta
 */
public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente buscarPorID(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cliente> listado(String valor) {
        List<Cliente> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {

            String sql = "select * from clientes where nombre || apellido ilike ? order by id asc";

            PreparedStatement st = cn.prepareStatement(sql); //
            st.setString(1, "%" + valor + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                //System.out.println(rs.getInt("id") +" - "+ rs.getString("nombre"));
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setTipoDocumento(rs.getString("tipo_documento"));
                cliente.setNumeroDocumento(rs.getString("numero_documento"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                lista.add(cliente);

            }

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
        return lista;
    }

    @Override
    public void insertar(Cliente obj) {
        String sql = "INSERT INTO public.clientes(\n" +
"	nombre, apellido, tipo_documento, numero_documento, telefono, email, direccion)\n" +
"	VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setString(1, obj.getNombre());
            st.setString(2, obj.getApellido());
            st.setString(3, obj.getTipoDocumento());
            st.setString(4, obj.getNumeroDocumento());
            st.setString(5, obj.getTelefono());
            st.setString(6, obj.getEmail());
            st.setString(7, obj.getDireccion());
            //st.setString(8, "current_date");
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

    @Override
    public void modificar(Cliente obj) {
        String sql = "UPDATE clientes\n" +
"	SET  nombre=?, apellido=?, tipo_documento=?, numero_documento=?, telefono=?, email=?, direccion=?\n" +
"	WHERE id=?;";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setString(1, obj.getNombre());
            st.setString(2, obj.getApellido());
            st.setString(3, obj.getTipoDocumento());
            st.setString(4, obj.getNumeroDocumento());
            st.setString(5, obj.getTelefono());
            st.setString(6, obj.getEmail());
            st.setString(7, obj.getDireccion());
            st.setInt(8, obj.getId());
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "delete from clientes where id=?";
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
