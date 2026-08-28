package com.parking.app.util;

import java.util.UUID;

public final class TokenGeneratorUtil {

    private TokenGeneratorUtil() {
    }

    public static String generateToken() {

        return "PK-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}
