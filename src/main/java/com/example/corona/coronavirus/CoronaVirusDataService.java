package com.example.corona.coronavirus;

import models.LocationStatus;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private List<LocationStatus> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* 1 * * * *") // Scheduling it to run every second
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStatus> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvReader);

        boolean countryDoesNotExists;
        for (CSVRecord record : records) {
            countryDoesNotExists = true;
            LocationStatus locationStatus = new LocationStatus();
            String currentCountry = record.get("Country/Region");
            System.out.println(currentCountry);

            for (LocationStatus item : newStats) {
                if (item.getCountry().equals(currentCountry)) {
                    item.setState(item.getState()+", "+record.get("Province/State"));
                    item.setLatestTotal(item.getLatestTotal() + Integer.parseInt(record.get(record.size() - 1)));
                    countryDoesNotExists = false;
                }
            }

            if(countryDoesNotExists)
            {
                locationStatus.setCountry(currentCountry);
                locationStatus.setState(record.get("Province/State"));
                locationStatus.setLatestTotal(Integer.parseInt(record.get(record.size() - 1)));
                newStats.add(locationStatus);
            }
        }


        newStats.sort((o1, o2) -> o1.getCountry().compareTo(o2.getCountry()));
//        newStats.sort((o1, o2) -> o1.getCountry().compareTo(o2.getCountry()));
        this.allStats = newStats;
    }

    public List<LocationStatus> getAllStats() {
        return allStats;
    }
}
