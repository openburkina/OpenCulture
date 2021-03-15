package com.openculture.org.service.mapper;


import com.openculture.org.domain.*;
import com.openculture.org.service.dto.AbonnementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Abonnement} and its DTO {@link AbonnementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AbonnementMapper extends EntityMapper<AbonnementDTO, Abonnement> {



    default Abonnement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Abonnement abonnement = new Abonnement();
        abonnement.setId(id);
        return abonnement;
    }
}
