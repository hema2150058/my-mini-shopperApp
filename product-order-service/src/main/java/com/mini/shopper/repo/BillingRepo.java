package com.mini.shopper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mini.shopper.model.Billing;

@Repository
public interface BillingRepo extends JpaRepository<Billing, Long> {

}
