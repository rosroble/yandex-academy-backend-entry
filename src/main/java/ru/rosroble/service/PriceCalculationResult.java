package ru.rosroble.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class PriceCalculationResult {
    @Getter
    @Setter
    private Long count;
    @Getter
    @Setter
    private Long sum;


    public void add(PriceCalculationResult result) {
        if (result == null) return;
        this.count += result.count;
        this.sum += result.sum;
    }

    public void incrementCount(Long inc) {
        count += inc;
    }

    public void incrementSum(Long inc) {
        sum += inc;
    }

}
