package com.example.clip.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentUserReport extends PaymentResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sumAllPayment;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sumNewPayment;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sumProcessPayment;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sumDisbursement;

}
