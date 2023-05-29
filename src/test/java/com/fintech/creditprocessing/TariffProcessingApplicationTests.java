package com.fintech.creditprocessing;

import com.fintech.creditprocessing.services.TariffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TariffProcessingApplicationTests {
    @Test
    public void areThereThreeTariffs() {
        var tariffs = TariffService.getAll();
        assertEquals(3, tariffs.length);
    }

    @ParameterizedTest(name = "{index} - {0} is interest rate of the first tariff")
    @ValueSource(strings = {"11%", "7%", "8%"})
    public void IsFirstTariffInterestRateEqualsValue(String interestRate) {
        var tariff = TariffService.getFirst();
        assertEquals(interestRate, tariff.getInterestRate());
    }
}
