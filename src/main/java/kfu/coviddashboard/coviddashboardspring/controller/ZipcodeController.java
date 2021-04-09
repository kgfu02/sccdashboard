package kfu.coviddashboard.coviddashboardspring.controller;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import kfu.coviddashboard.coviddashboardspring.model.Day;
import kfu.coviddashboard.coviddashboardspring.model.DayComparatorTime;
import kfu.coviddashboard.coviddashboardspring.model.ZipcodeDay;
import kfu.coviddashboard.coviddashboardspring.repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController @CrossOrigin(origins = "http://localhost:4200")
public class ZipcodeController {
    @Autowired
    private DayRepository dayRepository;

    @GetMapping("/zipcodes/cases")
    public Map<String,ArrayList<Integer>> getZipcodeCases() { // returns
        DayComparatorTime daycomparatortime = new DayComparatorTime();
        List<ZipcodeDay> cases = dayRepository.findZipcodeDaysAll();
        Collections.sort(cases, daycomparatortime);
        Map<String, ArrayList<Integer>> map = new TreeMap<>();
        for (int i = 0; i<cases.size(); i++) {
            if(!map.containsKey(cases.get(i).getName())) {
                ArrayList<Integer> x = new ArrayList<>();
                x.add(cases.get(i).getcount());
                map.put(cases.get(i).getName(),x);
            }
            else {
                map.get(cases.get(i).getName()).add(cases.get(i).getcount());
            }
        }
        return map;
    }

    @GetMapping("/zipcodes/timestamps")
    public List<String> getSortedTimestamps() {
        DayComparatorTime daycomparatortime = new DayComparatorTime();
        List<ZipcodeDay> arr = dayRepository.findZipcodeDaysAll();
        Collections.sort(arr, daycomparatortime);
        ArrayList<String> ret = new ArrayList<String>();
        for(ZipcodeDay d:arr) {
            Date date = new Date();
            date.setTime(d.getTimestamp().getTime());
            if(!ret.contains(new SimpleDateFormat("yyyy/MM/dd").format(date))) {
                ret.add(new SimpleDateFormat("yyyy/MM/dd").format(date));
            }
        }
        return ret;
    }
}
