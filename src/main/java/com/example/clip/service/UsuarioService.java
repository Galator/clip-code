package com.example.clip.service;

import com.example.clip.model.Payment;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;
import com.example.clip.response.PaymentUserReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    PaymentRepository paymentRepository;

    private static final String NEW_TRANSACTION= "NEW";
    private static final String PROCESSED_TRANSACTION= "PROCESSED";

    @Override
    public void paymentSave(PaymentRequest paymentRequest) throws PersistenceException {

        Payment payment = new Payment(paymentRequest);
        payment.setStatus(NEW_TRANSACTION);

        paymentRepository.save(payment);
        log.info("Payload Created Successfully");
    }

    @Override
    public List<PaymentResponse> getUserAll() {

        List<PaymentResponse> paymentRequestList = new ArrayList<>();
        paymentRepository.findAll().stream().forEach(payment -> paymentRequestList.add(new PaymentResponse(payment)));
        return paymentRequestList;
    }

    @Override
    public List<PaymentResponse> disbursementProcess() {

        List<PaymentResponse> paymentResponseList = disbursement();

        paymentResponseList.isEmpty();
        return paymentResponseList;
    }

    @Override
    public List<PaymentUserReport> reportAllUser() {

        List<PaymentUserReport> paymentUserReportList = new ArrayList<>();

        List<Payment> paymentList = paymentRepository.findAll();

        Map<String, List<Payment>> groupListByUser =paymentList.stream().collect(Collectors.groupingBy(Payment::getUserId));

        groupListByUser.forEach((userId, paymentListUser) -> {

            PaymentUserReport paymentUserReport = sumByUser(paymentListUser);
            paymentUserReport.setUserId(userId);

            paymentUserReportList.add(paymentUserReport);
        } );

        return paymentUserReportList;
    }

    @Override
    public PaymentUserReport reportByUser(String userId) {

        PaymentUserReport paymentUserReport = new PaymentUserReport();

        List<Payment> paymentList = paymentRepository.findByUserId(userId);

        paymentUserReport = sumByUser (paymentList);
        paymentUserReport.setUserId(userId);

        return paymentUserReport;
    }

    private List<PaymentResponse> disbursement () {

        List<PaymentResponse> paymentResponseList = new ArrayList<>();

        paymentResponseList = paymentRepository.findByStatus(NEW_TRANSACTION).map(payments ->  convertEntityToResponse(payments)).orElse(Collections.emptyList());

        return paymentResponseList;
    }

    private List<PaymentResponse> convertEntityToResponse (List<Payment> paymentList) {

        List<PaymentResponse> paymentResponseList = new ArrayList<>();

        paymentList.stream().forEach(payment -> {
            BigDecimal fee = payment.getAmount().multiply(new BigDecimal(3.5)).divide(new BigDecimal(100));

            payment.setDiscountFee(fee);
            payment.setStatus(PROCESSED_TRANSACTION);
            payment.setDisbursement(payment.getAmount().subtract(payment.getDiscountFee()));
            updatePayment(payment);
            paymentResponseList.add(new PaymentResponse(payment));
        });

        return paymentResponseList;
    }

    private void updatePayment (Payment payment) {
        paymentRepository.save(payment);
    }

    private PaymentUserReport sumByUser(List<Payment> paymentList){

        PaymentUserReport paymentUserReport = new PaymentUserReport();

        paymentUserReport.setSumAllPayment(paymentList.stream().mapToDouble(payment -> payment.getAmount().doubleValue()).sum());
        paymentUserReport.setSumNewPayment(paymentList.stream().filter(payment -> payment.getStatus().equals(NEW_TRANSACTION)).mapToDouble(paymentNew -> paymentNew.getAmount().doubleValue()).sum());
        paymentUserReport.setSumProcessPayment(paymentList.stream().filter(payment -> payment.getStatus().equals(PROCESSED_TRANSACTION)).mapToDouble(paymentNew -> paymentNew.getAmount().doubleValue()).sum());
        paymentUserReport.setSumDisbursement(paymentList.stream().filter(payment -> payment.getStatus().equals(PROCESSED_TRANSACTION)).mapToDouble(paymentNew -> paymentNew.getDisbursement().doubleValue()).sum());

        return paymentUserReport;
    }

}
