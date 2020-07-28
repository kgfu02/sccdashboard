package kfu.coviddashboard.coviddashboardspring.controller;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kfu.coviddashboard.coviddashboardspring.model.DayComparatorNameTime;
import kfu.coviddashboard.coviddashboardspring.model.DayComparatorTime;
import kfu.coviddashboard.coviddashboardspring.model.KeyMetric;
import kfu.coviddashboard.coviddashboardspring.repository.KeyMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kfu.coviddashboard.coviddashboardspring.model.Day;
import kfu.coviddashboard.coviddashboardspring.repository.DayRepository;

@RestController @CrossOrigin(origins = "http://localhost:4200")
public class KeyMetricController {
    @Autowired
    private KeyMetricRepository keyMetricRepository;

    @GetMapping("/keyMetric/{type}") // totalCases totalDeaths etc
    public List<KeyMetric> getRecent(@PathVariable(value = "type") String type) {
        return keyMetricRepository.getRecent(type,2);
    }

    @GetMapping("/keyMetric/{type}/date")
    public String getRecentDate(@PathVariable(value = "type") String type) {
        return new SimpleDateFormat("yyyy/MM/dd").format(keyMetricRepository.getRecent(type,1).get(0).getTimestamp());
    }
}
