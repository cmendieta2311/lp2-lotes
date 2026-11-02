package lp2.fcyt.lp2_lotes;

import lp2.fcyt.lp2_lotes.controlador.LoginController;
import lp2.fcyt.lp2_lotes.modelo.dao.Sesion;
import lp2.fcyt.lp2_lotes.vista.GUIPrincipal;

/**
 * Clase principal (punto de entrada de la aplicación). Primero muestra el
 * login y, solo si el usuario se autentica, abre la vista principal
 * (GUIPrincipal).
 *
 * @author cmendieta
 */
public class Lp2_lotes {

    public static void main(String[] args) {
        Sesion sesion = new Sesion();
        // El login es un diálogo modal: esta línea se bloquea hasta que se cierra
        new LoginController();
        // Si nadie inició sesión (se cerró el login), la aplicación termina
        if (sesion.estaLogeado()) {
            GUIPrincipal ventana = new GUIPrincipal();
            ventana.setVisible(true);
        } else {
            System.exit(0);
        }

    }
}
