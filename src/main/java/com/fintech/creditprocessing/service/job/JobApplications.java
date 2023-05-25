package com.fintech.creditprocessing.service.job;


import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.dao.LoanOrderDAO;
import com.fintech.creditprocessing.entity.LoanOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class JobApplications {

    private final LoanOrderDAO loanOrderDAO;

    @Scheduled(fixedDelay = 120_000)
    public void ApplicationReview() throws InterruptedException {

        log.info(String.valueOf(System.currentTimeMillis()));

        List<LoanOrder> orders = loanOrderDAO.getOrdersInProgress();
        if (orders.isEmpty()) {
            log.info("There are no applications in the \"IN_PROGRESS\" status");
            return;
        }
        Random random = new Random();

        for (LoanOrder order : orders) {
            if (random.nextBoolean()) order.setStatus(Status.APPROVED);
            else order.setStatus(Status.REFUSED);
            order.setTimeUpdate(new Timestamp(System.currentTimeMillis()));
        }

        loanOrderDAO.updateOrders(orders);
    }

}
