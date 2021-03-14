package com.openculture.org.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class OeuvreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Oeuvre.class);
        Oeuvre oeuvre1 = new Oeuvre();
        oeuvre1.setId(1L);
        Oeuvre oeuvre2 = new Oeuvre();
        oeuvre2.setId(oeuvre1.getId());
        assertThat(oeuvre1).isEqualTo(oeuvre2);
        oeuvre2.setId(2L);
        assertThat(oeuvre1).isNotEqualTo(oeuvre2);
        oeuvre1.setId(null);
        assertThat(oeuvre1).isNotEqualTo(oeuvre2);
    }
}
