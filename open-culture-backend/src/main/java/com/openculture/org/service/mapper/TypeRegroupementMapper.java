package com.openculture.org.service.mapper;


import com.openculture.org.domain.TypeRegroupement;
import com.openculture.org.service.dto.TypeRegroupementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TypeRegroupement} and its DTO {@link TypeRegroupementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeRegroupementMapper extends EntityMapper<TypeRegroupementDTO, TypeRegroupement> {

    @Mapping(target = "regroupements", ignore = true)
    @Mapping(target = "removeRegroupement", ignore = true)
    TypeRegroupement toEntity(TypeRegroupementDTO typeRegroupementDTO);

    default TypeRegroupement fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeRegroupement typeRegroupement = new TypeRegroupement();
        typeRegroupement.setId(id);
        return typeRegroupement;
    }
}
