package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArtisteOeuvreMapperTest {

    private ArtisteOeuvreMapper artisteOeuvreMapper;

    @BeforeEach
    public void setUp() {
        artisteOeuvreMapper = new ArtisteOeuvreMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(artisteOeuvreMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(artisteOeuvreMapper.fromId(null)).isNull();
    }
}
