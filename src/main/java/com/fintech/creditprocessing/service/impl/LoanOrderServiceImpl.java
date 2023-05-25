package com.fintech.creditprocessing.service.impl;


import com.fintech.creditprocessing.constant.ErrorCode;
import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.dao.LoanOrderDAO;
import com.fintech.creditprocessing.dao.TariffDAO;
import com.fintech.creditprocessing.domain.dto.LoanOrderForAddDTO;
import com.fintech.creditprocessing.domain.dto.LoanOrderForDelDTO;
import com.fintech.creditprocessing.domain.exception.CommonException;
import com.fintech.creditprocessing.entity.LoanOrder;
import com.fintech.creditprocessing.service.LoanOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LoanOrderServiceImpl implements LoanOrderService {

    private final LoanOrderDAO loanOrderDAO;
    private final TariffDAO tariffDAO;

    @Override
    public String createCreditApplication(LoanOrderForAddDTO loanOrderDTO) {

        if (!tariffDAO.isTariffExists(loanOrderDTO.tariffId()))
            throw new CommonException(ErrorCode.TARIFF_NOT_FOUND, "Тариф не найден");

        List<LoanOrder> orders = loanOrderDAO.getLoanOrdersByUserId(loanOrderDTO.userId());

        for (LoanOrder order : orders) {
            if (order.getTariffId().equals(loanOrderDTO.tariffId())) {

                switch (order.getStatus()) {
                    case IN_PROGRESS -> throw new CommonException(ErrorCode.LOAN_CONSIDERATION, "Заявка уже на рассмотрении");
                    case APPROVED -> throw new CommonException(ErrorCode.LOAN_ALREADY_APPROVED, "Заявка уже одобрена");
                    case REFUSED -> {
                        if (new Timestamp(System.currentTimeMillis()).getTime() - order.getTimeUpdate().getTime()  < 120000) {
                            throw new CommonException(ErrorCode.TRY_LATER, "Прошло менее 2 минут");
                        }
                    }
                }
            }
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        LoanOrder loanOrder = LoanOrder.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(loanOrderDTO.userId())
                .tariffId(loanOrderDTO.tariffId())
                .creditRating(0.1 + Math.random() * 0.8)
                .status(Status.IN_PROGRESS)
                .timeInsert(timestamp)
                .timeUpdate(timestamp)
                .build();

        loanOrderDAO.save(loanOrder);

        return loanOrder.getOrderId();
    }

    @Override
    public Status getStatusOrder(String orderId) {

        Optional<Status> statusOptional = Optional.ofNullable(loanOrderDAO.getStatusById(orderId));
        if (statusOptional.isEmpty()) throw new CommonException(ErrorCode.ORDER_NOT_FOUND, "Заявка не найдена");

        return statusOptional.get();
    }

    @Override
    public void deleteOrder(LoanOrderForDelDTO loanOrderDTO) {

        Optional<Status> statusOptional = Optional.ofNullable(loanOrderDAO.getStatusById(loanOrderDTO.orderId()));
        if (statusOptional.isEmpty()) throw new CommonException(ErrorCode.ORDER_NOT_FOUND, "Заявка не найдена");

        if (!statusOptional.get().equals(Status.IN_PROGRESS))
            throw new CommonException(ErrorCode.ORDER_IMPOSSIBLE_TO_DELETE, "Невозможно удалить заявку");

        loanOrderDAO.delete(loanOrderDTO.orderId());
    }
}
