package com.openculture.org.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class TypeOeuvreDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOeuvreDTO.class);
        TypeOeuvreDTO typeOeuvreDTO1 = new TypeOeuvreDTO();
        typeOeuvreDTO1.setId(1L);
        TypeOeuvreDTO typeOeuvreDTO2 = new TypeOeuvreDTO();
        assertThat(typeOeuvreDTO1).isNotEqualTo(typeOeuvreDTO2);
        typeOeuvreDTO2.setId(typeOeuvreDTO1.getId());
        assertThat(typeOeuvreDTO1).isEqualTo(typeOeuvreDTO2);
        typeOeuvreDTO2.setId(2L);
        assertThat(typeOeuvreDTO1).isNotEqualTo(typeOeuvreDTO2);
        typeOeuvreDTO1.setId(null);
        assertThat(typeOeuvreDTO1).isNotEqualTo(typeOeuvreDTO2);
    }
}
