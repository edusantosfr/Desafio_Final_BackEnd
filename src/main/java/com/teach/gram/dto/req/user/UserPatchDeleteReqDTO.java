package com.teach.gram.dto.req.user;

public record UserPatchDeleteReqDTO (
        String username,
        Boolean deleted
) {
}
