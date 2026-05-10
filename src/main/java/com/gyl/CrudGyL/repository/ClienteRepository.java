package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdClienteNot(String correo, Long id);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdClienteNot(String dni, Long id);

    @Modifying
    @Query(value = "UPDATE clientes SET fecha_baja = NULL WHERE id_cliente = :id", nativeQuery = true)
    void restaurarCliente(@Param("id") Long id);
}
