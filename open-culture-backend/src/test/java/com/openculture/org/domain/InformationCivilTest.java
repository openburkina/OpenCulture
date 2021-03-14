package com.openculture.org.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openculture.org.web.rest.TestUtil;

public class InformationCivilTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationCivil.class);
        InformationCivil informationCivil1 = new InformationCivil();
        informationCivil1.setId(1L);
        InformationCivil informationCivil2 = new InformationCivil();
        informationCivil2.setId(informationCivil1.getId());
        assertThat(informationCivil1).isEqualTo(informationCivil2);
        informationCivil2.setId(2L);
        assertThat(informationCivil1).isNotEqualTo(informationCivil2);
        informationCivil1.setId(null);
        assertThat(informationCivil1).isNotEqualTo(informationCivil2);
    }
}
