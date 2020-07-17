package kfu.coviddashboard.coviddashboardspring.controller;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kfu.coviddashboard.coviddashboardspring.model.DayComparatorNameTime;
import kfu.coviddashboard.coviddashboardspring.model.DayComparatorTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kfu.coviddashboard.coviddashboardspring.model.Day;
import kfu.coviddashboard.coviddashboardspring.repository.DayRepository;

@RestController @CrossOrigin(origins = "http://localhost:4200")
public class DayController {
    @Autowired
    private DayRepository dayrepository;

    @GetMapping("/days")
    public List<Day> getAllDays() {
        return dayrepository.findDaysAllCity();
    }
    @GetMapping("/days/{city}")
    public List<String> getAllDays(@PathVariable(value = "city") String city) {
        DayComparatorTime daycomparatortime = new DayComparatorTime();
        List<Day> arr= dayrepository.findDaysByCity(city);
        Collections.sort(arr, daycomparatortime);
        ArrayList<String> ret= new ArrayList<String>();
        for(Day d:arr) {
            ret.add(d.getcount().toString());
        }
        return ret;
    }

    @GetMapping("/days/cities")
    public ArrayList<ArrayList<String>> getAllCityCounts() {
        DayComparatorNameTime daycomparatornametime = new DayComparatorNameTime();
        List<Day> arr= dayrepository.findDaysAllCity();
        Collections.sort(arr, daycomparatornametime);
        ArrayList<ArrayList<String>> ret= new ArrayList<ArrayList<String>>();
        String prev = "";
        for(int i = 0; i<arr.size();i++) {
            if(!arr.get(i).getCity().equals(prev)){//make new list
                ret.add(new ArrayList<String>());
                if(arr.get(i).getcount()==null)
                    ret.get(ret.size()-1).add(null);
                else {
                    ret.get(ret.size() - 1).add(arr.get(i).getcount().toString());
                    prev = arr.get(i).getCity();
                }
            }
            else {
                if(arr.get(i).getcount()==null)
                    ret.get(ret.size()-1).add(null);
                else
                    ret.get(ret.size() - 1).add(arr.get(i).getcount().toString());
            }
        }
        return ret;
    }

    @GetMapping("/days/timestamps")
    public List<String> getSortedTimestamps() {
        DayComparatorTime daycomparatortime = new DayComparatorTime();
        List<Day> arr = dayrepository.findDaysAllCity();
        Collections.sort(arr, daycomparatortime);
        ArrayList<String> ret = new ArrayList<String>();
        for(Day d:arr) {
            Date date = new Date();
            date.setTime(d.getTimestamp().getTime());
            if(!ret.contains(new SimpleDateFormat("yyyy/MM/dd").format(date))) {
                ret.add(new SimpleDateFormat("yyyy/MM/dd").format(date));
            }
        }
        return ret;
    }

/*
    @GetMapping("/days/{id}")
    public ResponseEntity<Day> getDayById(@PathVariable(value = "id") Integer dayId)
            throws ResourceNotFoundException {
        Day day = dayrepository.findById(dayId)
                .orElseThrow(() -> new ResourceNotFoundException("Day not found for this id :: " + dayId));
        return ResponseEntity.ok().body(day);
    }
*/
    /*@PostMapping("/days")
    public Day createDay(@Valid @RequestBody Day day) {
        return dayRepository.save(day);
    }*/

    /*@PutMapping("/days/{id}")
    public ResponseEntity<Day> updateDay(@PathVariable(value = "id") Long dayId,
                                                   @Valid @RequestBody Day dayDetails) throws ResourceNotFoundException {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResourceNotFoundException("Day not found for this id :: " + dayId));

        day.setEmailId(dayDetails.getEmailId());
        day.setLastName(dayDetails.getLastName());
        day.setFirstName(dayDetails.getFirstName());
        final Day updatedDay = dayRepository.save(day);
        return ResponseEntity.ok(updatedDay);
    }*/

    /*@DeleteMapping("/days/{id}")
    public Map<String, Boolean> deleteDay(@PathVariable(value = "id") Long dayId)
            throws ResourceNotFoundException {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResourceNotFoundException("Day not found for this id :: " + dayId));

        dayRepository.delete(day);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }*/
}
