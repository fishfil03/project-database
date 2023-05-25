package com.fintech.creditprocessing.entity;


import com.fintech.creditprocessing.constant.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tariff {

    Long id;
    Type type;
    String interestRate;
}
