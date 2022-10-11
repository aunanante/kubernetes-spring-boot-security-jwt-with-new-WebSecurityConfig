package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByCommerceId(Long CommerceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductCategory p WHERE p.commerce.id = ?1")
    void deleteByCommerceId(Long CommerceId);

    List<ProductCategory> findByCategoryNameContaining(String categoryName);
}
