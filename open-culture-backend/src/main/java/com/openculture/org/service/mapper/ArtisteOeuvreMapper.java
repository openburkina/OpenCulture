package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.ArtisteOeuvreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtisteOeuvre} and its DTO {@link ArtisteOeuvreDTO}.
 */
@Mapper(componentModel = "spring", uses = {OeuvreMapper.class, ArtisteMapper.class})
public interface ArtisteOeuvreMapper extends EntityMapper<ArtisteOeuvreDTO, ArtisteOeuvre> {

    @Mapping(source = "oeuvre.id", target = "oeuvreId")
    @Mapping(source = "artiste.id", target = "artisteId")
    ArtisteOeuvreDTO toDto(ArtisteOeuvre artisteOeuvre);

    @Mapping(source = "oeuvreId", target = "oeuvre")
    @Mapping(source = "artisteId", target = "artiste")
    ArtisteOeuvre toEntity(ArtisteOeuvreDTO artisteOeuvreDTO);

    default ArtisteOeuvre fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtisteOeuvre artisteOeuvre = new ArtisteOeuvre();
        artisteOeuvre.setId(id);
        return artisteOeuvre;
    }
}
