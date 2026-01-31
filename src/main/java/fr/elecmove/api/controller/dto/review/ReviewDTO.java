package fr.elecmove.api.controller.dto.review;

import fr.elecmove.api.controller.dto.user.UserSingleDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private String id;
    private String comment;
    private Integer rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSingleDTO user;
}
