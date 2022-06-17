package com.jueye.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by soledede.weng on 2016-11-23.
 */
public interface RowMapper<T>{
    T mapRow(ResultSet rs) throws SQLException;
}
