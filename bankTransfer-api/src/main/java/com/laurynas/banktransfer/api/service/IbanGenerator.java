package com.laurynas.banktransfer.api.service;

import javax.inject.Singleton;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * It is just random number generator
 */
@Singleton
public class IbanGenerator {

    public String generateIban(String countryCode) {
        if (countryCode.length() != 2) {
            throw new IllegalArgumentException("Country code for IBAN generation should consist of 2 symbols");
        }

        String number = IntStream.rangeClosed(0, 17)
                .map(operand -> new Random().nextInt(10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
        return countryCode + number;
    }
}