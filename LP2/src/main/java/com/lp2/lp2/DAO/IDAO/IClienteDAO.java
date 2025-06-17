package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface IClienteDAO {
    void addCliente(Cliente cliente) throws SQLException;
    void updateCliente(Cliente cliente) throws SQLException;
    void deleteCliente(int id) throws SQLException;

    void AproveCliente(int id) throws SQLException;

    Cliente getClienteById(int id) throws SQLException;
    List<Cliente> getAllClientes() throws SQLException;

    List<Cliente> getAllClientesToAprove() throws SQLException;

    void inserirClientes(List<Cliente> clientes) throws SQLException;
}