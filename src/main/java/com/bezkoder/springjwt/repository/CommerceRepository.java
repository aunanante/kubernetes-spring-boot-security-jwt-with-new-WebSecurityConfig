package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.entity.Commerce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface CommerceRepository extends JpaRepository<Commerce, Long>{
    List<Commerce> findByVilleId(Long villeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Commerce c WHERE c.ville.id = ?1")
    void deleteByVilleId(Long villeId);

    List<Commerce> findByCommerceNameContaining(String commerceName);
}
