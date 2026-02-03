package fr.elecmove.api.controller.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PictureDTO {
    private String id;
    private String src;
    private String thumbnail;
    private String alt;
    private boolean main;
}
