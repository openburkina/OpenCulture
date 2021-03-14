package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TypeOeuvreMapperTest {

    private TypeOeuvreMapper typeOeuvreMapper;

    @BeforeEach
    public void setUp() {
        typeOeuvreMapper = new TypeOeuvreMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(typeOeuvreMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(typeOeuvreMapper.fromId(null)).isNull();
    }
}
