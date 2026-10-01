package com.charter.rewards.controller;

import com.charter.rewards.dto.CustomerRewardsDTO;
import com.charter.rewards.service.RewardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing endpoints for the rewards program.
 *
 * <p>Endpoints:
 * - /api/rewards/all
 * - /api/rewards/{customerId}
 * - /api/rewards/{customerId}/last-three-months
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {
    private final RewardService rewardsService;

    public RewardsController(RewardService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Retrieves rewards for all customers.
     *
     * @return list of customer rewards DTOs
     */
    @GetMapping("/all")
    public List<CustomerRewardsDTO> getAllCustomerRewards() {
        return rewardsService.getAllCustomerRewards();
    }

    /**
     * Retrieves rewards for a single customer.
     *
     * @param customerId the customer ID
     * @return customer rewards DTO
     */
    @GetMapping("/{customerId}")
    public CustomerRewardsDTO getRewardsOfSingleCustomer(@PathVariable Long customerId) {
        return rewardsService.getCustomerRewards(customerId);
    }

    /**
     * Retrieves rewards for a single customer limited to the last three months.
     *
     * @param customerId the customer ID
     * @return customer rewards DTO
     */
    @GetMapping("/{customerId}/last-three-months")
    public CustomerRewardsDTO getRewardsForLastThreeMonths(@PathVariable Long customerId) {
        return rewardsService.getRewardsForLastThreeMonths(customerId);
    }

}

