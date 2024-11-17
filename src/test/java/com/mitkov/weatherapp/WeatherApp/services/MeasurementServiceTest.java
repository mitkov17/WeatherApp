package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementsStatisticsDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.*;
import com.mitkov.weatherapp.WeatherApp.repositories.MeasurementRepository;
import com.mitkov.weatherapp.WeatherApp.util.SortParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MeasurementServiceTest {

    @InjectMocks
    private MeasurementService measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addMeasurementTest() {
        Measurement measurement = new Measurement();
        measurement.setMeasurementValue(20.0);

        measurementService.addMeasurement(measurement);

        verify(measurementRepository, times(1)).save(measurement);
    }

    @Test
    public void getRainyDaysTest() {
        Measurement measurement = new Measurement();
        measurement.setMeasurementValue(20.0);
        measurement.setRaining(true);

        when(measurementRepository.findByRainingTrue()).thenReturn(Collections.singletonList(measurement));

        List<Measurement> list = measurementService.getRainyDays();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(20.0, list.get(0).getMeasurementValue());
        assertEquals(true, list.get(0).getRaining());
    }

    @Test
    public void filterMeasurementsTest() {
        Measurement measurement = new Measurement();
        measurement.setMeasurementValue(20.0);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);

        List<Measurement> measurements = Collections.singletonList(measurement);

        when(measurementRepository.findByMeasurementUnitAndMeasurementValueBetween(
                MeasurementUnit.DEGREES_CELSIUS, 10.0, 30.0)).thenReturn(measurements);

        List<Measurement> result = measurementService.filterMeasurements(MeasurementUnit.DEGREES_CELSIUS, 10.0, 30.0);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());

        when(measurementRepository.findByMeasurementUnitAndMeasurementValueAfter(
                MeasurementUnit.DEGREES_CELSIUS, 15.0)).thenReturn(measurements);

        result = measurementService.filterMeasurements(MeasurementUnit.DEGREES_CELSIUS, 15.0, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());

        when(measurementRepository.findByMeasurementUnitAndMeasurementValueBefore(
                MeasurementUnit.DEGREES_CELSIUS, 25.0)).thenReturn(measurements);

        result = measurementService.filterMeasurements(MeasurementUnit.DEGREES_CELSIUS, null, 25.0);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());

        assertThrows(InvalidMeasurementRangeException.class, () ->
                measurementService.filterMeasurements(MeasurementUnit.DEGREES_CELSIUS, 30.0, 10.0));

        assertThrows(MeasurementUnitIsRequiredException.class, () ->
                measurementService.filterMeasurements(null, 10.0, 30.0));

        when(measurementRepository.findByMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS)).thenReturn(measurements);

        result = measurementService.filterMeasurements(MeasurementUnit.DEGREES_CELSIUS, null, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());
    }

    @Test
    public void sortMeasurementsTest() {
        Measurement measurement = new Measurement();
        measurement.setMeasurementValue(20.0);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);

        List<Measurement> measurements = Collections.singletonList(measurement);

        when(measurementRepository.findByMeasurementUnit(
                eq(MeasurementUnit.DEGREES_CELSIUS), any(Sort.class))).thenReturn(measurements);

        List<Measurement> result = measurementService.sortMeasurements(MeasurementUnit.DEGREES_CELSIUS, null, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());

        when(measurementRepository.findByMeasurementUnit(
                eq(MeasurementUnit.DEGREES_CELSIUS), eq(Sort.by(Sort.Direction.DESC, "measurementValue"))))
                .thenReturn(measurements);

        result = measurementService.sortMeasurements(MeasurementUnit.DEGREES_CELSIUS, false, SortParameter.MEASUREMENT_VALUE);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());

        when(measurementRepository.findByMeasurementUnit(
                eq(MeasurementUnit.DEGREES_CELSIUS), eq(Sort.by(Sort.Direction.ASC, "measuredAt"))))
                .thenReturn(measurements);

        result = measurementService.sortMeasurements(MeasurementUnit.DEGREES_CELSIUS, true, SortParameter.MEASUREMENT_TIME);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(20.0, result.get(0).getMeasurementValue());

        assertThrows(MeasurementUnitIsRequiredException.class, () ->
                measurementService.sortMeasurements(null, true, SortParameter.MEASUREMENT_VALUE));
    }

    @Test
    public void getMeasurementStatisticsTest() {
        Measurement measurement1 = new Measurement();
        measurement1.setMeasurementValue(10.0);
        measurement1.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement1.setMeasuredAt(new Date());

        Measurement measurement2 = new Measurement();
        measurement2.setMeasurementValue(20.0);
        measurement2.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement2.setMeasuredAt(new Date());

        List<Measurement> measurements = Arrays.asList(measurement1, measurement2);

        when(measurementRepository.findByMeasurementUnitAndMeasuredAtBetween(
                eq(MeasurementUnit.DEGREES_CELSIUS), any(Date.class), any(Date.class)))
                .thenReturn(measurements);

        List<MeasurementsStatisticsDTO> statistics = measurementService.getMeasurementStatistics(
                MeasurementUnit.DEGREES_CELSIUS, "2024/01/01", "2024/12/31");

        assertNotNull(statistics);
        assertEquals(1, statistics.size());
        assertEquals(15.0, statistics.get(0).getAvgValue());
        assertEquals(10.0, statistics.get(0).getMinValue());
        assertEquals(20.0, statistics.get(0).getMaxValue());
        assertEquals(2, statistics.get(0).getCount());

        when(measurementRepository.findByMeasurementUnitAndMeasuredAtBetween(
                any(MeasurementUnit.class), any(Date.class), any(Date.class)))
                .thenReturn(measurements);

        statistics = measurementService.getMeasurementStatistics(null, "2024/01/01", "2024/12/31");

        assertNotNull(statistics);
        assertEquals(MeasurementUnit.values().length, statistics.size());
    }

    @Test
    public void getMeasurementsByTimeRangeTest() {
        Measurement measurement1 = new Measurement();
        measurement1.setMeasurementValue(10.0);
        measurement1.setMeasuredAt(new Date());

        Measurement measurement2 = new Measurement();
        measurement2.setMeasurementValue(20.0);
        measurement2.setMeasuredAt(new Date());

        List<Measurement> measurements = Arrays.asList(measurement1, measurement2);

        when(measurementRepository.findByMeasuredAtBetween(any(Date.class), any(Date.class))).thenReturn(measurements);

        List<Measurement> result = measurementService.getMeasurementsByTimeRange("2024/01/01", "2024/12/31");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10.0, result.get(0).getMeasurementValue());
        assertEquals(20.0, result.get(1).getMeasurementValue());
    }

    @Test
    public void getMeasurementsByTimeRangeInvalidDateFormatTest() {
        InvalidDateFormatException thrown = assertThrows(
                InvalidDateFormatException.class,
                () -> measurementService.getMeasurementsByTimeRange("2024mistake/11/31", "2024/12/31")
        );

        assertTrue(thrown.getMessage().contains("Invalid date format. Please use 'yyyy/MM/dd'"));
    }

    @Test
    public void getMeasurementsByTimeRangeNullDatesTest() {
        Measurement measurement1 = new Measurement();
        measurement1.setMeasurementValue(15.0);
        measurement1.setMeasuredAt(new Date());

        when(measurementRepository.findByMeasuredAtBetween(null, null)).thenReturn(Collections.singletonList(measurement1));

        List<Measurement> result = measurementService.getMeasurementsByTimeRange(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(15.0, result.get(0).getMeasurementValue());
    }

    @Test
    public void getPaginatedMeasurementsTest() {
        Measurement measurement1 = new Measurement();
        measurement1.setMeasurementValue(10.0);
        Measurement measurement2 = new Measurement();
        measurement2.setMeasurementValue(20.0);

        List<Measurement> measurements = Arrays.asList(measurement1, measurement2);
        Page<Measurement> measurementPage = new PageImpl<>(measurements);

        when(measurementRepository.findAll(any(Pageable.class))).thenReturn(measurementPage);

        Page<Measurement> result = measurementService.getPaginatedMeasurements(0, 5);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(10.0, result.getContent().get(0).getMeasurementValue());
        assertEquals(20.0, result.getContent().get(1).getMeasurementValue());
    }

    @Test
    public void getPaginatedMeasurementsInvalidPageTest() {
        InvalidPageNumberException thrown = assertThrows(
                InvalidPageNumberException.class,
                () -> measurementService.getPaginatedMeasurements(-1, 5)
        );

        assertTrue(thrown.getMessage().contains("Page number must be greater than or equal to 0."));
    }

    @Test
    public void getPaginatedMeasurementsInvalidSizeTest() {
        InvalidPageSizeException thrown = assertThrows(
                InvalidPageSizeException.class,
                () -> measurementService.getPaginatedMeasurements(0, 0)
        );

        assertTrue(thrown.getMessage().contains("Size must be greater than 0 and less than or equal to 100."));
    }
}
