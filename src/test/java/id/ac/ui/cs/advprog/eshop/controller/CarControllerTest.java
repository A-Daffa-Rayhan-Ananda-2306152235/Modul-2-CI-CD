package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    void testCreatePageAndPost() throws Exception {
        mockMvc.perform(get("/car/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateCar"));

        mockMvc.perform(post("/car/create").flashAttr("car", new Car()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testListPage() throws Exception {
        when(carService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/car/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("CarList"));
    }

    @Test
    void testEditPageAndPost() throws Exception {
        Car car = new Car();
        car.setCarId("123");
        when(carService.findById("123")).thenReturn(car);

        mockMvc.perform(get("/car/edit/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditCar"));

        mockMvc.perform(post("/car/edit").flashAttr("car", car))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(post("/car/delete").param("carId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }
}