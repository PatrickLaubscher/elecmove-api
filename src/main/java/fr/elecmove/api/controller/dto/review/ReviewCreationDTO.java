package fr.elecmove.api.controller.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewCreationDTO {

    @NotNull
    @Size(min = 1, max = 300)
    private String comment;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer rate;
}
