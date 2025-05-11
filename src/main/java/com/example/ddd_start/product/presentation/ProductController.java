package com.example.ddd_start.product.presentation;

import com.example.ddd_start.product.application.service.DeleteProductService;
import com.example.ddd_start.product.application.service.FetchProductService;
import com.example.ddd_start.product.application.service.LastlyRetrieveProductService;
import com.example.ddd_start.product.application.service.PrintProductService;
import com.example.ddd_start.product.application.service.RegisterProductService;
import com.example.ddd_start.product.application.service.model.NewProductRequest;
import com.example.ddd_start.product.application.service.model.ProductDTO;
import com.example.ddd_start.product.application.service.model.UpdateProductRequest;
import com.example.ddd_start.product.presentation.model.RegisterProductCommand;
import com.example.ddd_start.product.presentation.model.RegisterProductResponse;
import com.example.ddd_start.product.presentation.model.SaveLastlyProductRequest;
import com.example.ddd_start.product.presentation.model.UpdateProductCommand;
import com.example.ddd_start.product.presentation.model.UpdateProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

  private final PrintProductService printProductService;
  private final DeleteProductService deleteProductService;
  private final RegisterProductService registerProductService;
  private final FetchProductService fetchProductService;
  private final LastlyRetrieveProductService lastlyRetrieveProductService;

  @GetMapping("/products")
  public ResponseEntity printAllProducts() {
    List<ProductDTO> products = printProductService.printAllProducts();
    return new ResponseEntity(
        products,
        HttpStatus.ACCEPTED
    );
  }

  @GetMapping("/products/{productId}")
  public ResponseEntity printProductById(@PathVariable Long productId) {
    ProductDTO product = printProductService.printProductById(productId);
    return new ResponseEntity(
        product,
        HttpStatus.ACCEPTED
    );
  }

  @PostMapping("/products")
  public ResponseEntity registerProduct(@RequestBody RegisterProductCommand cmd) {
    NewProductRequest req = NewProductRequest.builder()
        .title(cmd.title())
        .slug(cmd.slug())
        .price(cmd.price())
        .description(cmd.description())
        .categoryId(cmd.categoryId())
        .images(cmd.images())
        .build();

    Long productId = registerProductService.registerProductNoStore(req);
    return new ResponseEntity(
        new RegisterProductResponse(productId, "상품이 정상적으로 등록되었습니다."),
        HttpStatus.ACCEPTED
    );
  }

  @PutMapping("/products")
  public ResponseEntity<UpdateProductResponse> updateProduct(
      @RequestBody UpdateProductCommand cmd) {
    UpdateProductRequest req = UpdateProductRequest.builder()
        .productId(cmd.productId())
        .title(cmd.title())
        .slug(cmd.slug())
        .price(cmd.price())
        .description(cmd.description())
        .categoryId(cmd.categoryId())
        .images(cmd.images())
        .build();

    Long productId = registerProductService.updateProduct(req);
    return new ResponseEntity(
        new UpdateProductResponse(productId, "상품 변경이 완료되었습니다."),
        HttpStatus.ACCEPTED
    );
  }

  @DeleteMapping("/products")
  public ResponseEntity deleteProduct(@RequestParam Long productId) {
    deleteProductService.delete(productId);
    return new ResponseEntity("상품이 정상적으로 삭제되었습니다.", HttpStatus.ACCEPTED);
  }

  @PostMapping("/products/lastly")
  public ResponseEntity saveLastlyRetrievedProduct(@RequestBody SaveLastlyProductRequest req) {
    lastlyRetrieveProductService.saveLastlyRetrieveProduct(req.memberId(), req.productId());
    return new ResponseEntity(
        HttpStatus.ACCEPTED
    );
  }

  @GetMapping("/products/lastly")
  public ResponseEntity printLastlyRetrievedProduct(@RequestParam Long memberId) {
    List<ProductDTO> productDTOS = lastlyRetrieveProductService.printLastlyRetrieveProduct(
        memberId);
    return new ResponseEntity(
        productDTOS,
        HttpStatus.ACCEPTED
    );
  }
}

