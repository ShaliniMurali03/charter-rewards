package com.charter.rewards.entity;

import jakarta.persistence.*;

/** A customer enrolled in the rewards program. */
@Entity
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    protected Customer() {}

    /**
     * Creates a customer.
     *
     * @param id customer identifier
     * @param name customer name
     */
    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

}
