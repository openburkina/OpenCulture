package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class ArtisteOeuvreDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtisteOeuvreDTO.class);
        ArtisteOeuvreDTO artisteOeuvreDTO1 = new ArtisteOeuvreDTO();
        artisteOeuvreDTO1.setId(1L);
        ArtisteOeuvreDTO artisteOeuvreDTO2 = new ArtisteOeuvreDTO();
        assertThat(artisteOeuvreDTO1).isNotEqualTo(artisteOeuvreDTO2);
        artisteOeuvreDTO2.setId(artisteOeuvreDTO1.getId());
        assertThat(artisteOeuvreDTO1).isEqualTo(artisteOeuvreDTO2);
        artisteOeuvreDTO2.setId(2L);
        assertThat(artisteOeuvreDTO1).isNotEqualTo(artisteOeuvreDTO2);
        artisteOeuvreDTO1.setId(null);
        assertThat(artisteOeuvreDTO1).isNotEqualTo(artisteOeuvreDTO2);
    }
}
