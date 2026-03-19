package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentDataKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Map<String, String> voucherData;
    private Map<String, String> codData;

    @BeforeEach
    void setup() {
        this.voucherData = new HashMap<>();
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP1234ABC5678");

        this.codData = new HashMap<>();
        this.codData.put(PaymentDataKey.ADDRESS.getValue(), "Jalan Kenangan 123");
        this.codData.put(PaymentDataKey.DELIVERY_FEE.getValue(), "10000");
    }

    // --- HAPPY PATHS (Should default to WAITING) ---

    @Test
    void testCreatePaymentVoucherSuccess() {
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODSuccess() {
        Payment payment = new Payment("payment-456", PaymentMethod.CASH_ON_DELIVERY.getValue(), this.codData);
        assertEquals(PaymentStatus.WAITING.getValue(), payment.getStatus());
    }

    // --- UNHAPPY PATHS (Should default to REJECTED instead of throwing errors) ---

    @Test
    void testCreatePaymentEmptyPaymentData() {
        this.voucherData.clear();
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherWithExtraData() {
        this.voucherData.put("extraKey", "unusedData"); // Map size > 1
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODWithMissingData() {
        this.codData.remove(PaymentDataKey.DELIVERY_FEE.getValue()); // Map size < 2
        Payment payment = new Payment("payment-456", PaymentMethod.CASH_ON_DELIVERY.getValue(), this.codData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentWithEmptyStringValue() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), ""); // Empty value
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    // --- INVALID METHOD OR STATUS (Still throws exception because it's a developer error) ---

    @Test
    void testCreatePaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", "MAGIC_SPELL", this.voucherData);
        });
    }

    @Test
    void testSetInvalidStatus() {
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("MEOW");
        });
    }
}