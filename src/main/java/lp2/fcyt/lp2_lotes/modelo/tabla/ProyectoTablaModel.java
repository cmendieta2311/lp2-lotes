/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.tabla;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import lp2.fcyt.lp2_lotes.modelo.entidad.Proyecto;

/**
 *
 * @author cmendieta
 */
public class ProyectoTablaModel extends AbstractTableModel{
    List<Proyecto> lista;
    
    String[] columnas = {"ID","NOMBRE"};

    public void setLista(List<Proyecto> lista) {
        this.lista = lista;
    }
    
    public Proyecto getProyecto(int fila){
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
        Proyecto proy = lista.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return proy.getId();
            case 1:
                return proy.getNombre();    
            
            default:
                return null;
        }
    }
    
    
}
