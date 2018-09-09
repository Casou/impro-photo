package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.DateImproDao;
import com.bparent.improPhoto.domain.DateImpro;
import com.bparent.improPhoto.dto.DateImproDto;
import com.bparent.improPhoto.exception.ImproMappingException;
import com.bparent.improPhoto.exception.ImproServiceException;
import com.bparent.improPhoto.mapper.DateImproMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class DateImproServiceTest {

    @InjectMocks
    private DateImproService dateImproService;

    @Mock
    private DateImproDao dateImproDao;

    @Mock
    private DateImproMapper dateImproMapper;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


    @Before
    public void init() throws ImproMappingException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveDates() throws ImproServiceException, ParseException {
        ArgumentCaptor<List> dateImproCaptor = ArgumentCaptor.forClass(List.class);
        this.dateImproService.prepareDates(Arrays.asList(
                new DateImproDto(BigInteger.valueOf(1), dateFormatter.parse("06/01/2018"), "Prochaine date"),
                new DateImproDto(BigInteger.valueOf(2), dateFormatter.parse("22/05/2018"), "Date d'après")
        ));

        verify(this.dateImproDao).deleteByIdNotIn(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2)));
        verify(this.dateImproDao).save(anyListOf(DateImpro.class));
        verify(this.dateImproMapper).toEntity(eq(DateImpro.class), dateImproCaptor.capture());

        List<DateImproDto> allDates = dateImproCaptor.getValue();
        assertEquals(2, allDates.size());

        assertEquals(dateFormatter.parse("06/01/2018"), allDates.get(0).getDate());
        assertEquals(dateFormatter.parse("22/05/2018"), allDates.get(1).getDate());
    }

}