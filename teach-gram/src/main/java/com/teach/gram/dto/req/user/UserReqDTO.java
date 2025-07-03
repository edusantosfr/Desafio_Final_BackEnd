package com.teach.gram.dto.req.user;

public record UserReqDTO(
        String name,
        String email,
        String username,
        String description,
        String phone,
        String password,
        String profileLink
) {
}
