package com.example.clip.service;

import com.example.clip.response.PaymentResponse;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentUserReport;

import javax.persistence.PersistenceException;
import java.util.List;

public interface IUsuarioService {

    PaymentResponse paymentSave (PaymentRequest paymentRequest) throws PersistenceException;

    List<PaymentResponse> getUserAll () throws PersistenceException;

    List<PaymentResponse> disbursementProcess ();

    List<PaymentUserReport> reportAllUser ();

    PaymentUserReport reportByUser(String userId);

}
