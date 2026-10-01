package com.charter.rewards.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/** A purchase made by a customer. */
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private Double amount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    protected Transaction() {
    }

    /**
     * Creates a transaction.
     *
     * @param customer the purchasing customer
     * @param amount the purchase amount in dollars
     * @param date the date of the purchase
     */
    public Transaction(Customer customer, Double amount, LocalDate date)
    {
        this.customer = customer;
        this.amount = amount;
        this.transactionDate = date;
    }

    public Long getId()
    {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Double getAmount()
    {
        return amount;
    }

    public LocalDate getDate()
    {
        return transactionDate;
    }
}

