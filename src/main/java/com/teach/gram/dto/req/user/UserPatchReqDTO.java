package com.teach.gram.dto.req.user;

public record UserPatchReqDTO(
        String profileLink,
        String name,
        String username,
        String description
) {
}
