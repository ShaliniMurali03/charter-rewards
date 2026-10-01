package com.charter.rewards.controller;

import com.charter.rewards.RewardsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for {@link com.charter.rewards.controller.RewardsController}.
 *
 * <p>Uses Spring Boot Test and MockMvc to verify that the REST endpoints
 * return correct responses, handle errors properly, and integrate with
 * the service and repository layers.</p>
 */
@SpringBootTest(classes = RewardsApplication.class)
@AutoConfigureMockMvc
class RewardsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Validates that the endpoint for retrieving all customer rewards
     * returns a non-empty list with valid customer details and points.
     */
    @Test
    void testGetAllCustomerRewards() throws Exception {
        mockMvc.perform(get("/api/rewards/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].customerId", notNullValue()))
                .andExpect(jsonPath("$[0].name", not(emptyString())))
                .andExpect(jsonPath("$[0].totalPoints", greaterThanOrEqualTo(0)));
    }

    /**
     * Ensures that the endpoint for a single customer returns
     * correct details and reward points for the given customer ID.
     */
    @Test
    void testGetSingleCustomerRewards() throws Exception {
        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name", is("Shalini")))
                .andExpect(jsonPath("$.totalPoints", greaterThanOrEqualTo(0)));
    }

    /**
     * Confirms that the endpoint for last three months rewards
     * returns valid reward data grouped by year and month.
     */
    @Test
    void testGetRewardsForLastThreeMonths() throws Exception {
        mockMvc.perform(get("/api/rewards/1/last-three-months"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name", is("Shalini")))
                .andExpect(jsonPath("$.pointsByYear", notNullValue()));
    }

    /**
     * Verifies that requesting rewards for a non-existent customer
     * returns a client error (404 Not Found).
     */
    @Test
    void testCustomerNotFound() throws Exception {
        mockMvc.perform(get("/api/rewards/999"))
                .andExpect(status().is4xxClientError());
    }
}
