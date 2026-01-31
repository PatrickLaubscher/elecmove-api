package fr.elecmove.api.controller.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewCreationDTO {

    @NotNull
    @Size(min = 1, max = 300)
    private String comment;
    @NotNull
    @Min(0)
    @Max(5)
    private Integer rate;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
