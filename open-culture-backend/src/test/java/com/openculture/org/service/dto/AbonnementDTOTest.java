package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class AbonnementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbonnementDTO.class);
        AbonnementDTO abonnementDTO1 = new AbonnementDTO();
        abonnementDTO1.setId(1L);
        AbonnementDTO abonnementDTO2 = new AbonnementDTO();
        assertThat(abonnementDTO1).isNotEqualTo(abonnementDTO2);
        abonnementDTO2.setId(abonnementDTO1.getId());
        assertThat(abonnementDTO1).isEqualTo(abonnementDTO2);
        abonnementDTO2.setId(2L);
        assertThat(abonnementDTO1).isNotEqualTo(abonnementDTO2);
        abonnementDTO1.setId(null);
        assertThat(abonnementDTO1).isNotEqualTo(abonnementDTO2);
    }
}
