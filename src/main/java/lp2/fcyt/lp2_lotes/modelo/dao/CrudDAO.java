/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lp2.fcyt.lp2_lotes.modelo.dao;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD.
 * En el patrón MVC, define el contrato común que deben cumplir los DAOs de la capa de modelo para acceder y manipular datos.
 *
 * @author cmendieta
 */
public interface CrudDAO<T> {
    List<T> listado(String valor);
    T buscarPorID(int id);
    void insertar(T obj);
    void modificar(T obj);
    void eliminar(Integer id);
}
