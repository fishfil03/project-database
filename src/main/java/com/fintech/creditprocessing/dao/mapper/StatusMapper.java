package com.fintech.creditprocessing.dao.mapper;

import com.fintech.creditprocessing.constant.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMapper implements RowMapper<Status> {

    @Override
    public Status mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Status.valueOf(rs.getString("Status"));
    }
}
