package com.teach.gram.dto.req.post;

public record PostPatchReqDTO(

        String title,
        String description,
        String photoLink,
        String videoLink
){
}
