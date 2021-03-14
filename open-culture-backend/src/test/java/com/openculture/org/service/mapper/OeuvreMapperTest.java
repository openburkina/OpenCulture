package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OeuvreMapperTest {

    private OeuvreMapper oeuvreMapper;

    @BeforeEach
    public void setUp() {
        oeuvreMapper = new OeuvreMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(oeuvreMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(oeuvreMapper.fromId(null)).isNull();
    }
}
