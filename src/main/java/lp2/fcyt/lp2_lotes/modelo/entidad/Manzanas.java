/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.entidad;

/**
 *
 * @author cmendieta
 */
public class Manzanas {
    Integer id;
    Proyecto proyecto;
    String numero;
    String descripcion;

    public Manzanas(Integer id, Proyecto proyecto, String numero, String descripcion) {
        this.id = id;
        this.proyecto = proyecto;
        this.numero = numero;
        this.descripcion = descripcion;
    }

    public Manzanas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
   
}
