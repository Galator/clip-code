package com.example.clip.response;

import com.example.clip.model.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    private String userId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal amount;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal fee;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal disbursement;


    public PaymentResponse (Payment payment) {
        this.userId = payment.getUserId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.fee = payment.getDiscountFee();
        this.disbursement = payment.getDisbursement();
    }

}
