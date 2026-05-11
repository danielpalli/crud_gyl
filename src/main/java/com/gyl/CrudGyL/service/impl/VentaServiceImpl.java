package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.DetalleVentaRequestDto;
import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto;
import com.gyl.CrudGyL.dto.response.TopProductoResponseDto;
import com.gyl.CrudGyL.dto.response.VentaHistorialClienteResponseDto;
import com.gyl.CrudGyL.dto.response.VentaHistorialResponseDto;
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
import com.gyl.CrudGyL.specification.VentaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public PageResponseDto<VentaResponseDto> listar(String estado, Pageable paginacion) {
        if (!List.of("todas", "activas", "anuladas").contains(estado.toLowerCase())) {
            throw new BadRequestException("Estado inválido. Use: todas, activas o anuladas.");
        }

        Page<Venta> page = repository.findAll(
                VentaSpecification.conFiltros(estado), paginacion);
        
        return construirPaginaVentas(page);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenVentasResponseDto obtenerResumen() {
        return repository.obtenerResumenGlobal();
    }

    @Override
    public VentaResponseDto buscarPorId(Long id) {
        return repository.findByIdVenta(id)
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

    @Override
    public VentaHistorialClienteResponseDto obtenerHistorialCliente(Long idCliente, Pageable paginacion) {
        clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el cliente con id: " + idCliente));

        Page<Venta> page = repository.findAll(
                VentaSpecification.porCliente(idCliente), paginacion);

        return VentaHistorialClienteResponseDto.builder()
                .ventas(construirPaginaVentas(page))
                .resumen(repository.obtenerResumenCliente(idCliente))
                .build();
    }

    @Override
    public VentaHistorialResponseDto obtenerVentasPorRango(LocalDate inicio, LocalDate fin, Pageable paginacion) {
        LocalDate fechaInicio = (inicio != null) ? inicio : LocalDate.now();
        LocalDate fechaFin = (fin != null) ? fin : LocalDate.now();

        if (fechaInicio.isAfter(fechaFin)) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        Page<Venta> page = repository.findAll(
                VentaSpecification.porRangoFechas(fechaInicio, fechaFin), paginacion);

        return VentaHistorialResponseDto.builder()
                .ventas(construirPaginaVentas(page))
                .resumen(repository.obtenerResumenPorRango(fechaInicio, fechaFin))
                .build();
    }

    @Override
    public List<TopProductoResponseDto> obtenerTopProductosMasVendidos() {
        return repository.obtenerTopProductosMasVendidos(PageRequest.of(0, 5));
    }

    private List<DetalleVenta> construirDetalle(List<DetalleVentaRequestDto> detalleDtos, Venta venta) {
        Set<Long> idsProductos = detalleDtos.stream()
                .map(DetalleVentaRequestDto::idProducto)
                .collect(Collectors.toSet());

        if (idsProductos.size() < detalleDtos.size()) {
            throw new BadRequestException("No se pueden incluir productos duplicados en una misma venta");
        }

        Map<Long, Producto> productos = productoRepository.findAllById(idsProductos).stream()
                .collect(Collectors.toMap(Producto::getIdProducto, Function.identity()));

        if (productos.size() != idsProductos.size()) {
            Long idFaltante = idsProductos.stream()
                    .filter(id -> !productos.containsKey(id))
                    .findFirst()
                    .orElse(null);
            throw new ResourceNotFoundException("Producto no encontrado con id: " + idFaltante);
        }

        return detalleDtos.stream()
                .map(detalleDto -> {
                    Producto producto = productos.get(detalleDto.idProducto());

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

    private PageResponseDto<VentaResponseDto> construirPaginaVentas(Page<Venta> page) {
        return PageResponseDto.<VentaResponseDto>builder()
                .contenido(mapearVentasCompletas(page.getContent()))
                .numeroPagina(page.getNumber())
                .tamanioPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .esUltima(page.isLast())
                .build();
    }

    private List<VentaResponseDto> mapearVentasCompletas(List<Venta> ventas) {
        if (ventas.isEmpty()) {
            return List.of();
        }

        List<Long> ids = ventas.stream()
                .map(Venta::getIdVenta)
                .toList();

        Map<Long, Venta> ventasCompletas = repository.findDistinctByIdVentaIn(ids).stream()
                .collect(Collectors.toMap(Venta::getIdVenta, Function.identity(), (actual, repetida) -> actual));

        return ids.stream()
                .map(ventasCompletas::get)
                .map(mapper::toDto)
                .toList();
    }
}
