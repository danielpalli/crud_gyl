package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.DetalleVentaRequestDto;
import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.entity.DetalleVenta;
import com.gyl.CrudGyL.entity.Producto;
import com.gyl.CrudGyL.entity.Venta;
import com.gyl.CrudGyL.exception.BadRequestException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.mapper.VentaMapper;
import com.gyl.CrudGyL.repository.ClienteRepository;
import com.gyl.CrudGyL.repository.ProductoRepository;
import com.gyl.CrudGyL.repository.VentaRepository;
import com.gyl.CrudGyL.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VentaServiceImpl implements VentaService {
    private final VentaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper mapper;

    @Override
    @Transactional
    public VentaResponseDto crear(VentaRequestDto dto) {
        Cliente cliente = clienteRepository.findById(dto.idCliente())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No se encontró el cliente con id: " + dto.idCliente()));

        Venta venta = Venta.builder()
                .fechaVenta(LocalDate.now())
                .cliente(cliente)
                .build();

        List<DetalleVenta> detalles = construirDetalle(dto.detalles(), venta);

        venta.setDetalles(detalles);
        venta.setTotal(calcularTotal(detalles));

        Venta nuevaVenta = repository.save(venta);
        return mapper.toDto(nuevaVenta);
    }

    @Override
    public List<VentaResponseDto> listar(String estado) {
        return mapper.toDtoList(switch (estado.toLowerCase()) {
            case "activas" -> repository.findByFechaAnulacionIsNull();
            case "anuladas" -> repository.findByFechaAnulacionIsNotNull();
            case "todas" -> repository.findAll();
            default -> throw new BadRequestException("Estado inválido. Use: todas, activas o anuladas.");
        });
    }

    @Override
    public VentaResponseDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));
    }

    @Override
    @Transactional
    public VentaResponseDto actualizar(Long id, VentaRequestDto dto) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        Cliente cliente = clienteRepository.findById(dto.idCliente())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No se encontró el cliente con id: " + dto.idCliente()));

        venta.getDetalles().forEach(detalle -> {
            Producto p = detalle.getProducto();
            p.setStock(p.getStock() + detalle.getCantidad());
        });

        List<DetalleVenta> nuevosDetalles = construirDetalle(dto.detalles(), venta);

        venta.setCliente(cliente);
        venta.getDetalles().clear();
        venta.getDetalles().addAll(nuevosDetalles);
        venta.setTotal(calcularTotal(nuevosDetalles));

        Venta ventaActualizada = repository.save(venta);
        return mapper.toDto(ventaActualizada);
    }

    @Override
    @Transactional
    public EstadoResponseDto anular(Long id) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        venta.getDetalles().forEach(detalle -> {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
        });

        venta.setFechaAnulacion(Instant.now());

        return EstadoResponseDto.builder()
                .id(venta.getIdVenta())
                .nombre("Venta #" + venta.getIdVenta())
                .mensaje("La venta fue anulada correctamente")
                .estado("anulada")
                .build();
    }

    private List<DetalleVenta> construirDetalle(List<DetalleVentaRequestDto> detalleDtos, Venta venta) {
       long productosUnicos = detalleDtos.stream()
               .map(DetalleVentaRequestDto::idProducto)
               .distinct()
               .count();

       if (productosUnicos < detalleDtos.size()) {
           throw new BadRequestException("No se pueden incluir productos duplicados en una misma venta");
       }

        return detalleDtos.stream()
                .map(detalleDto -> {
                    Producto producto = productoRepository.findById(detalleDto.idProducto())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Producto no encontrado con id: " + detalleDto.idProducto()));

                    if (producto.getStock() < detalleDto.cantidad()) {
                        throw new BadRequestException("No hay stock suficiente para el producto: "
                                + producto.getNombreProducto() + " (Stock actual: " + producto.getStock() + ")");
                    }

                    producto.setStock(producto.getStock() - detalleDto.cantidad());

                    return DetalleVenta.builder()
                            .venta(venta)
                            .producto(producto)
                            .cantidad(detalleDto.cantidad())
                            .precioUnitario(producto.getPrecio())
                            .subtotal(producto.getPrecio() * detalleDto.cantidad())
                            .build();
                }).toList();
    }

    private Double calcularTotal(List<DetalleVenta> detalles) {
        return detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
    }
}
