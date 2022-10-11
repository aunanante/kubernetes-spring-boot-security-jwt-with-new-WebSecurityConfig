package com.bezkoder.springjwt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="commerce")
public class Commerce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @NotNull
    @Column(name = "commerce_Name")
    private String commerceName;

    @Column(name = "proprietaire_Name")
    private String proprietaireName;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "Telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "transfert")
    private float transfert;

    @Column(name = "date_transfert")
    private String date_transfert;

    @Column(name = "type_transfert")
    private String type_transfert;

    @Column(name = "payed")
    private boolean payed;

    @Column(name = "date_peremption")
    private String date_peremption;

    @Column(name = "presentation")
    private String presentation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Ville ville;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getProprietaireName() {
        return proprietaireName;
    }

    public void setProprietaireName(String proprietaireName) {
        this.proprietaireName = proprietaireName;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public float getTransfert() {
        return transfert;
    }

    public void setTransfert(float transfert) {
        this.transfert = transfert;
    }

    public String getDate_transfert() {
        return date_transfert;
    }

    public void setDate_transfert(String date_transfert) {
        this.date_transfert = date_transfert;
    }

    public String getType_transfert() {
        return type_transfert;
    }

    public void setType_transfert(String type_transfert) {
        this.type_transfert = type_transfert;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public String getDate_peremption() {
        return date_peremption;
    }

    public void setDate_peremption(String date_peremtion) {
        this.date_peremption = date_peremtion;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}
