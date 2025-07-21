package com.teach.gram.dto.res.post;

import java.time.LocalDateTime;

public record PostTimeResDTO(

        Long id,
        String title,
        String description,
        String photoLink,
        String videoLink,
        Integer likes,
        Boolean privatePost,
        LocalDateTime createdAt
) {
}
