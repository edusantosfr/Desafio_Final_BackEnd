package com.teach.gram.dto.req.user;

public record UserInfoPatchReqDTO(
        String name,
        String mail,
        String phone,
        String password
) {
}
