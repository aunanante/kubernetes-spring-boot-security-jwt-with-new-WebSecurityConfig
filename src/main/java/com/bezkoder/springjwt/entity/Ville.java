package com.bezkoder.springjwt.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="ville")
public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @NotNull
    //@Column(name = "ville_name")
    @Column(unique=true)
    private String villeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVilleName() {
        return villeName;
    }

    public void setVilleName(String villeName) {
        this.villeName = villeName;
    }
}
