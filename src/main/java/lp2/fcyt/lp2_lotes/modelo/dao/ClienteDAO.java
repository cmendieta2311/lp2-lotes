package lp2.fcyt.lp2_lotes.modelo.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */


import java.util.List;
import lp2.fcyt.lp2_lotes.modelo.entidad.Cliente;


/**
 * Interfaz DAO para la entidad Cliente.
 * En el patrón MVC, define el contrato de acceso a datos para Cliente dentro de la capa de modelo.
 *
 * @author cmendieta
 */
public interface ClienteDAO {
    List<Cliente> listado(String valor);
    Cliente buscarPorID(int id);
    void insertar(Cliente obj);
    void modificar(Cliente obj);
    void eliminar(Integer id);
    
}
