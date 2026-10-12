/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.entidad;

/**
 * Entidad que representa un proyecto.
 * En el patrón MVC, pertenece a la capa de modelo (entidades) y encapsula los datos de un registro de la tabla proyectos.
 *
 * @author cmendieta
 */
public class Proyecto {
    Integer id;
    String nombre;

    public Proyecto(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Proyecto() {
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
    
    
}
