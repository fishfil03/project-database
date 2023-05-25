package com.fintech.creditprocessing.service;

import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.domain.dto.LoanOrderForAddDTO;
import com.fintech.creditprocessing.domain.dto.LoanOrderForDelDTO;

public interface LoanOrderService {

    String createCreditApplication(LoanOrderForAddDTO loanOrderDTO);

    Status getStatusOrder(String orderId);

    void deleteOrder(LoanOrderForDelDTO loanOrderDTO);
}
