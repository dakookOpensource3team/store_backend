package com.example.ddd_start.order.domain;

import java.util.List;

public interface CalculateRuleEngine {

  public void evalutate( List<?> facts);
}
