package com.example.calc;


import java.util.List;
import java.util.Optional;

public interface CalculationRepo {
    Optional<Calc> findById(String id);
    List<Calc> findAll();
    void save(Calc calc);
    Optional<Calc> findLastCalculate();
}
