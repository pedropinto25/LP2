package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.Categoria;
import java.sql.SQLException;
import java.util.List;

public interface ICategoriaDAO {
    void addCategoria(Categoria categoria) throws SQLException;
    void updateCategoria(Categoria categoria) throws SQLException;
    void deleteCategoria(int id) throws SQLException;
    Categoria getCategoriaById(int id) throws SQLException;
    List<Categoria> getAllCategorias() throws SQLException;
}