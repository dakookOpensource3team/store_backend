package com.example.ddd_start.product.application.service.event;

import com.example.ddd_start.product.application.service.FetchProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FetchProductEventHandler {

  private final FetchProductService fetchProductService;

  @Async
  @EventListener(ApplicationReadyEvent.class)
  public void fetchProduct() {
    fetchProductService.fetchProducts();
  }
}
