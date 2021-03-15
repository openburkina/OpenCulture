package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.ArtisteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Artiste} and its DTO {@link ArtisteDTO}.
 */
@Mapper(componentModel = "spring", uses = {InformationCivilMapper.class})
public interface ArtisteMapper extends EntityMapper<ArtisteDTO, Artiste> {

    @Mapping(source = "informationCivil.id", target = "informationCivilId")
    ArtisteDTO toDto(Artiste artiste);

    @Mapping(source = "informationCivilId", target = "informationCivil")
    @Mapping(target = "artisteOeuvres", ignore = true)
    @Mapping(target = "removeArtisteOeuvre", ignore = true)
    Artiste toEntity(ArtisteDTO artisteDTO);

    default Artiste fromId(Long id) {
        if (id == null) {
            return null;
        }
        Artiste artiste = new Artiste();
        artiste.setId(id);
        return artiste;
    }
}
