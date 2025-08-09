package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    private final List<SearchResult> searchResults = new ArrayList<>();

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) { }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream hotels = getClass().getResourceAsStream("/hotels.json")) {
            searchResults.addAll(mapper.readValue(hotels, new TypeReference<List<SearchResult>>() {}));
        }

        try (InputStream cars = getClass().getResourceAsStream("/rental_cars.json")) {
            searchResults.addAll(mapper.readValue(cars, new TypeReference<List<SearchResult>>() {}));
        }

        environment.jersey().register(new SearchResource(searchResults));
    }
}
