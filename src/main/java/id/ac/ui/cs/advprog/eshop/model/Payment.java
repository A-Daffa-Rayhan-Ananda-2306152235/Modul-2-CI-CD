package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentDataKey;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    String id;
    Order order;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id;
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (!OrderStatus.WAITING_PAYMENT.getValue().equals(order.getStatus())) {
            throw new IllegalArgumentException("Order must be WAITING_PAYMENT");
        }
        if (method == null || !PaymentMethod.contains(method)) {
            throw new IllegalArgumentException("Invalid payment method");
        }
        this.order = order;
        this.paymentData = paymentData;
        this.method = method;

        boolean isDataValid = true;

        if (paymentData == null || paymentData.isEmpty()) {
            isDataValid = false;
        } else if (method.equals(PaymentMethod.VOUCHER.getValue())) {
            // Must have exactly 1 key, and it must be VOUCHER_CODE
            if (paymentData.size() != 1 || !paymentData.containsKey(PaymentDataKey.VOUCHER_CODE.getValue())) {
                isDataValid = false;
            } else {
                String code = paymentData.get(PaymentDataKey.VOUCHER_CODE.getValue());
                if (code == null || code.trim().isEmpty()) {
                    isDataValid = false;
                } else {
                    // Check voucher business logic: 16 chars, starts with ESHOP, 8 digits
                    int numCount = 0;
                    for (int i = 0; i < code.length(); i++) {
                        if (Character.isDigit(code.charAt(i))) {
                            numCount++;
                        }
                    }
                    if (code.length() != 16 || !code.startsWith("ESHOP") || numCount != 8) {
                        isDataValid = false;
                    }
                }
            }
        } else if (method.equals(PaymentMethod.CASH_ON_DELIVERY.getValue())) {
            // Must have exactly 2 keys: ADDRESS and DELIVERY_FEE
            if (paymentData.size() != 2 ||
                    !paymentData.containsKey(PaymentDataKey.ADDRESS.getValue()) ||
                    !paymentData.containsKey(PaymentDataKey.DELIVERY_FEE.getValue())) {
                isDataValid = false;
            } else {
                String address = paymentData.get(PaymentDataKey.ADDRESS.getValue());
                String fee = paymentData.get(PaymentDataKey.DELIVERY_FEE.getValue());
                if (address == null || address.trim().isEmpty() || fee == null || fee.trim().isEmpty()) {
                    isDataValid = false;
                }
            }
        }

        if (isDataValid) {
            this.status = PaymentStatus.WAITING.getValue();
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
            this.order.setStatus(OrderStatus.FAILED.getValue());
        }
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
            if (status.equals(PaymentStatus.SUCCESS.getValue())) {
                this.order.setStatus(OrderStatus.SUCCESS.getValue());
            } else if (status.equals(PaymentStatus.REJECTED.getValue())) {
                this.order.setStatus(OrderStatus.FAILED.getValue());
            } else if (status.equals(PaymentStatus.WAITING.getValue())) {
                this.order.setStatus(OrderStatus.WAITING_PAYMENT.getValue());
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}