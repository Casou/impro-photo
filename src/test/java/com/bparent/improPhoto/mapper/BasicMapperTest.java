package com.bparent.improPhoto.mapper;

import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.exception.ImproMappingException;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class BasicMapperTest {

    @Test
    public void shouldMapDto() throws ImproMappingException {
        BasicMapper<TestDto, TestEntity> mapper = new BasicMapper<>();
        Date d = new Date();
        List<TestDto> allDtos = mapper.toDto(TestDto.class,
                Arrays.asList(
                    new TestEntity(true, 1, 2L, 3.0, "string", d, BigInteger.valueOf(4), CategorieTypeEnum.PHOTO),
                    new TestEntity(false, 10, 20L, 30.0, "string 2", d, BigInteger.valueOf(40), CategorieTypeEnum.POLAROID)
                ));

        assertEquals(2, allDtos.size());

        TestDto dto1 = allDtos.get(0);
        assertTrue(dto1.getBooleanField());
        assertEquals(Integer.valueOf(1), dto1.getIntegerField());
        assertEquals(Long.valueOf(2), dto1.getLongField());
        assertEquals(Double.valueOf(3), dto1.getDoubleField());
        assertEquals("string", dto1.getStringField());
        assertEquals(d, dto1.getDateField());
        assertEquals(BigInteger.valueOf(4), dto1.getBigIntegerField());
        assertEquals(CategorieTypeEnum.PHOTO, dto1.getCategorieTypeEnumField());

        assertNull(dto1.getNonExistingField());

        TestDto dto2 = allDtos.get(1);
        assertFalse(dto2.getBooleanField());
        assertEquals(Integer.valueOf(10), dto2.getIntegerField());
        assertEquals(Long.valueOf(20), dto2.getLongField());
        assertEquals(Double.valueOf(30), dto2.getDoubleField());
        assertEquals("string 2", dto2.getStringField());
        assertEquals(d, dto2.getDateField());
        assertEquals(BigInteger.valueOf(40), dto2.getBigIntegerField());
        assertEquals(CategorieTypeEnum.POLAROID, dto2.getCategorieTypeEnumField());

        assertNull(dto2.getNonExistingField());
    }

    @Test
    public void shouldMapEntity() throws ImproMappingException {
        BasicMapper<TestDto, TestEntity> mapper = new BasicMapper<>();
        Date d = new Date();
        List<TestEntity> allEntities = mapper.toEntity(TestEntity.class,
                Arrays.asList(
                        new TestDto(true, 1, 2L, 3.0, "string", d, BigInteger.valueOf(4), CategorieTypeEnum.PHOTO, "doesn't exist"),
                        new TestDto(false, 10, 20L, 30.0, "string 2", d, BigInteger.valueOf(40), CategorieTypeEnum.POLAROID, "doesn't exist nether")
                ));

        assertEquals(2, allEntities.size());

        TestEntity entity1 = allEntities.get(0);
        assertTrue(entity1.getBooleanField());
        assertEquals(Integer.valueOf(1), entity1.getIntegerField());
        assertEquals(Long.valueOf(2), entity1.getLongField());
        assertEquals(Double.valueOf(3), entity1.getDoubleField());
        assertEquals("string", entity1.getStringField());
        assertEquals(d, entity1.getDateField());
        assertEquals(BigInteger.valueOf(4), entity1.getBigIntegerField());
        assertEquals(CategorieTypeEnum.PHOTO, entity1.getCategorieTypeEnumField());

        TestEntity entity2 = allEntities.get(1);
        assertFalse(entity2.getBooleanField());
        assertEquals(Integer.valueOf(10), entity2.getIntegerField());
        assertEquals(Long.valueOf(20), entity2.getLongField());
        assertEquals(Double.valueOf(30), entity2.getDoubleField());
        assertEquals("string 2", entity2.getStringField());
        assertEquals(d, entity2.getDateField());
        assertEquals(BigInteger.valueOf(40), entity2.getBigIntegerField());
        assertEquals(CategorieTypeEnum.POLAROID, entity2.getCategorieTypeEnumField());
    }

}