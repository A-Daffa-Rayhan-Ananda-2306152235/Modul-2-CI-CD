package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentDataKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Map<String, String> voucherData;
    private Map<String, String> codData;
    private Order order;

    @BeforeEach
    void setup() {
        this.voucherData = new HashMap<>();
        // Valid voucher: 16 chars, starts with ESHOP, exactly 8 digits
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP1234ABC5678");

        this.codData = new HashMap<>();
        this.codData.put(PaymentDataKey.ADDRESS.getValue(), "Jalan Kenangan 123");
        this.codData.put(PaymentDataKey.DELIVERY_FEE.getValue(), "10000");

        // Set up a valid dummy order
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);
        products.add(product);
        this.order = new Order("order-123", products, 1708560000L, "Safira");
    }

    // --- HAPPY PATHS ---

    @Test
    void testCreatePaymentVoucherSuccess() {
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODSuccess() {
        Payment payment = new Payment("payment-456", this.order, PaymentMethod.CASH_ON_DELIVERY.getValue(), this.codData);
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getStatus());
    }

    // --- VOUCHER SPECIFIC BUSINESS LOGIC TESTS ---

    @Test
    void testCreatePaymentVoucherInvalidLength() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP123"); // Too short
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testCreatePaymentVoucherDoesNotStartWithEshop() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "CORPZ1234ABC5678"); // Doesn't start with ESHOP
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testCreatePaymentVoucherNotExactlyEightDigits() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP12ABCDEFGH3"); // Only 3 digits
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());

        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP12345678901"); // 11 digits
        Payment payment2 = new Payment("payment-124", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment2.getStatus());
    }

    // --- UNHAPPY PATHS (Missing/Extra Data) ---

    @Test
    void testCreatePaymentEmptyPaymentData() {
        this.voucherData.clear();
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testCreatePaymentVoucherWithExtraData() {
        this.voucherData.put("extraKey", "unusedData");
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testCreatePaymentCODWithMissingData() {
        this.codData.remove(PaymentDataKey.DELIVERY_FEE.getValue());
        Payment payment = new Payment("payment-456", this.order, PaymentMethod.CASH_ON_DELIVERY.getValue(), this.codData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    // --- METHOD & STATUS TESTS ---

    @Test
    void testCreatePaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", this.order, "MAGIC_SPELL", this.voucherData);
        });
    }

    @Test
    void testSetValidStatus() {
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), payment.getOrder().getStatus());

        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
    }

    @Test
    void testSetInvalidStatus() {
        Payment payment = new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("MEOW");
        });
    }

    // --- ORDER VALIDATION TESTS ---

    @Test
    void testCreatePaymentWithNullOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", null, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        });
    }

    @Test
    void testCreatePaymentWithInvalidOrderStatus() {
        // Change order status to something other than WAITING_PAYMENT
        this.order.setStatus(OrderStatus.SUCCESS.getValue());

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", this.order, PaymentMethod.VOUCHER.getValue(), this.voucherData);
        });
    }
}