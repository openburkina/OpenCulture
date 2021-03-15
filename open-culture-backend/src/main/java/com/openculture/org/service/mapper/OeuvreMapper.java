package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.OeuvreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Oeuvre} and its DTO {@link OeuvreDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeOeuvreMapper.class, RegroupementMapper.class})
public interface OeuvreMapper extends EntityMapper<OeuvreDTO, Oeuvre> {

    @Mapping(source = "typeOeuvre.id", target = "typeOeuvreId")
    @Mapping(source = "regroupement.id", target = "regroupementId")
    OeuvreDTO toDto(Oeuvre oeuvre);

    @Mapping(source = "typeOeuvreId", target = "typeOeuvre")
    @Mapping(target = "artisteOeuvres", ignore = true)
    @Mapping(target = "removeArtisteOeuvre", ignore = true)
    @Mapping(source = "regroupementId", target = "regroupement")
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