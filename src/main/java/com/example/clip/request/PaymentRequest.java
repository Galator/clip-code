package com.example.clip.request;

import com.example.clip.model.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

    String userId;
    BigDecimal amount;
}
