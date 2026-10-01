package com.charter.rewards.repository;

import com.charter.rewards.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Data access for {@link Customer}. */
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>
{
}
