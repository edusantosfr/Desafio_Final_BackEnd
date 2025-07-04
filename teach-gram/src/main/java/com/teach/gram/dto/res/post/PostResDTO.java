package com.teach.gram.dto.res.post;

public record PostResDTO(

        Long id,
        String title,
        String description,
        String photoLink,
        String videoLink
) {
}
