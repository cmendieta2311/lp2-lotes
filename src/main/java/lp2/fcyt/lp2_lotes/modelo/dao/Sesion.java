package lp2.fcyt.lp2_lotes.modelo.dao;

import lp2.fcyt.lp2_lotes.modelo.entidad.Usuarios;

/**
 * Guarda el usuario que inició sesión para que cualquier parte de la
 * aplicación (por ejemplo GUIPrincipal) pueda consultarlo.
 *
 * @author cmendieta
 */
public class Sesion {
    // al ser static, existe una sola copia para toda la aplicacion
    private static Usuarios usuario;

    public Sesion() {
    }

    /** Registra al usuario autenticado; lo llama el DAO tras un login correcto. */
    public static void inciarSession(Usuarios user){
        usuario = user;
    }

    /** Devuelve el usuario de la sesión actual (null si nadie inició sesión). */
    public static Usuarios getUsuario() {
        return usuario;
    }

    /** Indica si hay un usuario autenticado. */
    public static boolean estaLogeado (){
        return usuario != null;
    }
    
    
}
