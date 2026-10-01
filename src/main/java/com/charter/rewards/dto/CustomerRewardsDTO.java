package com.charter.rewards.dto;

import java.util.Map;

public record CustomerRewardsDTO(
        Long customerId,
        String name,
        Map<Integer, Map<String, Integer>> pointsByYear,
        int totalPoints
) {
    public CustomerRewardsDTO {
        pointsByYear = Map.copyOf(pointsByYear);
    }
}