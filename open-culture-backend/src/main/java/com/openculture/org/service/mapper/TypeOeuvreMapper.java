package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.TypeOeuvreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOeuvre} and its DTO {@link TypeOeuvreDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeOeuvreMapper extends EntityMapper<TypeOeuvreDTO, TypeOeuvre> {

    @Mapping(target = "oeuvres", ignore = true)
    @Mapping(target = "removeOeuvre", ignore = true)
    TypeOeuvre toEntity(TypeOeuvreDTO typeOeuvreDTO);

    default TypeOeuvre fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeOeuvre typeOeuvre = new TypeOeuvre();
        typeOeuvre.setId(id);
        return typeOeuvre;
    }
}
