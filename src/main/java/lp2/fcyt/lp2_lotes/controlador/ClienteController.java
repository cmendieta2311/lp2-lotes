/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import lp2.fcyt.lp2_lotes.modelo.dao.ClienteDAO;
import lp2.fcyt.lp2_lotes.modelo.entidad.Cliente;
import lp2.fcyt.lp2_lotes.modelo.dao.ClienteDAOImpl;
import lp2.fcyt.lp2_lotes.modelo.tabla.ClienteTablaModel;
import lp2.fcyt.lp2_lotes.vista.ClienteUI;

/**
 * Controlador para la gestión de clientes.
 * En el patrón MVC, actúa como intermediario entre la vista (ClienteUI) y el modelo (DAO y entidades), gestionando eventos y lógica de negocio.
 *
 * @author cmendieta
 */
public class ClienteController implements ActionListener {

    ClienteDAO dao;
    ClienteUI gui;
    ClienteTablaModel modelo;
    char accion = 'N';

    public ClienteController() {
        this.dao = new ClienteDAOImpl();
        this.gui = new ClienteUI(null, true);
        this.modelo = new ClienteTablaModel();
        // escuchar eventos de los botones

        this.gui.btnGuardar.addActionListener(this);
        this.gui.btnNuevo.addActionListener(this);
        this.gui.btnEditar.addActionListener(this);
        this.gui.btnEliminar.addActionListener(this);
        this.gui.txtBuscar.addActionListener(this);

        this.gui.tabla.getSelectionModel().addListSelectionListener(this::getSeleccionFila);

        listar("");
        this.gui.setVisible(true);
    }

    /**
     * Obtiene y muestra la lista de clientes en la tabla de la vista.
     */
    void listar(String valor) {
        List<Cliente> listado = dao.listado(valor);
        modelo.setLista(listado);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }

    /**
     * Carga los datos del cliente seleccionado desde la tabla hacia los campos del formulario.
     */
    void getSeleccionFila(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int fila = gui.tabla.getSelectedRow();
        Cliente valorSeleccionado = modelo.getCliente(fila);

        gui.txtCodigo.setText(valorSeleccionado.getId().toString());
        gui.txtNombre.setText(valorSeleccionado.getNombre());
        gui.txtApellido.setText(valorSeleccionado.getApellido());
        gui.txtNroDocumento.setText(valorSeleccionado.getNumeroDocumento());
        gui.txtTelefono.setText(valorSeleccionado.getTelefono());
        gui.txtEmail.setText(valorSeleccionado.getEmail());
        gui.txtDireccion.setText(valorSeleccionado.getDireccion());
        //gui.txtEmail.setText(valorSeleccionado.getEmail());

    }

    //Recupera los datos del JtextField
    /**
     * Construye y devuelve un objeto Cliente a partir de los datos ingresados en el formulario.
     */
    Cliente getClienteForm() {
        Cliente cliente = new Cliente();
        if (accion != 'N') {
            cliente.setId(Integer.valueOf(gui.txtCodigo.getText()));
        }
        cliente.setNombre(gui.txtNombre.getText());
        cliente.setApellido(gui.txtApellido.getText());
        cliente.setTipoDocumento(gui.jcboTipoDocumento.getSelectedItem().toString());
        cliente.setNumeroDocumento(gui.txtNroDocumento.getText());
        cliente.setEmail(gui.txtEmail.getText());
        cliente.setDireccion(gui.txtDireccion.getText());
        cliente.setTelefono(gui.txtTelefono.getText());
        return cliente;
    }

    /**
     * Limpia los campos del formulario de cliente.
     */
    void limpiar() {
        gui.txtCodigo.setText("");
        gui.txtNombre.setText("");
        gui.txtApellido.setText("");
        gui.txtDireccion.setText("");
        gui.txtEmail.setText("");
        gui.txtTelefono.setText("");
        gui.txtNroDocumento.setText("");
    }

    /**
     * Valida que los datos obligatorios del formulario estén completos.
     */
    boolean validarDatos() {
        boolean vacio = false;
        if (gui.txtNombre.getText().isEmpty()) {
            vacio = true;
        }
        if (gui.txtApellido.getText().isEmpty()) {
            vacio = true;
        }
        if (gui.txtNroDocumento.getText().isEmpty()) {
            vacio = true;
        }
        return vacio;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == gui.txtBuscar) {
            listar(gui.txtBuscar.getText().trim());

        }

        if (e.getSource() == gui.btnNuevo) {
            accion = 'N';
            limpiar();
            gui.txtNombre.requestFocus();
        }

        if (e.getSource() == gui.btnEditar) {
            accion = 'M';
            gui.txtNombre.requestFocus();
        }
        if (e.getSource() == gui.btnCancelar) {
            limpiar();
        }

        if (e.getSource() == gui.btnEliminar) {
            accion = 'E';

            int fila = gui.tabla.getSelectedRow();
            if (fila > 0) {

                int ok = JOptionPane.showConfirmDialog(gui, "Esta seguro que desea eliminar?", "Atencion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    dao.eliminar(modelo.getCliente(fila).getId());
                    listar("");
                    gui.tabla.updateUI();
                }

            }

        }

        if (e.getSource() == gui.btnGuardar) {
            boolean v_validar = validarDatos();
            if (v_validar) {
                JOptionPane.showMessageDialog(gui, "Favor de completar los datos obligatorio");
                return;
            }

            if (accion == 'N') {
                dao.insertar(getClienteForm());
            }

            if (accion == 'M') {
                dao.modificar(getClienteForm());

            }
            limpiar();
            listar("");
            gui.tabla.updateUI();

        }
    }

}
