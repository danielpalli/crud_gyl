package com.gyl.CrudGyL.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "urls_cifradas",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_url_cifrada_algoritmo_descifrado", columnNames = {"algoritmo", "descifrado"}),
                @UniqueConstraint(name = "uk_url_cifrada_cifrado", columnNames = "cifrado")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlCifrada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUrlCifrada;

    @Column(nullable = false, length = 50)
    private String algoritmo;

    @Column(nullable = false, length = 1024)
    private String cifrado;

    @Column(nullable = false, length = 500)
    private String descifrado;
}
