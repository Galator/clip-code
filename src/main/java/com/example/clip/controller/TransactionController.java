package com.example.clip.controller;


import com.example.clip.response.PaymentResponse;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentUserReport;
import com.example.clip.service.IUsuarioService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/")
public class TransactionController {

    @Autowired
    private IUsuarioService usuarioService;


    @ApiOperation(value = "Alta de pago")
    @RequestMapping(value = "createPayload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody @Valid PaymentRequest paymentRequest) {

        try {
            usuarioService.paymentSave(paymentRequest);
            log.info("Payload Created Successfully");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (PersistenceException ex) {
            return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
        }
    }

    @ApiOperation(value="Enlista todos los pagos")
    @GetMapping(value = "payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserList () {

        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
        List<PaymentResponse> paymentList = usuarioService.getUserAll();

        if (!paymentList.isEmpty()) {
            httpStatus = HttpStatus.OK;
        }

        return ResponseEntity.status(httpStatus).body(paymentList);

    }

    @ApiOperation(value="Ejecuta proceso se desembolso")
    @GetMapping(value = "disbursement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> disbursementProcess () {

        HttpStatus httpStatus = HttpStatus.NO_CONTENT;
        List<PaymentResponse> paymentList = usuarioService.disbursementProcess();

        if (!paymentList.isEmpty()) {
            httpStatus = HttpStatus.OK;
        }

        return ResponseEntity.status(httpStatus).body(paymentList);
    }

    @ApiOperation(value="Muestra el reporte de pagos por user id")
    @GetMapping(value = "report/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserReport (@PathVariable String userId) {

        HttpStatus httpStatus = HttpStatus.OK;
        PaymentUserReport paymentUserReport = new PaymentUserReport();
        paymentUserReport = usuarioService.reportByUser(userId);

        if (paymentUserReport == null) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return ResponseEntity.status(httpStatus).body(paymentUserReport);
    }

    @ApiOperation(value="Muestra el reporte de pagos de todos los usuarios")
    @GetMapping(value = "report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserReportAll () {

        HttpStatus httpStatus = HttpStatus.OK;
        List<PaymentUserReport> paymentUserReportList = new ArrayList<>();
        paymentUserReportList = usuarioService.reportAllUser();

        if (paymentUserReportList.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return ResponseEntity.status(httpStatus).body(paymentUserReportList);
    }


}
