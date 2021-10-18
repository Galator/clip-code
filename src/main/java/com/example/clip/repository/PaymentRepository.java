package com.example.clip.repository;

import com.example.clip.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    Optional<List<Payment>> findByStatus(String status) throws PersistenceException;

    List<Payment> findByUserId(String userId) throws PersistenceException;
}
