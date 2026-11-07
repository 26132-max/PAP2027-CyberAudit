package com.cyberaudit.domain;

public record UserAccount(
        Long id,
        String fullName,
        String email,
        String role,
        Long organizationId,
        String organizationName,
        String createdAt) {
}
