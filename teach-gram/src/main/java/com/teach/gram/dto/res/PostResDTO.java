package com.teach.gram.dto.res;

public record PostResDTO(
        Long id,
        String titulo,
        String descricao,
        Status status
) {
}
