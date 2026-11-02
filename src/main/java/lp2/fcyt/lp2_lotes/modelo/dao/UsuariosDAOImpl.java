package lp2.fcyt.lp2_lotes.modelo.dao;

import lp2.fcyt.lp2_lotes.modelo.entidad.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lp2.fcyt.lp2_lotes.modelo.Conexion;

/**
 * Implementación del DAO para la entidad Usuarios. En el patrón MVC, pertenece
 * a la capa de modelo y se encarga de realizar las operaciones de acceso a
 * datos en la tabla usuarios.
 *
 * @author cmendieta
 */
public class UsuariosDAOImpl implements CrudDAO<Usuarios> {

    @Override
    public Usuarios buscarPorID(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Usuarios> listado(String valor) {
        List<Usuarios> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {

            String sql = "select * from usuarios where nombre ilike ? order by id asc";

            PreparedStatement st = cn.prepareStatement(sql); //
            st.setString(1, "%" + valor + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                //System.out.println(rs.getInt("id") +" - "+ rs.getString("nombre"));
                Usuarios proyecto = new Usuarios();
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
    public void insertar(Usuarios obj) {
        String sql = "INSERT INTO public.usuarios(\n"
                + "	 nombre, email, password_hash, rol, activo)\n"
                + "	VALUES (?, ?, md5(?), ?, ?)";
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
    public void modificar(Usuarios obj) {
        String sql = "update usuarios set nombre=? where id=?";
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
        String sql = "delete from usuarios where id=?";
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }

    }

    /**
     * Verifica email y contraseña de un usuario activo. Si son correctos,
     * guarda el usuario en Sesion y registra su último acceso.
     *
     * @return true si las credenciales son válidas, false si no lo son
     * @throws SQLException si no se puede acceder a la base de datos. Se
     * propaga en lugar de capturarla para que el controlador pueda distinguir
     * "credenciales incorrectas" de "base de datos no disponible".
     */
    public boolean login(Usuarios user) throws SQLException {
        // La contraseña se compara con md5 dentro de la base de datos: Java nunca lee el hash guardado.
        // Se listan las columnas necesarias en lugar de select * (password_hash no se trae).
        String sql = "select id, nombre, email, rol, activo from usuarios"
                + " where email=? and password_hash=md5(?) and activo=true";
        // try-with-resources cierra Connection, PreparedStatement y ResultSet
        // automáticamente, incluso si ocurre un error.
        try (Connection cn = Conexion.conectar();
                // Los parámetros ? de PreparedStatement evitan la inyección SQL
                PreparedStatement st = cn.prepareStatement(sql)) {
            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword_hash());
            try (ResultSet rs = st.executeQuery()) {
                // next() devuelve true si existe una fila: las credenciales son válidas
                if (rs.next()) {
                    Usuarios usuario = new Usuarios();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    // El enum rol_usuario de PostgreSQL se lee como texto
                    usuario.setRol(rs.getString("rol"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    // La contraseña no se guarda en la sesión: no se necesita
                    // después del login y así no queda en memoria.

                    // registramos quien inició sesión dentro del sistema
                    Sesion.inciarSession(usuario);

                    ultimoAcceso(usuario);
                    return true;
                }
            }
        }
        return false;
    }
    
    /** Actualiza la fecha y hora del último acceso del usuario. */
    public void ultimoAcceso (Usuarios user){
        String sql ="update usuarios set ultimo_acceso = current_timestamp where id = ?";
      
        try (Connection cn = Conexion.conectar()) {

            PreparedStatement st = cn.prepareStatement(sql); //

            st.setInt(1, user.getId());
     
            st.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error de conexion");
            ex.printStackTrace();

        }
    }

}
