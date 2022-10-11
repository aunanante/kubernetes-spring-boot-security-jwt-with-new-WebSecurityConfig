package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.entity.Product;
import com.bezkoder.springjwt.entity.ProductCategory;
import com.bezkoder.springjwt.repository.ProductCategoryRepository;
import com.bezkoder.springjwt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductController(ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository){
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Product> create(@RequestBody @Valid Product product) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(product.getCategory().getId());
        if (!optionalProductCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        product.setCategory(optionalProductCategory.get());

        Product savedProduct = productRepository.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.getId()).toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@RequestBody @Valid Product product, @PathVariable Long id) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(product.getCategory().getId());
        if (!optionalProductCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        product.setCategory(optionalProductCategory.get());
        product.setId(optionalProduct.get().getId());
        productRepository.save(product);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> delete(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productRepository.delete(optionalProduct.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("admin/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Product>> getAllTutorials(@RequestParam(required = false) String name) {
        try {
            List<Product> products = new ArrayList<Product>();

            if (name == null)
                productRepository.findAll().forEach(products::add);
            else
                productRepository.findByNameContaining(name).forEach(products::add);

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("admin/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalProduct.get());
    }

    @GetMapping("admin/category/{categoryId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Product>> getByProductCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productRepository.findByCategoryId(categoryId));
    }

}
