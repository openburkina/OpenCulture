package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArtisteMapperTest {

    private ArtisteMapper artisteMapper;

    @BeforeEach
    public void setUp() {
        artisteMapper = new ArtisteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(artisteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(artisteMapper.fromId(null)).isNull();
    }
}
