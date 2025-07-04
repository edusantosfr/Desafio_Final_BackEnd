package com.teach.gram.dto.res.user;

public record UserResDTO(
        Long id,
        String name,
        String email,
        String username,
        String description,
        String phone,
        String profileLink
) {
}
