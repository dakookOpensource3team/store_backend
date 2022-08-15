package com.example.ddd_start.infrastructure.money.moeny_converter;

import com.example.ddd_start.domain.common.Money;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter()
public class MoneyConverter implements AttributeConverter<Money, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Money money) {
    return money == null ? null : money.getValue();
  }

  @Override
  public Money convertToEntityAttribute(Integer value) {
    return value == null ? null : new Money(value);
  }
}
