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
    void testCreateCarPost() throws Exception {
        mockMvc.perform(post("/car/create")
                        .flashAttr("car", new Car()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testListCarPage() throws Exception {
        when(carService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/car/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("CarList"))
                .andExpect(model().attributeExists("cars"));
    }

    @Test
    void testEditCarPageNegative() throws Exception {
        when(carService.findById("invalid")).thenReturn(null);
        mockMvc.perform(get("/car/edit/invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditCar"));
        // Even if car is null, the controller still returns the view
    }

    @Test
    void testDeleteCarPost() throws Exception {
        mockMvc.perform(post("/car/delete")
                        .param("carId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
        verify(carService, times(1)).deleteCarById("123");
    }
}