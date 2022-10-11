package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.entity.Commerce;
import com.bezkoder.springjwt.entity.ProductCategory;
import com.bezkoder.springjwt.repository.CommerceRepository;
import com.bezkoder.springjwt.repository.ProductCategoryRepository;
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
@RequestMapping("/api/categories")
public class ProductCategoryController {
    private final ProductCategoryRepository productCategoryRepository;
    private final CommerceRepository commerceRepository;

    @Autowired
    public ProductCategoryController(ProductCategoryRepository productCategoryRepository,
                                     CommerceRepository commerceRepository){
        this.commerceRepository = commerceRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductCategory> create(@RequestBody @Valid ProductCategory productCategory) {
        Optional<Commerce> optionalCommerce = commerceRepository.findById(productCategory.getCommerce().getId());
        if (!optionalCommerce.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productCategory.setCommerce(optionalCommerce.get());

        ProductCategory savedproductCategory = productCategoryRepository.save(productCategory);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedproductCategory.getId()).toUri();

        return ResponseEntity.created(location).body(savedproductCategory);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductCategory> update(@RequestBody @Valid ProductCategory productCategory, @PathVariable Long id) {
        Optional<Commerce> optionalCommerce = commerceRepository.findById(productCategory.getCommerce().getId());
        if (!optionalCommerce.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
        if (!optionalProductCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productCategory.setCommerce(optionalCommerce.get());
        productCategory.setId(optionalProductCategory.get().getId());
        productCategoryRepository.save(productCategory);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductCategory> delete(@PathVariable Long id) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
        if (!optionalProductCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productCategoryRepository.delete(optionalProductCategory.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("admin/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductCategory>> getAllProductCategories(@RequestParam(required = false) String productCategoryName) {
        try {
            List<ProductCategory> productCategories = new ArrayList<ProductCategory>();

            if (productCategoryName == null)
                productCategoryRepository.findAll().forEach(productCategories::add);
            else
                productCategoryRepository.findByCategoryNameContaining(productCategoryName).forEach(productCategories::add);

            if (productCategories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(productCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("admin/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ProductCategory> getById(@PathVariable Long id) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
        if (!optionalProductCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalProductCategory.get());
    }

    @GetMapping("admin/commerce/{commerceId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductCategory>> getByCommerceId(@PathVariable Long commerceId) {
        return ResponseEntity.ok(productCategoryRepository.findByCommerceId(commerceId));
    }
}
