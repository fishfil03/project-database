package com.fintech.creditprocessing.domain.response.wrapper;

import com.fintech.creditprocessing.entity.Tariff;

import java.util.List;

public record TariffWrap(List<Tariff> tariffs) {
}