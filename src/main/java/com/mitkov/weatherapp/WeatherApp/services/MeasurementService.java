package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.dto.MeasurementsStatisticsDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.exceptions.*;
import com.mitkov.weatherapp.WeatherApp.repositories.MeasurementRepository;
import com.mitkov.weatherapp.WeatherApp.util.SortParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Transactional
    @PreAuthorize("hasRole('SENSOR')")
    public void addMeasurement(Measurement measurement) {
        measurementRepository.save(measurement);
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public List<Measurement> getRainyDays() {
        return measurementRepository.findByRainingTrue();
    }

    public List<Measurement> filterMeasurements(MeasurementUnit measurementUnit, Double min, Double max) {

        if (measurementUnit == null) {
            throw new MeasurementUnitIsRequiredException("Measurement unit is required!");
        }

        List<Measurement> resultList;

        if (min != null && max != null) {
            if (min > max) {
                throw new InvalidMeasurementRangeException("Min value cannot be greater than Max value");
            }
            resultList = measurementRepository.findByMeasurementUnitAndMeasurementValueBetween(measurementUnit, min, max);
        } else if (min != null) {
            resultList = measurementRepository.findByMeasurementUnitAndMeasurementValueAfter(measurementUnit, min);
        } else if (max != null) {
            resultList = measurementRepository.findByMeasurementUnitAndMeasurementValueBefore(measurementUnit, max);
        } else {
            resultList = measurementRepository.findByMeasurementUnit(measurementUnit);
        }

        return resultList;
    }

    public List<Measurement> sortMeasurements(MeasurementUnit measurementUnit, Boolean ascending, SortParameter sortParam) {
        if (measurementUnit == null) {
            throw new MeasurementUnitIsRequiredException("Measurement unit is required!");
        }

        String sortField = (sortParam != null) ? sortParam.getSortField() : "measurementValue";

        Sort.Direction direction = (ascending != null && ascending) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return measurementRepository.findByMeasurementUnit(measurementUnit, Sort.by(direction, sortField));
    }

    public List<MeasurementsStatisticsDTO> getMeasurementStatistics(MeasurementUnit measurementUnit, String startDate, String endDate) {

        List<MeasurementsStatisticsDTO> statistics = new ArrayList<>();

        if (measurementUnit != null) {
            List<Measurement> list = fetchMeasurementsByUnitAndDateRange(measurementUnit, parseDate(startDate), parseDate(endDate));
            statistics.add(computeStatistics(measurementUnit, list));
        } else {
            for (MeasurementUnit unit : MeasurementUnit.values()) {
                List<Measurement> unitList = fetchMeasurementsByUnitAndDateRange(unit, parseDate(startDate), parseDate(endDate));
                statistics.add(computeStatistics(unit, unitList));
            }
        }

        return statistics;
    }

    private MeasurementsStatisticsDTO computeStatistics(MeasurementUnit unit, List<Measurement> unitList) {
        DoubleSummaryStatistics stats = unitList.stream()
                .mapToDouble(Measurement::getMeasurementValue)
                .summaryStatistics();

        double average = stats.getCount() > 0 ? stats.getAverage() : Double.NaN;
        double min = stats.getCount() > 0 ? stats.getMin() : Double.NaN;
        double max = stats.getCount() > 0 ? stats.getMax() : Double.NaN;
        long count = stats.getCount();

        return new MeasurementsStatisticsDTO(unit, average, min, max, count);
    }

    private List<Measurement> fetchMeasurementsByUnitAndDateRange(MeasurementUnit measurementUnit, Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            return measurementRepository.findByMeasurementUnitAndMeasuredAtBetween(measurementUnit, startDate, endDate);
        } else if (startDate != null) {
            return measurementRepository.findByMeasurementUnitAndMeasuredAtAfter(measurementUnit, startDate);
        } else if (endDate != null) {
            return measurementRepository.findByMeasurementUnitAndMeasuredAtBefore(measurementUnit, endDate);
        } else {
            return measurementRepository.findByMeasurementUnit(measurementUnit);
        }
    }

    public List<Measurement> getMeasurementsByTimeRange(String startDate, String endDate) {
        return measurementRepository.findByMeasuredAtBetween(parseDate(startDate), parseDate(endDate));
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use 'yyyy/MM/dd'");
        }
    }

    public Page<Measurement> getPaginatedMeasurements(int page, int size) {

        if (page < 0) {
            throw new InvalidPageNumberException("Page number must be greater than or equal to 0.");
        }

        if (size <= 0 || size > 100) {
            throw new InvalidPageSizeException("Size must be greater than 0 and less than or equal to 100.");
        }

        Pageable pageable = PageRequest.of(page, size);
        return measurementRepository.findAll(pageable);
    }
}
