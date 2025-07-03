package com.teach.gram.dto.res;

public record LoginResDTO(
        String type, //Bearer
        String token,
        Long expiresAt
) {
}
