/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */


import java.util.List;
import lp2.fcyt.lp2_lotes.modelo.entidad.Proyecto;

/**
 * Interfaz DAO para la entidad Proyecto.
 * En el patrón MVC, define el contrato de acceso a datos para Proyecto dentro de la capa de modelo.
 *
 * @author cmendieta
 */
public interface ProyectoDAO {
    List<Proyecto> listado(String valor);
    Proyecto buscarPorID(int id);
    void insertar(Proyecto obj);
    void modificar(Proyecto obj);
    void eliminar(Integer id);
    
}
