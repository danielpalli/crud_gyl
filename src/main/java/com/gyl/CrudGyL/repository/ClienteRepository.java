package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdClienteNot(String correo, Long id);
}
