package com.laurynas.banktransfer.api.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IbanGeneratorTest {

    private IbanGenerator ibanGenerator = new IbanGenerator();

    @Test
    void generateIban_providedLithuanianCountryCode_returnedIbanIs20Length() {
        String result = ibanGenerator.generateIban("LT");

        assertEquals(20, result.length());
        assertTrue(result.startsWith("LT"));
    }

    @Test
    void generateIban_providedTooLongCountryCode_illegalArgumentExceptionIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> ibanGenerator.generateIban("LTU"));
    }
}
