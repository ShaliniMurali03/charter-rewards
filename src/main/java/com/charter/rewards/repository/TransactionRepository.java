package com.charter.rewards.repository;

import com.charter.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/** Data access for {@link Transaction}. */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>
{
        /**
         * Finds all transactions for a given customer.
         *
         * @param customerId the unique identifier of the customer
         * @return list of transactions belonging to the customer
         */
        List<Transaction> findByCustomerId(Long customerId);

        /**
         * Retrieves all distinct customer IDs that have recorded transactions.
         *
         * @return list of unique customer IDs
         */
        @Query("SELECT DISTINCT t.customer.id FROM Transaction t")
        List<Long> findDistinctCustomerIds();

        /**
         * Retrieves the name of a customer by their ID.
         *
         * @param customerId the unique identifier of the customer
         * @return the customer's name
         */
        @Query("SELECT c.name FROM Customer c WHERE c.id = :customerId")
        String findCustomerNameById(@Param("customerId") Long customerId);

        /**
         * Finds all transactions for a customer within a given date range.
         *
         * @param customerId the unique identifier of the customer
         * @param threeMonthsAgo start date of the range
         * @param now end date of the range
         * @return list of transactions within the specified period
         */
        List<Transaction> findByCustomer_IdAndTransactionDateBetween(Long customerId, LocalDate threeMonthsAgo, LocalDate now);
}
