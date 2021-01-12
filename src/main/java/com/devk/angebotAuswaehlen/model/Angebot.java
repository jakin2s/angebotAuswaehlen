package com.devk.angebotAuswaehlen.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Builder
@Setter@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Angebot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String beschreibung;

    private String adresse;

    private BigDecimal einzelpreis;

    private BigDecimal gesamtpreis;


}