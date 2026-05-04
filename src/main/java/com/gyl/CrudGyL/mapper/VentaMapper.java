package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.DetalleVentaResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.entity.DetalleVenta;
import com.gyl.CrudGyL.entity.Venta;
import com.gyl.CrudGyL.mapper.config.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface VentaMapper {
    @Mapping(source = "cliente.idCliente", target = "idCliente")
    @Mapping(source = "cliente", target = "nombreCliente", qualifiedByName = "combinarNombres")
    VentaResponseDto toDto(Venta entity);

    List<VentaResponseDto> toDtoList(List<Venta> listEntity);

    @Mapping(source = "producto.idProducto", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    DetalleVentaResponseDto toDetalleDto(DetalleVenta detalleEntity);

    List<DetalleVentaResponseDto> toDetalleDtoList(List<DetalleVenta> listEntity);

    @Mapping(target = "idVenta", ignore = true)
    @Mapping(target = "fechaVenta", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    void updateEntity(@MappingTarget Venta venta, VentaRequestDto dto);

    @Named("combinarNombres")
    default String combinar(Cliente cliente) {
        if (cliente == null)
            return "";
        return String.format("%s %s", cliente.getNombre(), cliente.getApellido());
    }
}
