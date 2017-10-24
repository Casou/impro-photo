package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproException;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BasicMapperTest {

    @Test
    public void shouldMapDto() throws ImproException {
        BasicMapper<TestDto, TestEntity> mapper = new BasicMapper<>();
        Date d = new Date();
        TestDto dto = mapper.map(TestDto.class, new TestEntity(
                 true, 1, 2L, 3.0, "string", d, BigInteger.valueOf(4), CategorieTypeEnum.PHOTO
        ));

        assertTrue(dto.getBooleanField());
        assertEquals(Integer.valueOf(1), dto.getIntegerField());
        assertEquals(Long.valueOf(2), dto.getLongField());
        assertEquals(Double.valueOf(3), dto.getDoubleField());
        assertEquals("string", dto.getStringField());
        assertEquals(d, dto.getDateField());
        assertEquals(BigInteger.valueOf(4), dto.getBigIntegerField());
        assertEquals(CategorieTypeEnum.PHOTO, dto.getCategorieTypeEnumField());

        assertNull(dto.getNonExistingField());
    }

    @Test
    public void shouldMapEntity() throws ImproException {
        BasicMapper<TestEntity, TestDto> mapper = new BasicMapper<>();
        Date d = new Date();
        TestEntity entity = mapper.map(TestEntity.class, new TestDto(
                 true, 1, 2L, 3.0, "string", d, BigInteger.valueOf(4), CategorieTypeEnum.PHOTO, "doesn't exist"
        ));

        assertTrue(entity.getBooleanField());
        assertEquals(Integer.valueOf(1), entity.getIntegerField());
        assertEquals(Long.valueOf(2), entity.getLongField());
        assertEquals(Double.valueOf(3), entity.getDoubleField());
        assertEquals("string", entity.getStringField());
        assertEquals(d, entity.getDateField());
        assertEquals(BigInteger.valueOf(4), entity.getBigIntegerField());
        assertEquals(CategorieTypeEnum.PHOTO, entity.getCategorieTypeEnumField());
    }

}