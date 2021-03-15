package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.RegroupementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Regroupement} and its DTO {@link RegroupementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RegroupementMapper extends EntityMapper<RegroupementDTO, Regroupement> {


    @Mapping(target = "oeuvres", ignore = true)
    @Mapping(target = "removeOeuvre", ignore = true)
    Regroupement toEntity(RegroupementDTO regroupementDTO);

    default Regroupement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Regroupement regroupement = new Regroupement();
        regroupement.setId(id);
        return regroupement;
    }
}
