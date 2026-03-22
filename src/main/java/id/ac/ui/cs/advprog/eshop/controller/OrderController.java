package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    // Note: The actual POST /order/create logic wasn't explicitly requested in the PDF's endpoint list,
    // but usually you'd have a POST mapping here to handle the form submission.
    // For the sake of the PDF instructions, we will focus on what was requested.

    @GetMapping("/history")
    public String historyOrderPage() {
        return "OrderHistoryForm";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "OrderHistoryList";
    }
}