package com.jeondoh.core.common.dto;

public record JwtToken(
        String memberId,
        String username,
        String role
) {
    public static JwtToken of(String memberId, String username, String role) {
        return new JwtToken(memberId, username, role);
    }

}
