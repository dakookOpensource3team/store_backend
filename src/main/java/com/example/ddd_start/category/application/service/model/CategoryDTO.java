package com.example.ddd_start.category.application.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

  private Long id;
  private String name;
  private String slug;
  private String image;
  private String creationAt;
  private String updatedAt;
}
