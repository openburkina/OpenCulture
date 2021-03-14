package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class RegroupementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegroupementDTO.class);
        RegroupementDTO regroupementDTO1 = new RegroupementDTO();
        regroupementDTO1.setId(1L);
        RegroupementDTO regroupementDTO2 = new RegroupementDTO();
        assertThat(regroupementDTO1).isNotEqualTo(regroupementDTO2);
        regroupementDTO2.setId(regroupementDTO1.getId());
        assertThat(regroupementDTO1).isEqualTo(regroupementDTO2);
        regroupementDTO2.setId(2L);
        assertThat(regroupementDTO1).isNotEqualTo(regroupementDTO2);
        regroupementDTO1.setId(null);
        assertThat(regroupementDTO1).isNotEqualTo(regroupementDTO2);
    }
}
