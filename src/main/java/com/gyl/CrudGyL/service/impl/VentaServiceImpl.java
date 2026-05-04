package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
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
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el cliente con id: " + dto.idCliente()));

        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setCliente(cliente);

        List<DetalleVenta> detalles = procesarDetalles(dto, venta);

        venta.setDetalles(detalles);
        venta.setTotal(calcularTotal(detalles));

        Venta nuevaVenta = repository.save(venta);
        return mapper.toDto(nuevaVenta);
    }

    @Override
    public List<VentaResponseDto> listar() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public VentaResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));
    }

    @Override
    @Transactional
    public VentaResponseDto actualizar(Long id, VentaRequestDto dto) {
        Venta venta = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));

        Cliente cliente = clienteRepository.findById(dto.idCliente())
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el cliente con id: " + dto.idCliente()));

        restaurarStock(venta.getDetalles());

        venta.getDetalles().clear();

        List<DetalleVenta> nuevosDetalles = procesarDetalles(dto, venta);

        venta.setCliente(cliente);
        venta.getDetalles().addAll(nuevosDetalles);
        venta.setTotal(calcularTotal(nuevosDetalles));

        Venta ventaActualizada = repository.save(venta);
        return mapper.toDto(ventaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Venta venta = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));

        restaurarStock(venta.getDetalles());

        repository.delete(venta);
    }

    private List<DetalleVenta> procesarDetalles(VentaRequestDto dto, Venta venta) {
        return dto.detalles().stream()
            .map(detalleDto -> {
                Producto producto = productoRepository.findById(detalleDto.idProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + detalleDto.idProducto()));

                if (producto.getStock() < detalleDto.cantidad()) {
                    throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
                }

                producto.setStock(producto.getStock() - detalleDto.cantidad());
                productoRepository.save(producto);

                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDto.cantidad());
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setSubtotal(producto.getPrecio() * detalleDto.cantidad());
                detalle.setVenta(venta);
                return detalle;
            }).toList();
    }

    private void restaurarStock(List<DetalleVenta> detalles) {
        detalles.forEach(detalle -> {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        });
    }

    private Double calcularTotal(List<DetalleVenta> detalles) {
        return detalles.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
    }
}
