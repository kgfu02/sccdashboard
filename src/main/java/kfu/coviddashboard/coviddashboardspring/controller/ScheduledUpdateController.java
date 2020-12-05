package kfu.coviddashboard.coviddashboardspring.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.InternCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import kfu.coviddashboard.coviddashboardspring.model.*;
import kfu.coviddashboard.coviddashboardspring.repository.DayRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
@EnableScheduling
public class ScheduledUpdateController {

    private String cityDataUrl = "https://services.arcgis.com/NkcnS0qk4w2wasOJ/ArcGIS/rest/services/COVIDCasesByCities/FeatureServer/0/query?where=name+<>+'unincorporated'+AND+name+<>+'santa+cruz+county'+AND+name+<>+'los+altos+hills'+AND+name+<>+'monte+sereno'&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter&returnGeodetic=false&outFields=name,cases&returnGeometry=false&returnCentroid=false&featureEncoding=esriDefault&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnExtentOnly=false&returnQueryGeometry=false&returnDistinctValues=false&cacheHint=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&returnZ=false&returnM=false&returnExceededLimitFeatures=true&quantizationParameters=&sqlFormat=none&f=pjson&token=";
    private String zipcodeDataUrl = "https://services.arcgis.com/NkcnS0qk4w2wasOJ/ArcGIS/rest/services/COVIDCasesByZipCode/FeatureServer/0/query?where=1=1&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter&returnGeodetic=false&outFields=zipcode,cases&returnGeometry=false&returnCentroid=false&featureEncoding=esriDefault&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnExtentOnly=false&returnQueryGeometry=false&returnDistinctValues=false&cacheHint=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&returnZ=false&returnM=false&returnExceededLimitFeatures=true&quantizationParameters=&sqlFormat=none&f=pjson&token=";
    private boolean updatedZipcode;
    private boolean updatedCity = updatedZipcode = false;
    @Autowired
    private DayRepository dayrepository;

    @Scheduled(cron = "0 0 0 ? * *", zone="America/Los_Angeles")
    public void reset() {
        updatedCity = updatedZipcode = false;
    }
    //city Update
    @Scheduled(cron = "0 0/30 11/1 ? * *", zone="America/Los_Angeles")
    public void updateCityCases() throws IOException, JSONException, ParseException {
        if(updatedCity) {return;}

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(cityDataUrl, String.class);
        Map<String, Integer> map = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject obj = new JSONObject(response.getBody());
        //System.out.println("///////"+obj.getJSONArray("features")+"///////");
        JSONArray arr = obj.getJSONArray("features");
        //map = mapper.readValue(response.getBody(), new TypeReference<HashMap>(){});
        for (int i = 0; i < arr.length(); i++) { // insert JSON to Map
            JSONObject city = arr.getJSONObject(i).getJSONObject("attributes");
            map.put(city.getString("NAME").toLowerCase(), city.getInt("Cases"));
        }
        System.out.println("///////" + map + "///////");
        List<Day> d = dayrepository.findDaysByCity("san jose");
        DayComparatorNameTime daycomparatornametime = new DayComparatorNameTime();
        Collections.sort(d, daycomparatornametime);
        Day x = d.get(d.size() - 1); // get latest day from san jose
        FileWriter myWriter = new FileWriter("log.txt", true);
        // timestamp of update
        ZoneId zoneId = ZoneId.of("US/Pacific");
        ZonedDateTime zt = ZonedDateTime.now(zoneId);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        if (x.getcount() != map.get("san jose").intValue()) {
            // change has occured in san jose; can be more robust by comparing total
            postData(map,"dashboardtable");
            updatedCity = true;
            myWriter.write("Updated cities database at " + dtf.format(zt) + "\n");
            myWriter.close();
        }
        else {
            myWriter.write("Checked cities database at " + dtf.format(zt) + "\n");
            myWriter.close();
        }

    }
    //0 0/10 11/1 ? * *
    //* * * ? * *
    @Scheduled(cron = "0 0/30 11/1 ? * *", zone="America/Los_Angeles")
    public void updateZipcodeCases() throws IOException, JSONException, ParseException {
        if(updatedZipcode) {return;}

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(zipcodeDataUrl, String.class);
        Map<String, Integer> map = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject obj = new JSONObject(response.getBody());
        //System.out.println("///////"+obj.getJSONArray("features")+"///////");
        JSONArray arr = obj.getJSONArray("features");
        //map = mapper.readValue(response.getBody(), new TypeReference<HashMap>(){});
        int todaySum = 0;
        for (int i = 0; i < arr.length(); i++) { // insert JSON to Map
            JSONObject zipcode = arr.getJSONObject(i).getJSONObject("attributes");
            System.out.println(zipcode.get("Cases"));
            if(!zipcode.get("Cases").equals(null)) {
                todaySum += zipcode.getInt("Cases");
                map.put(zipcode.getString("Zipcode").toLowerCase(), zipcode.getInt("Cases"));
            }
        }
        System.out.println("///////" + map + "///////");
        //check if updated
        ZoneId zoneId = ZoneId.of("US/Pacific");
        ZonedDateTime zt = ZonedDateTime.now(zoneId);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        List<ZipcodeDay> d = dayrepository.findZipcodeDaysAll();
        //sum cases
        int yesterdaySum = sumDays(d);
        FileWriter myWriter = new FileWriter("log.txt", true);
        if (todaySum > yesterdaySum) {
            // change has occured in total cases, update
            postData(map,"zipcodetable");
            updatedZipcode = true;
            myWriter.write("Updated zipcode database at " + dtf.format(zt) + "\n");
            myWriter.close();
        }
        else {
            myWriter.write("Checked zipcode database at " + dtf.format(zt) + "\n");
            myWriter.close();
        }

    }

    public void postData(Map<String, Integer> map, String tableName) {
        ZoneId zoneId = ZoneId.of("US/Pacific");
        ZonedDateTime zt = ZonedDateTime.now(zoneId);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            if(tableName.equals("dashboardtable"))
                dayrepository.postDataCity(entry.getKey(),entry.getValue(),dtf.format(zt));
            else
                dayrepository.postDataZipcode(entry.getKey(),entry.getValue(),dtf.format(zt));

        }
    }

    private int sumDays(List<? extends Entry> days) {
        if(days.size()==0) {return 0;}
        DayComparatorTime daycomparatortime = new DayComparatorTime();
        Collections.sort(days, daycomparatortime);
        Timestamp yesterday = days.get(days.size()-1).getTimestamp(); //not necessarily yesterday, most recent entry is more accurate
        if(days.get(days.size()-1).getcount()==null) { //if manually inputted null skip check
            return 0;
        }
        int i = days.size()-1;
        int sum = 0;
        while(i>=0 && days.get(i).getTimestamp().equals(yesterday)) {
            sum += days.get(i).getcount();
            i--;
        }
        return sum;
    }
}
