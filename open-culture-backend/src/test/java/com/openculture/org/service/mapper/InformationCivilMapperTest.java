package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InformationCivilMapperTest {

    private InformationCivilMapper informationCivilMapper;

    @BeforeEach
    public void setUp() {
        informationCivilMapper = new InformationCivilMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(informationCivilMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(informationCivilMapper.fromId(null)).isNull();
    }
}
