package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UtilsTest {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = Utils.generateCardNumber();
        assertThat("Card number should not be empty", cardNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void validCardNumber(){
        String cardNumber =  Utils.generateCardNumber();
        assertThat("Card number should only include numbers and hyphens", cardNumber, matchesPattern("[\\d-]+"));
    }

    @Test
    public void cvvLength(){
        String cvv = Utils.generateCvv();
        assertThat("CVV should have a length of 3", cvv, hasLength(3));
        assertThat("CVV should be greater than or equal to 0", Integer.parseInt(cvv), is(greaterThan(0)));
    }

    @Test
    public void cvvIsNumeric() {
        String cvv = Utils.generateCvv();
        assertThat("CVV should be a numeric value", cvv, matchesPattern("\\d+"));
    }
}
