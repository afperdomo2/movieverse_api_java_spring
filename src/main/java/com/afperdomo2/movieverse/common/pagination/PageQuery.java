package com.afperdomo2.movieverse.common.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageQuery {

    @Schema(description = "Número de página (empieza en 1)", example = "1", defaultValue = "1")
    @Min(value = 1, message = "page debe ser mayor o igual a 1")
    private int page = 1;

    @Schema(description = "Cantidad de registros por página", example = "10", defaultValue = "10")
    @Min(value = 1, message = "limit debe ser mayor o igual a 1")
    @Max(value = 100, message = "limit debe ser menor o igual a 100")
    private int limit = 10;
}
