package com.bikeshare.lab3;

import com.bikeshare.model.Bike;
import com.bikeshare.model.Bike.BikeType;
import com.bikeshare.model.Bike.BikeStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class BikeStructuralTest {
    private Bike standardBike;
    private Bike electricBike;

    @BeforeEach
    void setUp() {
        standardBike = new Bike("S1", BikeType.STANDARD);
        electricBike = new Bike("E1", BikeType.ELECTRIC);
    }

    @Test
    void testReserveAvailableBike() {
        standardBike.reserve();
        assertEquals(BikeStatus.RESERVED, standardBike.getStatus());
    }

    @Test
    void testReserveNotAvailableThrows() {
        standardBike.reserve();
        assertThrows(IllegalStateException.class, () -> standardBike.reserve());
    }

    @Test
    void testStartRideAvailableBike() {
        standardBike.startRide();
        assertEquals(BikeStatus.IN_USE, standardBike.getStatus());
    }

    @Test
    void testStartRideReservedBike() {
        standardBike.reserve();
        standardBike.startRide();
        assertEquals(BikeStatus.IN_USE, standardBike.getStatus());
    }

    @Test
    void testStartRideNotAvailableThrows() {
        standardBike.markAsBroken();
        assertThrows(IllegalStateException.class, () -> standardBike.startRide());
    }

    @Test
    void testStartRideLowBatteryThrows() {
        electricBike.chargeBattery(-95);
        electricBike.reserve();
        electricBike.chargeBattery(-1);
        assertThrows(IllegalStateException.class, () -> electricBike.startRide());
    }

    @Test
    void testEndRideUpdatesStats() {
        standardBike.startRide();
        standardBike.endRide(5.0);
        assertEquals(BikeStatus.AVAILABLE, standardBike.getStatus());
        assertEquals(1, standardBike.getTotalRides());
        assertEquals(5.0, standardBike.getTotalDistance());
    }

    @Test
    void testEndRideNotInUseThrows() {
        assertThrows(IllegalStateException.class, () -> standardBike.endRide(1.0));
    }

    @Test
    void testEndRideNegativeDistanceThrows() {
        standardBike.startRide();
        assertThrows(IllegalArgumentException.class, () -> standardBike.endRide(-1.0));
    }

    @Test
    void testSendToMaintenance() {
        standardBike.sendToMaintenance();
        assertEquals(BikeStatus.MAINTENANCE, standardBike.getStatus());
    }

    @Test
    void testSendToMaintenanceInUseThrows() {
        standardBike.startRide();
        assertThrows(IllegalStateException.class, () -> standardBike.sendToMaintenance());
    }

    @Test
    void testCompleteMaintenance() {
        standardBike.sendToMaintenance();
        standardBike.completeMaintenance();
        assertEquals(BikeStatus.AVAILABLE, standardBike.getStatus());
        assertFalse(standardBike.needsMaintenance());
    }

    @Test
    void testCompleteMaintenanceNotInMaintenanceThrows() {
        assertThrows(IllegalStateException.class, () -> standardBike.completeMaintenance());
    }

    @Test
    void testMarkAsBroken() {
        standardBike.markAsBroken();
        assertEquals(BikeStatus.BROKEN, standardBike.getStatus());
        assertTrue(standardBike.needsMaintenance());
    }

    @Test
    void testChargeBatteryElectric() {
        electricBike.chargeBattery(10);
        assertEquals(100.0, electricBike.getBatteryLevel());
    }

    @Test
    void testChargeBatteryNonElectricThrows() {
        assertThrows(IllegalStateException.class, () -> standardBike.chargeBattery(10));
    }

    @Test
    void testChargeBatteryInvalidAmountThrows() {
        assertThrows(IllegalArgumentException.class, () -> electricBike.chargeBattery(-1));
        assertThrows(IllegalArgumentException.class, () -> electricBike.chargeBattery(101));
    }

    @Test
    void testIsAvailable() {
        assertTrue(standardBike.isAvailable());
        standardBike.markAsBroken();
        assertFalse(standardBike.isAvailable());
    }
}
