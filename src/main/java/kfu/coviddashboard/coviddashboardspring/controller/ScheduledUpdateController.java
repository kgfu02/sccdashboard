package kfu.coviddashboard.coviddashboardspring.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.InternCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import kfu.coviddashboard.coviddashboardspring.model.Day;
import kfu.coviddashboard.coviddashboardspring.model.DayComparatorNameTime;
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

    String cityDataUrl = "https://services.arcgis.com/NkcnS0qk4w2wasOJ/ArcGIS/rest/services/COVIDCasesByCities/FeatureServer/0/query?where=name+<>+'unincorporated'+AND+name+<>+'santa+cruz+county'+AND+name+<>+'los+altos+hills'+AND+name+<>+'monte+sereno'&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter&returnGeodetic=false&outFields=name,cases&returnGeometry=false&returnCentroid=false&featureEncoding=esriDefault&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnExtentOnly=false&returnQueryGeometry=false&returnDistinctValues=false&cacheHint=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&returnZ=false&returnM=false&returnExceededLimitFeatures=true&quantizationParameters=&sqlFormat=none&f=pjson&token=";

    @Autowired
    private DayRepository dayrepository;

    @Scheduled(fixedRate = 600000)
    public void updateCityCases() throws IOException, JSONException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(cityDataUrl, String.class);
        Map<String, Integer> map = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject obj = new JSONObject(response.getBody());
        //System.out.println("///////"+obj.getJSONArray("features")+"///////");
        JSONArray arr = obj.getJSONArray("features");
        //map = mapper.readValue(response.getBody(), new TypeReference<HashMap>(){});
        for (int i = 0; i < arr.length(); i++) {
            JSONObject city = arr.getJSONObject(i).getJSONObject("attributes");
            map.put(city.getString("NAME").toLowerCase(), city.getInt("Cases"));
        }
        System.out.println("///////" + map + "///////");
        List<Day> d = dayrepository.findDayByCity("san jose");
        DayComparatorNameTime daycomparatornametime = new DayComparatorNameTime();
        Collections.sort(d, daycomparatornametime);
        Day x = d.get(d.size() - 1); // get latest day from san jose
        FileWriter myWriter = new FileWriter("log.txt", true);
        if (x.getcount() != map.get("san jose").intValue()) {
            // change has occured in san jose; can be more robust by comparing total
            postData(map);

            myWriter.write("Updated database at " + new Date() + "\n");
            myWriter.close();
        }
        else {
            myWriter.write("Checked database at " + new Date() + "\n");
            myWriter.close();
        }

    }

    public void postData(Map<String, Integer> map) {
        Date date = new Date(System.currentTimeMillis() - 3600 * 1000*7); // convert from utc to pdt (-7 hrs)
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            dayrepository.postData(entry.getKey(),entry.getValue(),java.time.LocalDate.now().toString());
        }
    }
}
