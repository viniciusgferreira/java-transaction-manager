package com.wex.transactionmanager.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wex.transactionmanager.models.PurchaseTransaction;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, UUID> {

}
