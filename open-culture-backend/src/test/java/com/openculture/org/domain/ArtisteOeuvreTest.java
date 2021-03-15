package com.openculture.org.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class ArtisteOeuvreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtisteOeuvre.class);
        ArtisteOeuvre artisteOeuvre1 = new ArtisteOeuvre();
        artisteOeuvre1.setId(1L);
        ArtisteOeuvre artisteOeuvre2 = new ArtisteOeuvre();
        artisteOeuvre2.setId(artisteOeuvre1.getId());
        assertThat(artisteOeuvre1).isEqualTo(artisteOeuvre2);
        artisteOeuvre2.setId(2L);
        assertThat(artisteOeuvre1).isNotEqualTo(artisteOeuvre2);
        artisteOeuvre1.setId(null);
        assertThat(artisteOeuvre1).isNotEqualTo(artisteOeuvre2);
    }
}
