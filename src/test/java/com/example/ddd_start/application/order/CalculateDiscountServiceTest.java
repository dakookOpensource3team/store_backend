package com.example.ddd_start.application.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.ddd_start.domain.customer.Customer;
import com.example.ddd_start.domain.customer.CustomerRepository;
import com.example.ddd_start.domain.order.OrderLine;
import com.example.ddd_start.domain.order.OrderRepository;
import com.example.ddd_start.infrastructure.calculate_rule_engine.CalculateRuleEngine;
import com.example.ddd_start.infrastructure.calculate_rule_engine.DroolsRuleEngine;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class CalculateDiscountServiceTest {

  private CustomerRepository customerRepository;
  private CalculateRuleEngine calculateRuleEngine;
  private CalculateDiscountService calculateDiscountService;

  @BeforeEach
  public void init() {
    customerRepository = mock(CustomerRepository.class);
    calculateRuleEngine = new DroolsRuleEngine();

    when(customerRepository.findById(1L))
        .thenReturn(Optional.of(new Customer(1L, "taewoo")));

    calculateDiscountService = new CalculateDiscountService(calculateRuleEngine,
        customerRepository);
  }

  @Test
  public void CalculateDiscountTest() {
    List<OrderLine> orderLines = Collections.singletonList(new OrderLine());
    calculateDiscountService.calculateDiscount(orderLines, 1L);
  }
}