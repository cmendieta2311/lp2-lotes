package lp2.fcyt.lp2_lotes.modelo.entidad;

/**
 * Entidad que representa un registro de la tabla usuarios.
 * En el patrón MVC, pertenece a la capa de modelo y solo transporta datos
 * entre el DAO, el controlador y la vista.
 *
 * @author cmendieta
 */
public class Usuarios {

    Integer id;
    String nombre;
    String email;
    String password_hash;
    String rol;
    Boolean activo;

    public Usuarios() {
    }

    public Usuarios(Integer id, String nombre, String email, String password_hash, String rol, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password_hash = password_hash;
        this.rol = rol;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
}
