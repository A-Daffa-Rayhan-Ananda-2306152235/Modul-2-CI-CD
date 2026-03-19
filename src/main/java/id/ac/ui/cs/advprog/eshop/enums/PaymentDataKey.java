package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentDataKey {
    VOUCHER_CODE("voucherCode"),
    ADDRESS("address"),
    DELIVERY_FEE("deliveryFee");

    private final String value;

    private PaymentDataKey(String value) {
        this.value = value;
    }
}