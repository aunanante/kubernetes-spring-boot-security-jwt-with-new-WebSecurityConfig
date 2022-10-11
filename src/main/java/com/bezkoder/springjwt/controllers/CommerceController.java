package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.entity.Commerce;
import com.bezkoder.springjwt.entity.Ville;
import com.bezkoder.springjwt.repository.CommerceRepository;
import com.bezkoder.springjwt.repository.VilleRepository;
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
@RequestMapping("/api/commerces")
public class CommerceController {
    private final CommerceRepository commerceRepository;
    private final VilleRepository villeRepository;

    @Autowired
    public CommerceController(CommerceRepository commerceRepository, VilleRepository villeRepository) {
        this.commerceRepository = commerceRepository;
        this.villeRepository = villeRepository;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Commerce> create(@RequestBody @Valid Commerce commerce) {
        Optional<Ville> optionalVille = villeRepository.findById(commerce.getVille().getId());
        if (!optionalVille.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        commerce.setVille(optionalVille.get());

        Commerce savedCommerce = commerceRepository.save(commerce);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCommerce.getId()).toUri();

        return ResponseEntity.created(location).body(savedCommerce);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Commerce> update(@RequestBody @Valid Commerce commerce, @PathVariable Long id) {
        Optional<Ville> optionalVille = villeRepository.findById(commerce.getVille().getId());
        if (!optionalVille.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Optional<Commerce> optionalCommerce = commerceRepository.findById(id);
        if (!optionalCommerce.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        commerce.setVille(optionalVille.get());
        commerce.setId(optionalCommerce.get().getId());
        commerceRepository.save(commerce);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Commerce> delete(@PathVariable Long id) {
        Optional<Commerce> optionalCommerce = commerceRepository.findById(id);
        if (!optionalCommerce.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        commerceRepository.delete(optionalCommerce.get());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("admin/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
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

    @GetMapping("admin/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Commerce> getById(@PathVariable Long id) {
        Optional<Commerce> optionalCommerce = commerceRepository.findById(id);
        if (!optionalCommerce.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalCommerce.get());
    }

    @GetMapping("admin/Ville/{VilleId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Commerce>> getByVilleId(@PathVariable Long VilleId) {
        return ResponseEntity.ok(commerceRepository.findByVilleId(VilleId));
    }
}

