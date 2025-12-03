package com.jeondoh.core.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaticVariables {

    // ========================================================================
    // JWT
    public static final String ROLE_CLAIM_KEY = "authorities";
    public static final String NAME_CLAIM_KEY = "username";
    public static final String AUTH_HEADER_PREFIX_KEY = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";
}
