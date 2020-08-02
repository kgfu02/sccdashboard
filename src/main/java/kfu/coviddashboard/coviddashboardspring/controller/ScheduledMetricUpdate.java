package kfu.coviddashboard.coviddashboardspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.org.apache.xpath.internal.objects.XString;
import kfu.coviddashboard.coviddashboardspring.repository.DayRepository;
import kfu.coviddashboard.coviddashboardspring.repository.KeyMetricRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.gargoylesoftware.htmlunit.BrowserVersion;

@Component
@Configuration
@EnableScheduling
public class ScheduledMetricUpdate {
    private boolean updatedCases,updatedDeaths,updatedNewCases;
    private String chromeDriverPath = "chromedriver";
    @Autowired
    private KeyMetricRepository keyMetricRepository;

    @Scheduled(cron = "0 0 0 ? * *", zone="America/Los_Angeles")
    public void reset() {
        updatedCases = updatedDeaths = updatedNewCases = false;
    }

    //0 0/10 11/1 ? * *
    //* * * ? * *
    @Scheduled(cron = "0 0/10 11/1 ? * *", zone="America/Los_Angeles")
    public void update() throws IOException, JSONException, ParseException, InterruptedException {
        updateCumulativeCases();
        updateCumulativeDeaths();
        updateNewCases();
    }

    public void updateCumulativeCases() throws IOException, InterruptedException {
        if(updatedCases) {return;}
        WebDriver driver = getDriver("https://app.powerbigov.us/view?r=eyJrIjoiMzdlZDFiM2QtZjM5MC00OWY3LWFhYjgtOGM1MWJiMTVmZmVhIiwidCI6IjBhYzMyMDJmLWMzZTktNGY1Ni04MzBkLTAxN2QwOWQxNmIzZiJ9");
        String pageText = driver.findElement(By.tagName("Body")).getText();
        driver.quit();
        updatedCases = scrape("totalCases",pageText);
    }

    public void updateCumulativeDeaths() throws IOException, InterruptedException {
        if(updatedDeaths) {return;}
        WebDriver driver = getDriver("https://app.powerbigov.us/view?r=eyJrIjoiMzdlZDFiM2QtZjM5MC00OWY3LWFhYjgtOGM1MWJiMTVmZmVhIiwidCI6IjBhYzMyMDJmLWMzZTktNGY1Ni04MzBkLTAxN2QwOWQxNmIzZiJ9");
        String pageText = driver.findElement(By.tagName("Body")).getText();
        driver.quit();
        updatedDeaths = scrape("totalDeaths",pageText);
    }

    public void updateNewCases() throws IOException, InterruptedException {
        if(updatedNewCases) {return;}
        WebDriver driver = getDriver("https://app.powerbigov.us/view?r=eyJrIjoiMzdlZDFiM2QtZjM5MC00OWY3LWFhYjgtOGM1MWJiMTVmZmVhIiwidCI6IjBhYzMyMDJmLWMzZTktNGY1Ni04MzBkLTAxN2QwOWQxNmIzZiJ9");
        String pageText = driver.findElement(By.tagName("Body")).getText();
        driver.quit();
        updatedNewCases = scrape("newCases",pageText) ;
    }

    private boolean checkNum(String str) { // check if str is purely a number
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private WebDriver getDriver(String url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu","--window-size=1920,1200","--ignore-certificate-errors","--no-sandbox");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, 100);
        driver.get(url);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        Thread.sleep(1000*4);
        //System.out.println(driver.findElement(By.tagName("Body")).getText());
        return driver;
    }

    private boolean scrape(String type, String pageText) throws IOException { // posts scraped statistic of target type
        String line=null;
        int counter = 0; // 1 = total deaths, 2 = new cases, 3 = new deaths, 4 = total hospitalizations, 5 = new hospitalizations, 6 = total cases
        int counterTarget = getCounterIndex(type);
        boolean updated = false;
        BufferedReader bufReader = new BufferedReader(new StringReader(pageText));
        while( (line=bufReader.readLine()) != null ) {
            if (line.contains("Data last updated")) {
                String update = line.substring(18);
                updated = alreadyUpdated(type, update);
                /*if (updated && type == "totalCases") {updatedCases=true;}
                else if (updated && type == "totalDeaths") {updatedDeaths=true;}*/
            }
            if (checkNum(line)) {
                counter++;
                if (counter == counterTarget && !updated) { // if at target statistic and date has not been seen before
                    // then update
                    keyMetricRepository.postData(type,Integer.parseInt(line),getDate());
                    ZoneId zoneId = ZoneId.of("US/Pacific");
                    ZonedDateTime zt = ZonedDateTime.now(zoneId);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                    FileWriter myWriter = new FileWriter("logKeyMetric.txt", true);
                    myWriter.write("Updated " + type + " database at " + dtf.format(zt) + "\n");
                    myWriter.close();
                    return true;
                }
            }
        }
        return false;
    }

    private int getCounterIndex(String type) { // returns index that statistic appears
        switch(type) { // 1 = total deaths, 2 = new cases, 3 = new deaths, 4 = total hospitalizations, 5 = new hospitalizations, 6 = total cases
            case "totalDeaths": return 1;
            case "totalCases": return 6;
            case "newCases": return 2;
            default: return -1;
        }
    }

    private boolean alreadyUpdated(String type, String update) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZoneId zoneId = ZoneId.of("US/Pacific");
        ZonedDateTime zt = ZonedDateTime.now(zoneId);

        String recentUpdate = new SimpleDateFormat("yyyy-MM-dd").format(keyMetricRepository.getRecent(type).getTimestamp());
        String scrapedUpdate = LocalDate.parse(update, formatter).toString();
        return (scrapedUpdate.compareTo(recentUpdate)<=0); // if scraped date is same or before recent
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZoneId zoneId = ZoneId.of("US/Pacific");
        ZonedDateTime zt = ZonedDateTime.now(zoneId);
        return dtf.format(zt);
    }
}
