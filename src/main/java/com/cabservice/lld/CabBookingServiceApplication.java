package com.cabservice.lld;

import com.cabservice.lld.model.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.cabservice.lld.helper.DIModule;
import com.cabservice.lld.model.dto.CabSnapshot;
import com.cabservice.lld.model.dto.CityTimeCountKey;
import com.cabservice.lld.service.intf.CabService;
import com.cabservice.lld.service.intf.CityService;
import com.cabservice.lld.service.intf.RiderService;
import com.cabservice.lld.service.intf.TripService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CabBookingServiceApplication {


    public static void main(String[] args) {

        CabBookingServiceApplication cabBookingServiceApplication = new CabBookingServiceApplication();
        Injector injector = Guice.createInjector(new DIModule());

        CabService cabService = injector.getInstance(CabService.class);
        CityService cityService = injector.getInstance(CityService.class);
        RiderService riderService = injector.getInstance(RiderService.class);
        TripService tripService = injector.getInstance(TripService.class);

        cabBookingServiceApplication.testPositiveFlow(cabService, cityService, riderService, tripService);

    }

    private void testPositiveFlow(CabService cabService, CityService cityService, RiderService riderService, TripService tripService) {
        List<City> cities = initializeCity(cityService);

        List<Rider> riders = initializeRiders(riderService);

        List<Cab> cabs = initializeCabs(cabService);

        setCabLocationAndRider(cabService, cities, riders, cabs);

        Trip trip1 = tripService.bookCab(cities.get(0), cities.get(1));
        System.out.println("Started trip " + trip1);
        Trip trip2 = tripService.bookCab(cities.get(0), cities.get(1));
        System.out.println("Started trip " + trip2);
        Trip trip3 = tripService.bookCab(cities.get(0), cities.get(1));
        System.out.println("Started trip " + trip3);
        try {
            Trip trip4 = tripService.bookCab(cities.get(0), cities.get(1)); // This should throw exception as there are no cabs available at city 1 now.
        } catch(Exception e) {
            System.out.println("Unable to book trip " + e.getMessage());
        }

        try {
            Thread.sleep(2000); //Waiting for two secs for adding delay into the system
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted " + e.getMessage());
        }
        tripService.endTrip(trip1);
        Trip trip4 = tripService.bookCab(cities.get(1), cities.get(0));
        System.out.println("Started trip " + trip4);

        Trip trip5 = tripService.bookCab(cities.get(1), cities.get(0));
        System.out.println("Started trip " + trip5);

        Trip trip6 = tripService.bookCab(cities.get(1), cities.get(0));
        System.out.println("Started trip " + trip6);

        Trip trip7 = tripService.bookCab(cities.get(1), cities.get(0));
        System.out.println("Started trip " + trip7);


        getCabSnapshot(cabService);

        try {
            tripService.endTrip(trip1); // This would throw exception as trip1 is already completed
        } catch(Exception e) {
            System.out.println("Unable to end trip as this is already finished");
        }
        tripService.endTrip(trip2);
        tripService.endTrip(trip3);
        tripService.endTrip(trip4);

        getCabHistoricalStates(cabService, cabs.get(0));

        getOrderWiseCityDemand(cityService);
    }

    private void getOrderWiseCityDemand(CityService cityService) {
        Set<CityTimeCountKey> demandWiseCity = cityService.getDemandWiseCityList();
        System.out.println("Demand wise city");
        for(CityTimeCountKey cityTimeCountKey : demandWiseCity) {
            System.out.println(cityTimeCountKey);
        }
    }

    private void getCabHistoricalStates(CabService cabService, Cab cab) {
        List<CabStateMapping> cabStateMappings = cabService.getCabStateHistory(cab);
        System.out.println("Cab state history");
        for(CabStateMapping cabStateMapping : cabStateMappings) {
            System.out.println(cabStateMapping);
        }
    }


    private void getCabSnapshot(CabService cabService) {
        List<CabSnapshot> cabSnapshots = cabService.getAllCabSnapshot();
        System.out.println("Cab snapshots: ");
        for(CabSnapshot cabSnapshot : cabSnapshots) {
            System.out.println(cabSnapshot);
        }
    }

    private void setCabLocationAndRider(CabService cabService, List<City> cities, List<Rider> riders, List<Cab> cabs) {
        for (int i = 0; i < 9; i++) {
            Cab cab = cabs.get(i);
            Rider rider = riders.get(i);
            cabService.assignRiderToCab(cab, rider);
            City city = cities.get(i % 3);
            cabService.changeCabLocation(cab, city);
        }
    }

    private List<Cab> initializeCabs(CabService cabService) {
        List<Cab> cabs = new ArrayList<>(10);
        for (int i = 0; i < 9; i++) {
            Cab cab = Cab.builder()
                    .cabNumber("Cab number " + (i + 1))
                    .build();
            cabs.add(cabService.registerCab(cab));
        }
        return cabs;
    }

    private List<Rider> initializeRiders(RiderService riderService) {
        List<Rider> riders = new ArrayList<>(10);
        for (int i = 0; i < 9; i++) {
            Rider rider = Rider.builder()
                    .licenseNumber("License " + (i + 1))
                    .build();
            riders.add(riderService.addRider(rider));
        }
        return riders;
    }

    private List<City> initializeCity(CityService cityService) {
        List<City> cities = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            City city = City.builder()
                    .cityName("City " + (i + 1))
                    .build();
            cities.add(cityService.addCity(city));
        }
        return cities;
    }
}
