package com.lp2.lp2.DAO.IDAO;

import com.lp2.lp2.Model.Lance;

import java.sql.SQLException;
import java.util.List;

public interface ILanceDAO {
    void addLance(Lance lance) throws SQLException;
    void updateLance(Lance lance) throws SQLException;
    void deleteLance(int id) throws SQLException;
    Lance getLanceById(int id) throws SQLException;
    List<Lance> getAllLances() throws SQLException;
}
