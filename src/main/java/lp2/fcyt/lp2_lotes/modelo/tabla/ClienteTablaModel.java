/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.tabla;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import lp2.fcyt.lp2_lotes.modelo.entidad.Cliente;

/**
 * Modelo de tabla para mostrar clientes en la vista.
 * En el patrón MVC, conecta la capa de modelo (lista de Cliente) con la tabla de la interfaz gráfica.
 *
 * @author cmendieta
 */
public class ClienteTablaModel extends AbstractTableModel {

    List<Cliente> lista;

    String[] columnas = {"ID", "NOMBRE", "APELLIDO", "TELEFONO"};

    public void setLista(List<Cliente> lista) {
        this.lista = lista;
    }

    public Cliente getCliente(int fila) {
        return lista.get(fila);
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cliente proy = lista.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return proy.getId();
            case 1:
                return proy.getNombre();
            case 2:
                return proy.getApellido();
            case 3:
                return proy.getTelefono();

            default:
                return null;
        }
    }

}
