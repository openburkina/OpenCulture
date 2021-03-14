package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class InformationCivilDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationCivilDTO.class);
        InformationCivilDTO informationCivilDTO1 = new InformationCivilDTO();
        informationCivilDTO1.setId(1L);
        InformationCivilDTO informationCivilDTO2 = new InformationCivilDTO();
        assertThat(informationCivilDTO1).isNotEqualTo(informationCivilDTO2);
        informationCivilDTO2.setId(informationCivilDTO1.getId());
        assertThat(informationCivilDTO1).isEqualTo(informationCivilDTO2);
        informationCivilDTO2.setId(2L);
        assertThat(informationCivilDTO1).isNotEqualTo(informationCivilDTO2);
        informationCivilDTO1.setId(null);
        assertThat(informationCivilDTO1).isNotEqualTo(informationCivilDTO2);
    }
}
