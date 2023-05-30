package com.fintech.creditprocessing;

import com.fintech.creditprocessing.constant.Status;
import com.fintech.creditprocessing.services.CreditService;
import com.fintech.creditprocessing.services.TariffService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class CreditProcessingApplicationTests {
    @ParameterizedTest(name = "{index} - {0} is added")
    @ValueSource(longs = {120364894755L, 153866894755L, 123156124755L})
    public void isNewRequestAdded(long userId) {
        assertNotEquals(0, CreditService.apply(userId, TariffService.getFirst()));
    }

    @ParameterizedTest(name = "{index} - {0} is orderId")
    @ValueSource(strings = {"d7b47d91-d346-46aa-b1d8-2f014d2954d4", "d8d59f39-da4a-4b39-bba7-2c1c44dfb114", "ab596b16-44a4-47fe-9c80-44ef65dcab7d"})
    public void isStatusCodeApproved(String orderId) {
        var status = CreditService.getStatusOrder(orderId);
        assertEquals(Status.APPROVED, status);
    }
}
