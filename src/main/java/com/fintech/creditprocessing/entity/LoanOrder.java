package com.fintech.creditprocessing.entity;


import com.fintech.creditprocessing.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanOrder {

    private Long id;
    private String orderId;
    private Long userId;
    private Long tariffId;
    private Double creditRating;
    private Status status;
    private Timestamp timeInsert;
    private Timestamp timeUpdate;
}
