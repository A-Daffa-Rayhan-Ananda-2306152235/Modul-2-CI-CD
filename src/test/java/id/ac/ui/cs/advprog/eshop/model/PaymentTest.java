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
        // Valid voucher: 16 chars, starts with ESHOP, exactly 8 digits
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP1234ABC5678");

        this.codData = new HashMap<>();
        this.codData.put(PaymentDataKey.ADDRESS.getValue(), "Jalan Kenangan 123");
        this.codData.put(PaymentDataKey.DELIVERY_FEE.getValue(), "10000");
    }

    // --- HAPPY PATHS ---

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

    // --- VOUCHER SPECIFIC BUSINESS LOGIC TESTS ---

    @Test
    void testCreatePaymentVoucherInvalidLength() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP123"); // Too short
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherDoesNotStartWithEshop() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "CORPZ1234ABC5678"); // Doesn't start with ESHOP
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherNotExactlyEightDigits() {
        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP12ABCDEFGH3"); // Only 3 digits
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());

        this.voucherData.put(PaymentDataKey.VOUCHER_CODE.getValue(), "ESHOP12345678901"); // 11 digits
        Payment payment2 = new Payment("payment-124", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment2.getStatus());
    }

    // --- UNHAPPY PATHS (Missing/Extra Data) ---

    @Test
    void testCreatePaymentEmptyPaymentData() {
        this.voucherData.clear();
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherWithExtraData() {
        this.voucherData.put("extraKey", "unusedData");
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODWithMissingData() {
        this.codData.remove(PaymentDataKey.DELIVERY_FEE.getValue());
        Payment payment = new Payment("payment-456", PaymentMethod.CASH_ON_DELIVERY.getValue(), this.codData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    // --- METHOD & STATUS TESTS ---

    @Test
    void testCreatePaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-123", "MAGIC_SPELL", this.voucherData);
        });
    }

    @Test
    void testSetValidStatus() {
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());

        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetInvalidStatus() {
        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(), this.voucherData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("MEOW");
        });
    }
}