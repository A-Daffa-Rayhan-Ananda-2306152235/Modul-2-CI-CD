package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void testFindAll() {
        List<Car> mockCars = new ArrayList<>();
        mockCars.add(new Car());
        when(carRepository.findAll()).thenReturn(mockCars.iterator());

        List<Car> result = carService.findAll();
        assertEquals(1, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdPositive() {
        Car car = new Car();
        when(carRepository.findById("123")).thenReturn(car);
        Car result = carService.findById("123");
        assertEquals(car, result);
    }

    @Test
    void testUpdate() {
        Car car = new Car();
        carService.update(car);
        verify(carRepository, times(1)).update(car);
    }
}