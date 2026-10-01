package com.charter.rewards.service;

import com.charter.rewards.dto.CustomerRewardsDTO;
import com.charter.rewards.entity.Customer;
import com.charter.rewards.entity.Transaction;
import com.charter.rewards.exception.CustomerNotFoundException;
import com.charter.rewards.repository.CustomerRepository;
import com.charter.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the business logic for the Charter Rewards Program.
 * Calculates reward points for customers.
 */
@Service
public class RewardService
{
    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;

    @Value("${rewards.threshold.first}")
    private int firstThreshold;

    @Value("${rewards.threshold.second}")
    private int secondThreshold;

    public RewardService(CustomerRepository customerRepository,TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves reward points for a specific customer across all transactions.
     *
     * @param customerId the unique identifier of the customer
     * @return a DTO containing customer details and reward points
     * @throws CustomerNotFoundException if the customer does not exist or has no transactions
     */
    public CustomerRewardsDTO getCustomerRewards(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " has no transactions");
        }

        Map<Integer, Map<String, Integer>> pointsByYear = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getYear(),
                        Collectors.groupingBy(
                                t -> t.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                Collectors.summingInt(t -> calculatePoints(t.getAmount()))
                        )
                ));

        pointsByYear = Map.copyOf(pointsByYear);

        int totalPoints = pointsByYear.values().stream()
                .flatMap(m -> m.values().stream())
                .mapToInt(Integer::intValue)
                .sum();

        return new CustomerRewardsDTO(customer.getId(), customer.getName(), pointsByYear, totalPoints);
    }

    /**
     * Retrieves reward points for all customers in the system.
     *
     * @return a list of DTOs containing customer details and reward points
     */
    public List<CustomerRewardsDTO> getAllCustomerRewards()
    {
        // Fetch all distinct customer IDs
        List<Long> customerIds = transactionRepository.findDistinctCustomerIds();

        // reward response for each customer
        return customerIds.stream().map(this::getCustomerRewards)
                .collect( Collectors.toList());

    }

    /**
     * Retrieves reward points for a specific customer limited to the last three months.
     *
     * @param customerId the unique identifier of the customer
     * @return a DTO containing customer details and reward points for the last three months
     * @throws CustomerNotFoundException if the customer does not exist or has no transactions
     */
    public CustomerRewardsDTO getRewardsForLastThreeMonths(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        LocalDate now = LocalDate.now();
        LocalDate threeMonthsAgo = now.minusMonths(3);

        List<Transaction> transactions = transactionRepository
                .findByCustomer_IdAndTransactionDateBetween(customerId, threeMonthsAgo, now);

        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException("Customer has no transactions in last 3 months");
        }

        Map<Integer, Map<String, Integer>> pointsByYear = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getYear(),
                        Collectors.groupingBy(
                                t -> t.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                                Collectors.summingInt(t -> calculatePoints(t.getAmount()))
                        )
                ));

        int totalPoints = pointsByYear.values().stream()
                .flatMap(m -> m.values().stream())
                .mapToInt(Integer::intValue)
                .sum();

        return new CustomerRewardsDTO(customer.getId(), customer.getName(), pointsByYear, totalPoints);
    }

    /**
     * Calculates reward points for a single transaction amount
     * based on the Charter Rewards Program rules.
     *
     * @param amount the transaction amount in dollars
     * @return the total reward points earned for this transaction
     */
    private int calculatePoints(double amount) {
        int points = 0;
        if (amount > secondThreshold) {
            points += (amount - secondThreshold) * 2 + (secondThreshold - firstThreshold);
        } else if (amount > firstThreshold) {
            points += (amount - firstThreshold);
        }
        return points;
    }

}
