package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.UrlCifrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlCifradaRepository extends JpaRepository<UrlCifrada, Long> {
    Optional<UrlCifrada> findByAlgoritmoAndDescifrado(String algoritmo, String descifrado);

    Optional<UrlCifrada> findByCifrado(String cifrado);
}
