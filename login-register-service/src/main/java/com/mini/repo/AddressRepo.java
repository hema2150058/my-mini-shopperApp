package com.mini.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mini.model.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer>{

}
