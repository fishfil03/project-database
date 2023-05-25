package com.fintech.creditprocessing.service.impl;

import com.fintech.creditprocessing.constant.ErrorCode;
import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.dao.LoanOrderDAO;
import com.fintech.creditprocessing.dao.TariffDAO;
import com.fintech.creditprocessing.domain.dto.LoanOrderForAddDTO;
import com.fintech.creditprocessing.domain.dto.LoanOrderForDelDTO;
import com.fintech.creditprocessing.domain.exception.CommonException;
import com.fintech.creditprocessing.entity.LoanOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanOrderServiceImplTest {

    @Mock
    private LoanOrderDAO loanOrderDAO;
    @Mock
    private TariffDAO tariffDAO;
    @InjectMocks
    private LoanOrderServiceImpl loanOrderService;

    @Test
    public void testGetStatusOrder() {

        String orderId = UUID.randomUUID().toString();
        Status status = Status.APPROVED;

        when(loanOrderDAO.getStatusById(orderId)).thenReturn(Status.APPROVED);

        assertEquals(status, loanOrderService.getStatusOrder(orderId));
    }

    @Test
    public void testGetStatusOrderNotFound() {

        String orderId = UUID.randomUUID().toString();

        when(loanOrderDAO.getStatusById(orderId)).thenReturn(null);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.getStatusOrder(orderId));

        try {
            loanOrderService.getStatusOrder(orderId);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.ORDER_NOT_FOUND, exp.getCode());
        }
    }

    @Test
    public void testDeleteOrder() {

        String orderId = UUID.randomUUID().toString();
        LoanOrderForDelDTO loanOrderDTO = new LoanOrderForDelDTO(1L, orderId);

        when(loanOrderDAO.getStatusById(orderId)).thenReturn(Status.IN_PROGRESS);

        loanOrderService.deleteOrder(loanOrderDTO);

        verify(loanOrderDAO, times(1)).delete(orderId);
    }

    @Test
    public void testDeleteOrderNotFound() {

        String orderId = UUID.randomUUID().toString();
        LoanOrderForDelDTO loanOrderDTO = new LoanOrderForDelDTO(1L, orderId);

        when(loanOrderDAO.getStatusById(orderId)).thenReturn(null);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.deleteOrder(loanOrderDTO));

        try {
            loanOrderService.deleteOrder(loanOrderDTO);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.ORDER_NOT_FOUND, exp.getCode());
        }
    }

    @Test
    public void testDeleteOrderImpossibleToDelete() {

        String orderId = UUID.randomUUID().toString();
        LoanOrderForDelDTO loanOrderDTO = new LoanOrderForDelDTO(1L, orderId);

        when(loanOrderDAO.getStatusById(orderId)).thenReturn(Status.APPROVED);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.deleteOrder(loanOrderDTO));

        try {
            loanOrderService.deleteOrder(loanOrderDTO);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.ORDER_IMPOSSIBLE_TO_DELETE, exp.getCode());
        }
    }

    @Test
    void testCreateCreditApplication() {

        LoanOrderForAddDTO orderDTO = new LoanOrderForAddDTO(1L, 3L);
        List<LoanOrder> orders = new ArrayList<>();
        orders.add(LoanOrder.builder()
                .tariffId(3L)
                .status(Status.REFUSED)
                .timeUpdate(Timestamp.valueOf("2010-01-01 00:00:00"))
                .build());

        when(tariffDAO.isTariffExists(orderDTO.tariffId())).thenReturn(Boolean.TRUE);
        when(loanOrderDAO.getLoanOrdersByUserId(orderDTO.userId())).thenReturn(orders);

        loanOrderService.createCreditApplication(orderDTO);

        verify(loanOrderDAO, times(1)).save(Mockito.any());
    }

    @Test
    void testCreateCreditApplicationTariffNotFound() {

        LoanOrderForAddDTO orderDTO = new LoanOrderForAddDTO(1L, 4L);

        when(tariffDAO.isTariffExists(orderDTO.tariffId())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.createCreditApplication(orderDTO));

        try {
            loanOrderService.createCreditApplication(orderDTO);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.TARIFF_NOT_FOUND, exp.getCode());
        }
    }

    @Test
    void testCreateCreditApplicationTariffLoanConsideration() {

        LoanOrderForAddDTO orderDTO = new LoanOrderForAddDTO(1L, 3L);
        List<LoanOrder> orders = new ArrayList<>();
        orders.add(LoanOrder.builder().tariffId(3L).status(Status.IN_PROGRESS).build());

        when(tariffDAO.isTariffExists(orderDTO.tariffId())).thenReturn(Boolean.TRUE);
        when(loanOrderDAO.getLoanOrdersByUserId(orderDTO.userId())).thenReturn(orders);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.createCreditApplication(orderDTO));

        try {
            loanOrderService.createCreditApplication(orderDTO);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.LOAN_CONSIDERATION, exp.getCode());
        }
    }

    @Test
    void testCreateCreditApplicationLoanAlreadyApproved() {

        LoanOrderForAddDTO orderDTO = new LoanOrderForAddDTO(1L, 3L);
        List<LoanOrder> orders = new ArrayList<>();
        orders.add(LoanOrder.builder().tariffId(3L).status(Status.APPROVED).build());

        when(tariffDAO.isTariffExists(orderDTO.tariffId())).thenReturn(Boolean.TRUE);
        when(loanOrderDAO.getLoanOrdersByUserId(orderDTO.userId())).thenReturn(orders);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.createCreditApplication(orderDTO));

        try {
            loanOrderService.createCreditApplication(orderDTO);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.LOAN_ALREADY_APPROVED, exp.getCode());
        }
    }

    @Test
    void testCreateCreditApplicationTryLater() {

        LoanOrderForAddDTO orderDTO = new LoanOrderForAddDTO(1L, 3L);
        List<LoanOrder> orders = new ArrayList<>();
        orders.add(LoanOrder.builder()
                .tariffId(3L)
                .status(Status.REFUSED)
                .timeUpdate(new Timestamp(System.currentTimeMillis()))
                .build());

        when(tariffDAO.isTariffExists(orderDTO.tariffId())).thenReturn(Boolean.TRUE);
        when(loanOrderDAO.getLoanOrdersByUserId(orderDTO.userId())).thenReturn(orders);

        Assertions.assertThrows(CommonException.class, () -> loanOrderService.createCreditApplication(orderDTO));

        try {
            loanOrderService.createCreditApplication(orderDTO);
        } catch (CommonException exp) {
            assertEquals(ErrorCode.TRY_LATER, exp.getCode());
        }
    }
}