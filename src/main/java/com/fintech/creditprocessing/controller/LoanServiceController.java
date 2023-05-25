package com.fintech.creditprocessing.controller;


import com.fintech.creditprocessing.domain.dto.LoanOrderForAddDTO;
import com.fintech.creditprocessing.domain.dto.LoanOrderForDelDTO;
import com.fintech.creditprocessing.domain.response.Response;
import com.fintech.creditprocessing.domain.response.SuccessResponse;
import com.fintech.creditprocessing.domain.response.wrapper.OrderIdWrap;
import com.fintech.creditprocessing.domain.response.wrapper.OrderStatusWrap;
import com.fintech.creditprocessing.domain.response.wrapper.TariffWrap;
import com.fintech.creditprocessing.service.LoanOrderService;
import com.fintech.creditprocessing.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan-service")
@RequiredArgsConstructor
@Slf4j
public class LoanServiceController {

    private final LoanOrderService loanOrderService;
    private final TariffService tariffService;


    @GetMapping("/getTariffs")
    public ResponseEntity<Response> getTariffs() {
        return ResponseEntity.ok(new SuccessResponse<>(new TariffWrap(tariffService.getAllTariffs())));
    }

    @PostMapping("/order")
    public ResponseEntity<Response> addOrder(@RequestBody LoanOrderForAddDTO loanOrder) {
        return ResponseEntity.ok(new SuccessResponse<>(
                new OrderIdWrap(loanOrderService.createCreditApplication(loanOrder))));
    }

    @GetMapping("/getStatusOrder")
    public ResponseEntity<Response> getStatusOrder(@RequestParam("orderId") String orderId) {
        return ResponseEntity.ok(new SuccessResponse<>(new OrderStatusWrap(loanOrderService.getStatusOrder(orderId))));
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<?> deleteOrder(@RequestBody LoanOrderForDelDTO loanOrder) {
        loanOrderService.deleteOrder(loanOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
