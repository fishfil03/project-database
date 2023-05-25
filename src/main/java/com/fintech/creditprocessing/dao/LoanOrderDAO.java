package com.fintech.creditprocessing.dao;

import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.dao.mapper.StatusMapper;
import com.fintech.creditprocessing.entity.LoanOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanOrderDAO {

    private static final String
            SQL_INSERT = "INSERT INTO loan_order(order_id, user_id, tariff_id," +
            " credit_rating, status, time_insert, time_update) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_ORDERS_BY_ID = "SELECT * FROM loan_order WHERE user_id=?";
    private static final String SQL_GET_STATUS_BY_ID = "SELECT status FROM loan_order WHERE order_id=?";
    private static final String SQL_DELETE_ORDER_BY_ID = "DELETE FROM loan_order WHERE order_id=?";
    private static final String SQL_GET_ORDERS_IN_PROGRESS = "SELECT * FROM loan_order WHERE status='IN_PROGRESS'";
    private static final String SQL_UPDATE_ORDERS = "UPDATE loan_order SET status=?, time_update=? WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    public List<LoanOrder> getLoanOrdersByUserId(Long userId) {
        return jdbcTemplate.query(SQL_GET_ORDERS_BY_ID, new BeanPropertyRowMapper<>(LoanOrder.class), userId);
    }

    public void save(LoanOrder loanOrder) {
        jdbcTemplate.update(SQL_INSERT,
                loanOrder.getOrderId(), loanOrder.getUserId(), loanOrder.getTariffId(), loanOrder.getCreditRating(),
                loanOrder.getStatus().toString(), loanOrder.getTimeInsert(), loanOrder.getTimeUpdate());
    }

    public Status getStatusById(String orderId) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_STATUS_BY_ID, new StatusMapper(), orderId);
        }
        catch (EmptyResultDataAccessException e) {
            log.error(MessageFormat.format( "The order {0} is not in the database ({1})", orderId, e.getClass()));
            return null;
        }
    }

    public void delete(String orderId) {
        jdbcTemplate.update(SQL_DELETE_ORDER_BY_ID, orderId);
    }

    public List<LoanOrder> getOrdersInProgress() {
        return jdbcTemplate.query(SQL_GET_ORDERS_IN_PROGRESS, new BeanPropertyRowMapper<>(LoanOrder.class));
    }

    //@Transactional
    public void updateOrders(List<LoanOrder> loanOrders) {

        List<Object[]> batchArgs = new ArrayList<>();
        loanOrders.forEach(order -> batchArgs.add(new Object[]{order.getStatus().toString(), order.getTimeUpdate(), order.getId()}));

        jdbcTemplate.batchUpdate(SQL_UPDATE_ORDERS, batchArgs);
    }

}
