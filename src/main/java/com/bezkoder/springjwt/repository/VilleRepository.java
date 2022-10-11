package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.entity.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("http://localhost:4200")
public interface VilleRepository extends JpaRepository<Ville, Long>{
    List<Ville> findByVilleNameContaining(String villeName);
}