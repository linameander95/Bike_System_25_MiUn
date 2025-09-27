package com.bikeshare.lab3;

import com.bikeshare.model.Bike;
import com.bikeshare.model.Bike.BikeType;
import com.bikeshare.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StationBikeIntegrationTest {
    private Station station;
    private Bike standardBike;
    private Bike electricBike;

    @BeforeEach
    void setUp() {
        station = new Station("ST1", "Central", "123 Main St", 59.33, 18.06, 10);
        standardBike = new Bike("B1", BikeType.STANDARD);
        electricBike = new Bike("B2", BikeType.ELECTRIC);
    }

    @Test
    void testAddBikeToStation() {
        station.addBike(standardBike);
        assertTrue(station.getAllBikes().contains(standardBike));
        assertEquals("ST1", standardBike.getCurrentStationId());
    }

    @Test
    void testAddBikeNotAvailableThrows() {
        standardBike.reserve();
        assertThrows(IllegalStateException.class, () -> station.addBike(standardBike));
    }

    @Test
    void testRemoveBikeFromStation() {
        station.addBike(standardBike);
        Bike removed = station.removeBike(standardBike.getBikeId());
        assertEquals(standardBike, removed);
        assertNull(removed.getCurrentStationId());
    }

    @Test
    void testRemoveBikeNotFoundThrows() {
        assertThrows(IllegalStateException.class, () -> station.removeBike("B1"));
    }

    @Test
    void testReserveBikeAtStation() {
        station.addBike(standardBike);
        station.reserveBike(standardBike.getBikeId());
        assertThrows(IllegalStateException.class, () -> station.reserveBike(standardBike.getBikeId()));
    }

    @Test
    void testCancelReservation() {
        station.addBike(standardBike);
        station.reserveBike(standardBike.getBikeId());
        station.cancelReservation(standardBike.getBikeId());
        // Should be able to reserve again
        station.reserveBike(standardBike.getBikeId());
    }

    @Test
    void testGetAvailableBikeByType() {
        station.addBike(standardBike);
        station.addBike(electricBike);
        Bike found = station.getAvailableBike(BikeType.ELECTRIC);
        assertEquals(electricBike, found);
    }

    @Test
    void testGetAvailableBikeNoneFound() {
        assertNull(station.getAvailableBike(BikeType.PREMIUM));
    }

    @Test
    void testBusinessLogicFullStation() {
        for (int i = 0; i < 10; i++) {
            Bike b = new Bike("B" + i, BikeType.STANDARD);
            station.addBike(b);
        }
        Bike extraBike = new Bike("B11", BikeType.STANDARD);
        assertThrows(IllegalStateException.class, () -> station.addBike(extraBike));
    }
}
