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
import lp2.fcyt.lp2_lotes.modelo.dao.CrudDAO;
import lp2.fcyt.lp2_lotes.modelo.entidad.Proyecto;
import lp2.fcyt.lp2_lotes.modelo.dao.ProyectoDAOImpl;
import lp2.fcyt.lp2_lotes.modelo.tabla.ProyectoTablaModel;
import lp2.fcyt.lp2_lotes.vista.ProyectoUI;

/**
 * Controlador para la gestión de proyectos.
 * En el patrón MVC, actúa como intermediario entre la vista (ProyectoUI) y el modelo (DAO y entidades), gestionando eventos y lógica de negocio.
 *
 * @author cmendieta
 */
public class ProyectoController implements ActionListener {

    CrudDAO<Proyecto> dao;
    ProyectoUI gui;
    ProyectoTablaModel modelo;
    char accion = 'N';

    public ProyectoController() {
        this.dao = new ProyectoDAOImpl();
        this.gui = new ProyectoUI(null, true);
        this.modelo = new ProyectoTablaModel();
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
     * Obtiene y muestra la lista de proyectos en la tabla de la vista.
     */
    void listar(String valor) {
        List<Proyecto> listado = dao.listado(valor);
        modelo.setLista(listado);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }

    /**
     * Carga los datos del proyecto seleccionado desde la tabla hacia los campos del formulario.
     */
    void getSeleccionFila(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        int fila = gui.tabla.getSelectedRow();
        Proyecto valorSeleccionado = modelo.getProyecto(fila);

        gui.txtCodigo.setText(valorSeleccionado.getId().toString());
        gui.txtNombre.setText(valorSeleccionado.getNombre());
    }

    /**
     * Construye y devuelve un objeto Proyecto a partir de los datos ingresados en el formulario.
     */
    Proyecto getProyectoForm() {
        Proyecto proyecto = new Proyecto();
        if(accion != 'N'){
            proyecto.setId(Integer.valueOf(gui.txtCodigo.getText()));
        }
        
        proyecto.setNombre(gui.txtNombre.getText());
        return proyecto;
    }
    
    /**
     * Limpia los campos del formulario de proyecto.
     */
    void limpiar(){
        gui.txtCodigo.setText("");
        gui.txtNombre.setText("");
    }
    
    /**
     * Valida que los datos obligatorios del formulario estén completos.
     */
    boolean validarDatos(){
        boolean vacio = false;
        if(gui.txtNombre.getText().isEmpty()){
            vacio= true;
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
                    dao.eliminar(modelo.getProyecto(fila).getId());
                    listar("");
                    gui.tabla.updateUI();
                }

            }

        }

        if (e.getSource() == gui.btnGuardar) {
            boolean v_validar = validarDatos();
            if(v_validar){
                JOptionPane.showMessageDialog(gui, "Favor de completar los datos obligatorio");
                return;
            }
            
            if (accion == 'N') {
                dao.insertar(getProyectoForm());
            }

            if (accion == 'M') {
                dao.modificar(getProyectoForm());

            }
            limpiar();
            listar("");
            gui.tabla.updateUI();

        }
    }

}
