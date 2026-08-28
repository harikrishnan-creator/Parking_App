package com.parking.app.constants;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String JWT_HEADER =
            "Authorization";

    public static final String JWT_PREFIX =
            "Bearer ";

    public static final long JWT_EXPIRATION =
            86400000L;

    public static final String ROLE_ADMIN =
            "ROLE_ADMIN";

    public static final String ROLE_OPERATOR =
            "ROLE_OPERATOR";

    public static final String AUTH_API =
            "/api/auth/**";
}
