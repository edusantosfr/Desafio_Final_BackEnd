package com.teach.gram.dto.res;

public record UserResDTO(
        String name,
        String email,
        String username,
        String description,
        String phone,
        String profileLink
) {
}
