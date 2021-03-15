package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.InformationCivilDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InformationCivil} and its DTO {@link InformationCivilDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InformationCivilMapper extends EntityMapper<InformationCivilDTO, InformationCivil> {


    @Mapping(target = "artiste", ignore = true)
    InformationCivil toEntity(InformationCivilDTO informationCivilDTO);

    default InformationCivil fromId(Long id) {
        if (id == null) {
            return null;
        }
        InformationCivil informationCivil = new InformationCivil();
        informationCivil.setId(id);
        return informationCivil;
    }
}
