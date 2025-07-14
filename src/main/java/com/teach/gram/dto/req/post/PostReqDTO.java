package com.teach.gram.dto.req.post;

public record PostReqDTO(

        String title,
        String description,
        String photoLink,
        String videoLink,
        Boolean privatePost
) {
}
