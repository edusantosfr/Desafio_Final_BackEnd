package com.teach.gram.dto.res.login;

import com.teach.gram.dto.res.user.UserResDTO;

public record LoginResDTO(
        String type, //Bearer
        String token,
        Long expiresAt,
        UserResDTO user
) {
}
