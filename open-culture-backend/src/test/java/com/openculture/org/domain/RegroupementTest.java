package com.openculture.org.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class RegroupementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regroupement.class);
        Regroupement regroupement1 = new Regroupement();
        regroupement1.setId(1L);
        Regroupement regroupement2 = new Regroupement();
        regroupement2.setId(regroupement1.getId());
        assertThat(regroupement1).isEqualTo(regroupement2);
        regroupement2.setId(2L);
        assertThat(regroupement1).isNotEqualTo(regroupement2);
        regroupement1.setId(null);
        assertThat(regroupement1).isNotEqualTo(regroupement2);
    }
}
