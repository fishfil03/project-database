package com.fintech.creditprocessing.service.impl;

import com.fintech.creditprocessing.dao.TariffDAO;
import com.fintech.creditprocessing.entity.Tariff;
import com.fintech.creditprocessing.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffDAO tariffDAO;

    @Override
    public List<Tariff> getAllTariffs() {

        Optional<List<Tariff>> optTariffs = Optional.of(tariffDAO.getAllTariffs());
        return optTariffs.get();
    }
}
