/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package lp2.fcyt.lp2_lotes;

import lp2.fcyt.lp2_lotes.vista.GUIPrincipal;

/**
 * Clase principal (punto de entrada de la aplicación).
 * En el patrón MVC, inicia la vista principal (GUIPrincipal) y pone en marcha la interfaz gráfica.
 *
 * @author cmendieta
 */
public class Lp2_lotes {

    public static void main(String[] args) {
        GUIPrincipal ventana = new GUIPrincipal();
        ventana.setVisible(true);
    }
}
