package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.entity.Ville;
import com.bezkoder.springjwt.repository.CommerceRepository;
import com.bezkoder.springjwt.repository.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/villes")
public class VilleController {

    private final VilleRepository villeRepository;
    private final CommerceRepository commerceRepository;

    @Autowired
    public VilleController(VilleRepository villeRepository, CommerceRepository commerceRepository){
        this.villeRepository = villeRepository;
        this.commerceRepository = commerceRepository;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ville> create(@Valid @RequestBody Ville ville) {
        Ville savedVille = villeRepository.save(ville);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedVille.getId()).toUri();

        return ResponseEntity.created(location).body(savedVille);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Ville> update(@PathVariable Long id, @Valid @RequestBody Ville ville) {
        Optional<Ville> optionalVille = villeRepository.findById(id);
        if (!optionalVille.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        ville.setId(optionalVille.get().getId());
        villeRepository.save(ville);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ville> delete(@PathVariable Long id) {
        Optional<Ville> optionalVille = villeRepository.findById(id);
        if (!optionalVille.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        deleteVilleInTransaction(optionalVille.get());
        return ResponseEntity.noContent().build();
    }

    @Transactional

    public void deleteVilleInTransaction(Ville ville) {
        commerceRepository.deleteByVilleId(ville.getId());
        villeRepository.delete(ville);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Ville> getById(@PathVariable Long id) {
        Optional<Ville> optionalVille = villeRepository.findById(id);
        if (!optionalVille.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalVille.get());
    }

    @GetMapping("/admin/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
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
}
