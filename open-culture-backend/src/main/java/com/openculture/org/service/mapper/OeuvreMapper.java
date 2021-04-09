package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.OeuvreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Oeuvre} and its DTO {@link OeuvreDTO}.
 */
@Mapper(componentModel = "spring", uses = {RegroupementMapper.class, TypeOeuvreMapper.class})
public interface OeuvreMapper extends EntityMapper<OeuvreDTO, Oeuvre> {

    @Mapping(source = "regroupement", target = "regroupementDTO")
    @Mapping(source = "typeOeuvre", target = "typeOeuvreDTO")
    OeuvreDTO toDto(Oeuvre oeuvre);

    @Mapping(target = "artisteOeuvres", ignore = true)
    @Mapping(target = "removeArtisteOeuvre", ignore = true)
    @Mapping(source = "regroupementDTO", target = "regroupement")
    @Mapping(source = "typeOeuvreDTO", target = "typeOeuvre")
    Oeuvre toEntity(OeuvreDTO oeuvreDTO);

    default Oeuvre fromId(Long id) {
        if (id == null) {
            return null;
        }
        Oeuvre oeuvre = new Oeuvre();
        oeuvre.setId(id);
        return oeuvre;
    }
}
