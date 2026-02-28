package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @InjectMocks
    private CarRepository carRepository;

    @Test
    void testCreateWithAndWithoutId() {
        // Case 1: No ID (Positive/Auto-gen)
        Car car1 = new Car();
        carRepository.create(car1);
        assertNotNull(car1.getCarId());

        // Case 2: ID provided (Positive)
        Car car2 = new Car();
        car2.setCarId("fixed-id");
        carRepository.create(car2);
        assertEquals("fixed-id", car2.getCarId());
    }

    @Test
    void testFindByIdPositive() {
        Car car = new Car();
        car.setCarId("123");
        carRepository.create(car);
        Car found = carRepository.findById("123");
        assertEquals(car, found);
    }

    @Test
    void testFindByIdNegative() {
        Car found = carRepository.findById("non-existent");
        assertNull(found);
    }

    @Test
    void testUpdatePositive() {
        Car car = new Car();
        car.setCarId("123");
        car.setCarName("Old Name");
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarId("123");
        updatedCar.setCarName("New Name");

        Car result = carRepository.update(updatedCar);
        assertNotNull(result);
        assertEquals("New Name", carRepository.findById("123").getCarName());
    }

    @Test
    void testUpdateNegative() {
        Car updatedCar = new Car();
        updatedCar.setCarId("invalid-id");
        Car result = carRepository.update(updatedCar);
        assertNull(result); // Loop should finish without finding it
    }

    @Test
    void testDeletePositive() {
        Car car = new Car();
        car.setCarId("123");
        carRepository.create(car);
        carRepository.delete("123");
        assertNull(carRepository.findById("123"));
    }
}