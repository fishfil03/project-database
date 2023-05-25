package com.fintech.creditprocessing.service.impl;

import com.fintech.creditprocessing.constant.Type;
import com.fintech.creditprocessing.dao.TariffDAO;
import com.fintech.creditprocessing.entity.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TariffServiceImplTest {

    @Mock
    private TariffDAO tariffDAO;

    @InjectMocks
    private TariffServiceImpl tariffService;

    @BeforeEach
    public void setUp() {
        List<Tariff> tariffs = new ArrayList<>();
        tariffs.add(Tariff.builder().id(1L).type(Type.CONSUMER).interestRate("11%").build());
        tariffs.add(Tariff.builder().id(2L).type(Type.MORTGAGE).interestRate("7%").build());
        tariffs.add(Tariff.builder().id(3L).type(Type.INTRABANK).interestRate("8%").build());

        when(tariffDAO.getAllTariffs()).thenReturn(tariffs);
    }

    @Test
    public void testGetAllTariffs() {
        List<Tariff> expectedTariffs = new ArrayList<>();
        expectedTariffs.add(Tariff.builder().id(1L).type(Type.CONSUMER).interestRate("11%").build());
        expectedTariffs.add(Tariff.builder().id(2L).type(Type.MORTGAGE).interestRate("7%").build());
        expectedTariffs.add(Tariff.builder().id(3L).type(Type.INTRABANK).interestRate("8%").build());

        List<Tariff> actualTariffs = tariffService.getAllTariffs();

        assertEquals(expectedTariffs, actualTariffs);
    }
}