package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentDataKey;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    private Order order;
    private Map<String, String> voucherData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-123", products, 1708560000L, "Safira");

        voucherData = new HashMap<>();
        voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP1234ABC5678");
    }

    @Test
    void testAddPayment() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), voucherData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), voucherData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(payment.getId(), result.getId());
        assertEquals(PaymentStatus.WAITING.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), voucherData);
        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(payment);
        doReturn(order).when(orderRepository).save(order);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), voucherData);
        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(payment);
        doReturn(order).when(orderRepository).save(order);

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testSetStatusPaymentNotFound() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), voucherData);

        // Simulate repository not finding the payment
        doReturn(null).when(paymentRepository).findById(payment.getId());

        assertThrows(NoSuchElementException.class, () -> {
            paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        });
    }

    @Test
    void testGetPaymentIfFound() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), voucherData);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIfNotFound() {
        doReturn(null).when(paymentRepository).findById("invalid-id");
        assertThrows(NoSuchElementException.class, () -> paymentService.getPayment("invalid-id"));
    }

    @Test
    void testGetAllPayments() {
        Payment payment = new Payment("payment-123", order, PaymentMethod.VOUCHER.getValue(), voucherData);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        doReturn(paymentList).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
    }
}