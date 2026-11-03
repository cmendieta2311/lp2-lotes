package lp2.fcyt.lp2_lotes.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import lp2.fcyt.lp2_lotes.modelo.dao.UsuariosDAOImpl;
import lp2.fcyt.lp2_lotes.modelo.entidad.Usuarios;
import lp2.fcyt.lp2_lotes.vista.GUILogin;

/**
 * Controlador del inicio de sesión.
 * En el patrón MVC, recibe los datos de la vista (GUILogin), los valida contra
 * el modelo (UsuariosDAOImpl) y decide si se cierra el login o se muestra un error.
 *
 * @author cmendieta
 */
public class LoginController implements ActionListener {

    GUILogin gui;
    UsuariosDAOImpl dao;

    /**
     * Crea la vista, registra los eventos y muestra el login.
     * Como el diálogo es modal, setVisible(true) bloquea hasta que se llame a dispose().
     */
    public LoginController() {
        this.dao = new UsuariosDAOImpl();
        this.gui = new GUILogin(null, true);

        // Escuchar eventos del botones
        this.gui.btnIngresar.addActionListener(this);
        // Presionar Enter en el campo contraseña también intenta ingresar
        this.gui.txtContraseña.addActionListener(this);
        this.gui.setVisible(true);
    }

    /**
     * Arma un objeto Usuarios con lo escrito en la vista y lo valida con el DAO.
     * Si el login es correcto, cierra el diálogo; si no, limpia la contraseña.
     * Un error de base de datos se informa con un mensaje distinto al de
     * credenciales incorrectas.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Usuarios user = new Usuarios();
        user.setEmail(this.gui.txtUsuario.getText().trim());
        // getPassword() es la forma recomendada de leer un JPasswordField.
        // No se aplica trim(): los espacios forman parte de la contraseña.
        user.setPassword_hash(new String(this.gui.txtContraseña.getPassword()));
        try {
            boolean v_control = dao.login(user);
            if (v_control) {
                this.gui.dispose();
            } else {
                mostrarError("Usuario o contraseña incorrectos", "Error de inicio de sesión");
                this.gui.txtContraseña.setText("");
                this.gui.txtContraseña.requestFocusInWindow();
            }
        } catch (SQLException ex) {
            // Problema técnico (servidor caído, credenciales de conexión, etc.):
            // se registra el detalle y se muestra un mensaje claro al usuario.
            ex.printStackTrace();
            mostrarError("No se pudo conectar a la base de datos", "Error de conexión");
        }
    }

    /**
     * Muestra un mensaje de error centrado sobre la ventana de login y
     * siempre por encima de ella.
     */
    private void mostrarError(String mensaje, String titulo) {
        JOptionPane pane = new JOptionPane(mensaje, JOptionPane.ERROR_MESSAGE);
        JDialog dialogo = pane.createDialog(gui, titulo);
        dialogo.setAlwaysOnTop(true);
        dialogo.setLocationRelativeTo(gui);
        dialogo.setVisible(true);
        dialogo.dispose();
    }

}
