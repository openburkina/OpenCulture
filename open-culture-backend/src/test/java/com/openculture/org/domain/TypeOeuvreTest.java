package com.openculture.org.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class TypeOeuvreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOeuvre.class);
        TypeOeuvre typeOeuvre1 = new TypeOeuvre();
        typeOeuvre1.setId(1L);
        TypeOeuvre typeOeuvre2 = new TypeOeuvre();
        typeOeuvre2.setId(typeOeuvre1.getId());
        assertThat(typeOeuvre1).isEqualTo(typeOeuvre2);
        typeOeuvre2.setId(2L);
        assertThat(typeOeuvre1).isNotEqualTo(typeOeuvre2);
        typeOeuvre1.setId(null);
        assertThat(typeOeuvre1).isNotEqualTo(typeOeuvre2);
    }
}
