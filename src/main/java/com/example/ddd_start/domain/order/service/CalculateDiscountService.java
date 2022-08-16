package com.example.ddd_start.domain.order.service;

import com.example.ddd_start.domain.order.calculate_rule_engine.CalculateRuleEngine;
import com.example.ddd_start.domain.common.Money;
import com.example.ddd_start.domain.customer.Customer;
import com.example.ddd_start.domain.customer.CustomerRepository;
import com.example.ddd_start.domain.order.OrderLine;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateDiscountService {

  private final CalculateRuleEngine calculateRuleEngine;
  private final CustomerRepository customerRepository;

  public void calculateDiscount(List<OrderLine> orderLines, Long customerId) {
    Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
    Customer customer = optionalCustomer.orElseThrow(NoSuchElementException::new);

    List<?> facts = Arrays.asList(customer, new Money());
    calculateRuleEngine.evalutate(facts);
  }
}
