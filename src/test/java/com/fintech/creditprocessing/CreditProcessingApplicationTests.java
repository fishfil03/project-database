package com.fintech.creditprocessing;

import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.services.CreditService;
import com.fintech.creditprocessing.services.TariffService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CreditProcessingApplicationTests {
    @ParameterizedTest(name = "{index} - {0} is added")
    @ValueSource(longs = {120356894755L, 153356894755L, 120356124755L})
    public void isNewRequestAdded(long userId) {
        CreditService.apply(userId, TariffService.getFirst());
    }

    @ParameterizedTest(name = "{index} - {0} is orderId")
    @ValueSource(strings = {"dedffbb7-8336-49ab-98c8-7ee1f016bd67", "d7b47d91-d346-46aa-b1d8-2f014d2954d4", "ab596b16-44a4-47fe-9c80-44ef65dcab7d"})
    public void isStatusCodeApproved(String orderId) {
        var status = CreditService.getStatusOrder(orderId);
        assertEquals(Status.APPROVED, status);
    }
}
