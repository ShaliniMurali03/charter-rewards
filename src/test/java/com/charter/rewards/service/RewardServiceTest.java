package com.charter.rewards.service;

import com.charter.rewards.dto.CustomerRewardsDTO;
import com.charter.rewards.entity.Customer;
import com.charter.rewards.entity.Transaction;
import com.charter.rewards.exception.CustomerNotFoundException;
import com.charter.rewards.repository.CustomerRepository;
import com.charter.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link RewardService}.
 *
 * Verifies reward calculation logic, exception handling,
 * and retrieval of customer rewards under different scenarios.
 */
@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardService rewardService;

    /**
     * Validates that rewards are correctly calculated
     * for a customer with a valid transaction.
     */
    @Test
    void getCustomerRewards_success() {
        Customer customer = new Customer(1L, "Shalini");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(1L)).thenReturn(
                List.of(new Transaction(customer, 120.0, LocalDate.of(2026, 9, 1)))
        );

        CustomerRewardsDTO dto = rewardService.getCustomerRewards(1L);

        assertEquals(1L, dto.customerId());
        assertEquals("Shalini", dto.name());
        assertTrue(dto.totalPoints() > 0);
    }

    /**
     * Ensures that rewards can be retrieved for all customers
     * when multiple customer IDs are present.
     */
    @Test
    void getAllCustomerRewards_success() {
        Customer customer = new Customer(2L, "Murali");
        when(transactionRepository.findDistinctCustomerIds()).thenReturn(List.of(2L));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(2L)).thenReturn(
                List.of(new Transaction(customer, 200.0, LocalDate.of(2026, 8, 10)))
        );

        List<CustomerRewardsDTO> dtos = rewardService.getAllCustomerRewards();

        assertEquals(1, dtos.size());
        assertEquals("Murali", dtos.get(0).name());
    }

    /**
     * Confirms that rewards are calculated correctly
     * for transactions within the last three months.
     */
    @Test
    void getRewardsForLastThreeMonths_success() {
        Customer customer = new Customer(3L, "TestUser");
        when(customerRepository.findById(3L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomer_IdAndTransactionDateBetween(
                eq(3L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(new Transaction(customer, 350.0, LocalDate.now().minusWeeks(2))));

        CustomerRewardsDTO dto = rewardService.getRewardsForLastThreeMonths(3L);

        assertEquals("TestUser", dto.name());
        assertTrue(dto.totalPoints() > 0);
    }

    /**
     * Verifies that an exception is thrown when
     * a customer ID does not exist in the repository.
     */
    @Test
    void getCustomerRewards_customerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,
                () -> rewardService.getCustomerRewards(99L));
    }

    /**
     * Ensures that an exception is thrown when
     * a customer has no transactions recorded.
     */
    @Test
    void getCustomerRewards_noTransactions() {
        Customer customer = new Customer(1L, "Shalini");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(1L)).thenReturn(List.of());

        assertThrows(CustomerNotFoundException.class,
                () -> rewardService.getCustomerRewards(1L));
    }

    /**
     * Verifies that an exception is thrown when
     * retrieving last three months rewards for a non‑existent customer.
     */
    @Test
    void getRewardsForLastThreeMonths_customerNotFound() {
        when(customerRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,
                () -> rewardService.getRewardsForLastThreeMonths(5L));
    }

    /**
     * Ensures that an exception is thrown when
     * a customer has no transactions in the last three months.
     */
    @Test
    void getRewardsForLastThreeMonths_noTransactions() {
        Customer customer = new Customer(6L, "NoTxn");
        when(customerRepository.findById(6L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomer_IdAndTransactionDateBetween(
                eq(6L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of());

        assertThrows(CustomerNotFoundException.class,
                () -> rewardService.getRewardsForLastThreeMonths(6L));
    }
}