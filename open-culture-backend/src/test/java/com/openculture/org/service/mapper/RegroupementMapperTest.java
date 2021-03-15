package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegroupementMapperTest {

    private RegroupementMapper regroupementMapper;

    @BeforeEach
    public void setUp() {
        regroupementMapper = new RegroupementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(regroupementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(regroupementMapper.fromId(null)).isNull();
    }
}
