package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class OeuvreDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OeuvreDTO.class);
        OeuvreDTO oeuvreDTO1 = new OeuvreDTO();
        oeuvreDTO1.setId(1L);
        OeuvreDTO oeuvreDTO2 = new OeuvreDTO();
        assertThat(oeuvreDTO1).isNotEqualTo(oeuvreDTO2);
        oeuvreDTO2.setId(oeuvreDTO1.getId());
        assertThat(oeuvreDTO1).isEqualTo(oeuvreDTO2);
        oeuvreDTO2.setId(2L);
        assertThat(oeuvreDTO1).isNotEqualTo(oeuvreDTO2);
        oeuvreDTO1.setId(null);
        assertThat(oeuvreDTO1).isNotEqualTo(oeuvreDTO2);
    }
}
