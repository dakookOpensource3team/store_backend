package com.example.ddd_start.infrastructure.calculate_rule_engine;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DroolsRuleEngine implements CalculateRuleEngine{

  public void evalutate(List<?> facts) {
    final String session = "droolsRuleSession";
    log.info(session + "를 이용하여 할인 금액을 계산합니다.");
  }
}
