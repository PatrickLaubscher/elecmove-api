package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.review.ReviewCreationDTO;
import fr.elecmove.api.controller.dto.review.ReviewDTO;
import fr.elecmove.api.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel= MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy= ReportingPolicy.IGNORE)
public interface ReviewMapper {

    Review toEntity(ReviewCreationDTO dto);
    ReviewDTO toDto(Review Review);

}
