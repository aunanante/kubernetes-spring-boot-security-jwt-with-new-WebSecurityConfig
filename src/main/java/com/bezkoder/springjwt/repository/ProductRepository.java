package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.category.id = ?1")
    void deleteByproductCategoryId(Long categoryId);

    List<Product> findByNameContaining(String name);
}
