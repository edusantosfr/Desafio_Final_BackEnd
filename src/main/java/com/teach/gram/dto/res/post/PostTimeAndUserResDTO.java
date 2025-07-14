package com.teach.gram.dto.res.post;

import com.teach.gram.dto.res.user.UserResDTO;

import java.time.LocalDateTime;

public record PostTimeAndUserResDTO(

        Long id,
        String title,
        String description,
        String photoLink,
        String videoLink,
        Integer likes,
        LocalDateTime createdAt,

        UserResDTO user
) {
}
