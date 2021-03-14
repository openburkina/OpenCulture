package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class ArtisteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtisteDTO.class);
        ArtisteDTO artisteDTO1 = new ArtisteDTO();
        artisteDTO1.setId(1L);
        ArtisteDTO artisteDTO2 = new ArtisteDTO();
        assertThat(artisteDTO1).isNotEqualTo(artisteDTO2);
        artisteDTO2.setId(artisteDTO1.getId());
        assertThat(artisteDTO1).isEqualTo(artisteDTO2);
        artisteDTO2.setId(2L);
        assertThat(artisteDTO1).isNotEqualTo(artisteDTO2);
        artisteDTO1.setId(null);
        assertThat(artisteDTO1).isNotEqualTo(artisteDTO2);
    }
}
