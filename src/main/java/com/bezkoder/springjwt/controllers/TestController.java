package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.entity.Commerce;
import com.bezkoder.springjwt.entity.Product;
import com.bezkoder.springjwt.entity.ProductCategory;
import com.bezkoder.springjwt.entity.Ville;
import com.bezkoder.springjwt.repository.CommerceRepository;
import com.bezkoder.springjwt.repository.ProductCategoryRepository;
import com.bezkoder.springjwt.repository.ProductRepository;
import com.bezkoder.springjwt.repository.VilleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    private final VilleRepository villeRepository;
    private final CommerceRepository commerceRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    public TestController(ProductRepository productRepository,
                          ProductCategoryRepository productCategoryRepository,
                          CommerceRepository commerceRepository,
                          VilleRepository villeRepository) {
        this.commerceRepository = commerceRepository;
        this.villeRepository = villeRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/all/villes/{id}")
    public ResponseEntity<Ville> getById_Vil(@PathVariable Long id) {
        Optional<Ville> optionalVille = villeRepository.findById(id);
        if (!optionalVille.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalVille.get());
    }

    @GetMapping("/all/commerces/{id}")
    public ResponseEntity<Commerce> getById_Com(@PathVariable Long id) {
        Optional<Commerce> optionalCommerce = commerceRepository.findById(id);
        if (!optionalCommerce.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalCommerce.get());
    }

    @GetMapping("/all/categories/{id}")
    public ResponseEntity<ProductCategory> getById_Cat(@PathVariable Long id) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
        if (!optionalProductCategory.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalProductCategory.get());
    }

    @GetMapping("/all/products/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalProduct.get());
    }

    @GetMapping("/all/villes")
    public ResponseEntity<List<Ville>> getAllVilles(@RequestParam(required = false) String villeName) {
        try {
            List<Ville> villes = new ArrayList<Ville>();

            if (villeName == null)
                villeRepository.findAll().forEach(villes::add);
            else
                villeRepository.findByVilleNameContaining(villeName).forEach(villes::add);

            if (villes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(villes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/commerces")
    public ResponseEntity<List<Commerce>> getAllCommerces(@RequestParam(required = false) String commerceName) {
        try {
            List<Commerce> commerces = new ArrayList<Commerce>();

            if (commerceName == null)
                commerceRepository.findAll().forEach(commerces::add);
            else
                commerceRepository.findByCommerceNameContaining(commerceName).forEach(commerces::add);

            if (commerces.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(commerces, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/categories")
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

    @GetMapping("/all/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String name) {
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

    @GetMapping("/all/villes_commerces/{VilleId}")
    public ResponseEntity<List<Commerce>> getByVilleId(@PathVariable Long VilleId) {
        return ResponseEntity.ok(commerceRepository.findByVilleId(VilleId));
    }

    @GetMapping("/all/commerces_categories/{commerceId}")
    public ResponseEntity<List<ProductCategory>> getByCommerceId(@PathVariable Long commerceId) {
        return ResponseEntity.ok(productCategoryRepository.findByCommerceId(commerceId));
    }

    @GetMapping("/all/categories_products/{categoryId}")
    public ResponseEntity<List<Product>> getByProductCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productRepository.findByCategoryId(categoryId));
    }

}
