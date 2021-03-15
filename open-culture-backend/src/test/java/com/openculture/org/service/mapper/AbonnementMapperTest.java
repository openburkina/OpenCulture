package com.openculture.org.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AbonnementMapperTest {

    private AbonnementMapper abonnementMapper;

    @BeforeEach
    public void setUp() {
        abonnementMapper = new AbonnementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(abonnementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(abonnementMapper.fromId(null)).isNull();
    }
}
