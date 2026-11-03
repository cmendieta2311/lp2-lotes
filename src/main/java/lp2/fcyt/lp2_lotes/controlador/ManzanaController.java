/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import lp2.fcyt.lp2_lotes.modelo.dao.CrudDAO;
import lp2.fcyt.lp2_lotes.modelo.dao.ManzanaDAOImpl;
import lp2.fcyt.lp2_lotes.modelo.dao.ProyectoDAOImpl;
import lp2.fcyt.lp2_lotes.modelo.entidad.Manzanas;
import lp2.fcyt.lp2_lotes.modelo.entidad.Proyecto;
import lp2.fcyt.lp2_lotes.modelo.tabla.ManzanaTablaModel;
import lp2.fcyt.lp2_lotes.vista.ManzanaUI;

/**
 * Controlador para la gestión de clientes. En el patrón MVC, actúa como
 * intermediario entre la vista (ManzanasUI) y el modelo (DAO y entidades),
 * gestionando eventos y lógica de negocio.
 *
 * @author cmendieta
 */
public class ManzanaController implements ActionListener {
    
    CrudDAO<Manzanas> dao;
    CrudDAO<Proyecto> daoProyecto;
    ManzanaUI gui;
    ManzanaTablaModel modelo;
    char accion = 'N';
    
    public ManzanaController() {
        this.dao = new ManzanaDAOImpl();
        this.daoProyecto = new ProyectoDAOImpl();
        this.gui = new ManzanaUI(null, false);
        this.modelo = new ManzanaTablaModel();
        // escuchar eventos de los botones

        this.gui.btnGuardar.addActionListener(this);
        this.gui.btnNuevo.addActionListener(this);
        this.gui.btnEditar.addActionListener(this);
        this.gui.btnEliminar.addActionListener(this);
        this.gui.txtBuscar.addActionListener(this);
        
        this.gui.tabla.getSelectionModel().addListSelectionListener(this::getSeleccionFila);
        
        listar("");
        llenarComboProyecto(gui.jcboProyecto);
        this.gui.setVisible(true);
    }

    /**
     * Obtiene y muestra la lista de clientes en la tabla de la vista.
     */
    void listar(String valor) {
        List<Manzanas> listado = dao.listado(valor);
        modelo.setLista(listado);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }

    /**
     * Carga los datos del cliente seleccionado desde la tabla hacia los campos
     * del formulario.
     */
    void getSeleccionFila(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        
        int fila = gui.tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Manzanas valorSeleccionado = modelo.getManzanas(fila);

        gui.txtCodigo.setText(valorSeleccionado.getId().toString());
        gui.txtNumero.setText(valorSeleccionado.getNumero());
        gui.txtDescripcion.setText(valorSeleccionado.getDescripcion());
        // Seleccionar el proyecto por id (Proyecto no implementa equals)
        Proyecto proyecto = valorSeleccionado.getProyecto();
        for (int i = 0; i < gui.jcboProyecto.getItemCount(); i++) {
            Proyecto p = (Proyecto) ((JComboBox) gui.jcboProyecto).getItemAt(i);
            if (proyecto != null && p.getId().equals(proyecto.getId())) {
                gui.jcboProyecto.setSelectedIndex(i);
                break;
            }
        }
    }

    //Recupera los datos del JtextField
    /**
     * Construye y devuelve un objeto Manzanas a partir de los datos ingresados
     * en el formulario.
     */
    Manzanas getManzanasForm() {
        Manzanas manzana = new Manzanas();
        
        if (accion != 'N') {
            manzana.setId(Integer.valueOf(gui.txtCodigo.getText()));
        }
        Proyecto proyecto = (Proyecto) gui.jcboProyecto.getSelectedItem();
        manzana.setProyecto(proyecto);
        manzana.setNumero(gui.txtNumero.getText());
        manzana.setDescripcion(gui.txtDescripcion.getText());
        
        return manzana;
    }

    // Llenar combo
    void llenarComboProyecto(JComboBox cbo) {
        DefaultComboBoxModel<Proyecto> model = new DefaultComboBoxModel<>();
        List<Proyecto> lista = daoProyecto.listado("");
        for (int i = 0; i < lista.size(); i++) {
            Proyecto p = lista.get(i);
            model.addElement(p);
        }
        cbo.setModel(model);
    }

    /**
     * Limpia los campos del formulario de cliente.
     */
    void limpiar() {
        gui.txtCodigo.setText("");
        gui.txtNumero.setText("");
        gui.txtDescripcion.setText("");
    }

    /**
     * Valida que los datos obligatorios del formulario estén completos.
     */
    boolean validarDatos() {
        boolean vacio = false;
        if (gui.txtNumero.getText().trim().isEmpty()) {
            vacio = true;
        }
        if (gui.jcboProyecto.getSelectedItem() == null) {
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
//            gui.txtNombre.requestFocus();
        }
        
        if (e.getSource() == gui.btnEditar) {
            accion = 'M';
//            gui.txtNombre.requestFocus();
        }
        if (e.getSource() == gui.btnCancelar) {
            limpiar();
        }
        
        if (e.getSource() == gui.btnEliminar) {
            accion = 'E';
            
            int fila = gui.tabla.getSelectedRow();
            if (fila >= 0) {
                
                int ok = JOptionPane.showConfirmDialog(gui, "Esta seguro que desea eliminar?", "Atencion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    dao.eliminar(modelo.getManzanas(fila).getId());
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
                dao.insertar(getManzanasForm());
            }
            
            if (accion == 'M') {
                dao.modificar(getManzanasForm());
                
            }
            limpiar();
            listar("");
            gui.tabla.updateUI();
            
        }
    }
    
}
