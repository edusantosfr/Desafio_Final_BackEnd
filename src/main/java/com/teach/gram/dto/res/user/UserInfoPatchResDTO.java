package com.teach.gram.dto.res.user;

public record UserInfoPatchResDTO(
        String type, //Bearer
        String token,
        Long expiresAt,
        UserResDTO user
) {
}
