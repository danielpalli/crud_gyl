package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.response.EncryptedUrlResponseDto;
import com.gyl.CrudGyL.entity.UrlCifrada;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.repository.UrlCifradaRepository;
import com.gyl.CrudGyL.service.UrlEncryptionService;
import com.gyl.CrudGyL.util.adapters.UrlCryptoAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UrlEncryptionServiceImpl implements UrlEncryptionService {
    private static final List<String> ENCRYPTABLE_STATIC_PATHS = Stream.of(
            "/api/ventas/top-productos",
            "/api/ventas/resumen",
            "/api/ventas/cliente",
            "/api/ventas/rango",
            "/api/tipo-productos",
            "/api/productos/buscar",
            "/api/productos",
            "/api/clientes",
            "/api/ventas"
    )
            .sorted(Comparator.comparingInt(String::length).reversed())
            .toList();

    private final Map<String, UrlCryptoAdapter> cryptoAdapters;
    private final UrlCifradaRepository urlCifradaRepository;
    private final String defaultAlgorithm;

    public UrlEncryptionServiceImpl(
            List<UrlCryptoAdapter> cryptoAdapters,
            UrlCifradaRepository urlCifradaRepository,
            @Value("${app.crypto.default-algorithm:aes-cbc}") String defaultAlgorithm) {
        this.cryptoAdapters = cryptoAdapters.stream()
                .collect(Collectors.toMap(UrlCryptoAdapter::getAlgorithm, Function.identity()));
        this.urlCifradaRepository = urlCifradaRepository;
        this.defaultAlgorithm = defaultAlgorithm;
    }

    @Override
    @Transactional
    public EncryptedUrlResponseDto encryptUrl(String rawUrl) {
        return saveEncryptedUrl(rawUrl, defaultAlgorithm, true);
    }

    @Override
    @Transactional
    public EncryptedUrlResponseDto encryptUrl(String rawUrl, String algorithm) {
        return saveEncryptedUrl(rawUrl, algorithm, true);
    }

    @Override
    @Transactional
    public EncryptedUrlResponseDto createEncryptedUrl(String rawUrl, String algorithm) {
        return saveEncryptedUrl(rawUrl, algorithm, false);
    }

    @Override
    @Transactional
    public EncryptedUrlResponseDto updateEncryptedUrl(String rawUrl, String algorithm) {
        UrlCryptoAdapter cryptoAdapter = getAdapter(algorithm);
        PathParts pathParts = splitEncryptablePath(splitUrl(rawUrl).path());
        urlCifradaRepository.findByAlgoritmoAndDescifrado(cryptoAdapter.getAlgorithm(), pathParts.staticPath())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una URL cifrada para actualizar: " + pathParts.staticPath()));

        return saveEncryptedUrl(rawUrl, cryptoAdapter.getAlgorithm(), true);
    }

    private EncryptedUrlResponseDto saveEncryptedUrl(String rawUrl, String algorithm, boolean overwriteExisting) {
        UrlCryptoAdapter cryptoAdapter = getAdapter(algorithm);
        UrlParts urlParts = splitUrl(rawUrl);
        PathParts pathParts = splitEncryptablePath(urlParts.path());
        UrlCifrada urlCifrada = urlCifradaRepository
                .findByAlgoritmoAndDescifrado(cryptoAdapter.getAlgorithm(), pathParts.staticPath())
                .map(existingUrl -> {
                    if (!overwriteExisting) {
                        throw new ConflictException(
                                "La URL ya está cifrada. Use PATCH para regenerar el cifrado.");
                    }
                    return existingUrl;
                })
                .orElseGet(() -> UrlCifrada.builder()
                        .algoritmo(cryptoAdapter.getAlgorithm())
                        .descifrado(pathParts.staticPath())
                        .build());

        String encryptedSegment = cryptoAdapter.encrypt(pathParts.staticPath());
        urlCifrada.setCifrado(encryptedSegment);
        UrlCifrada savedUrl = urlCifradaRepository.save(urlCifrada);

        return EncryptedUrlResponseDto.builder()
                .id(savedUrl.getIdUrlCifrada())
                .algoritmo(savedUrl.getAlgoritmo())
                .devUrl(rawUrl)
                .prodUrl("/" + encryptedSegment + pathParts.suffix() + urlParts.queryString())
                .encryptedSegment(encryptedSegment)
                .decryptedStaticPath(savedUrl.getDescifrado())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public EncryptedUrlResponseDto decryptUrl(String rawUrl) {
        return decryptUrlInternal(rawUrl);
    }

    private EncryptedUrlResponseDto decryptUrlInternal(String rawUrl) {
        UrlParts urlParts = splitUrl(rawUrl);
        String path = urlParts.path();

        if (!path.startsWith("/") || path.indexOf('/', 1) == 1) {
            throw new IllegalArgumentException("La URL cifrada debe comenzar con /{segmentoCifrado}");
        }

        String encryptedAndSuffix = path.substring(1);
        int nextSlash = encryptedAndSuffix.indexOf('/');
        String encryptedSegment = nextSlash >= 0 ? encryptedAndSuffix.substring(0, nextSlash) : encryptedAndSuffix;
        String suffix = nextSlash >= 0 ? encryptedAndSuffix.substring(nextSlash) : "";
        UrlCifrada savedUrl = urlCifradaRepository.findByCifrado(encryptedSegment)
                .orElseThrow(() -> new IllegalArgumentException("No existe una URL cifrada registrada con ese valor."));

        String staticPath = getAdapter(savedUrl.getAlgoritmo()).decrypt(encryptedSegment);
        if (!savedUrl.getDescifrado().equals(staticPath)) {
            throw new IllegalArgumentException("La URL cifrada no coincide con el valor registrado.");
        }

        String devUrl = staticPath + suffix + urlParts.queryString();

        return EncryptedUrlResponseDto.builder()
                .id(savedUrl.getIdUrlCifrada())
                .algoritmo(savedUrl.getAlgoritmo())
                .devUrl(devUrl)
                .prodUrl(rawUrl)
                .encryptedSegment(encryptedSegment)
                .decryptedStaticPath(staticPath)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public String decryptRequestPath(String encryptedPath) {
        return decryptUrlInternal(encryptedPath).devUrl();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EncryptedUrlResponseDto> listEncryptedUrls() {
        return urlCifradaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EncryptedUrlResponseDto getEncryptedUrl(Long id) {
        UrlCifrada urlCifrada = urlCifradaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la URL cifrada con id: " + id));
        return toResponse(urlCifrada);
    }

    private EncryptedUrlResponseDto toResponse(UrlCifrada urlCifrada) {
        return EncryptedUrlResponseDto.builder()
                .id(urlCifrada.getIdUrlCifrada())
                .algoritmo(urlCifrada.getAlgoritmo())
                .devUrl(urlCifrada.getDescifrado())
                .prodUrl("/" + urlCifrada.getCifrado())
                .encryptedSegment(urlCifrada.getCifrado())
                .decryptedStaticPath(urlCifrada.getDescifrado())
                .build();
    }

    private UrlCryptoAdapter getAdapter(String algorithm) {
        String normalizedAlgorithm = algorithm == null || algorithm.isBlank()
                ? defaultAlgorithm
                : algorithm.toLowerCase();
        UrlCryptoAdapter adapter = cryptoAdapters.get(normalizedAlgorithm);
        if (adapter == null) {
            throw new IllegalArgumentException("Algoritmo no soportado: " + algorithm);
        }
        return adapter;
    }

    private PathParts splitEncryptablePath(String path) {
        return ENCRYPTABLE_STATIC_PATHS.stream()
                .filter(staticPath -> path.equals(staticPath) || path.startsWith(staticPath + "/"))
                .findFirst()
                .map(staticPath -> new PathParts(staticPath, path.substring(staticPath.length())))
                .orElseThrow(() -> new IllegalArgumentException("La URL no pertenece a una ruta cifrable."));
    }

    private UrlParts splitUrl(String rawUrl) {
        int queryStart = rawUrl.indexOf('?');
        String path = queryStart >= 0 ? rawUrl.substring(0, queryStart) : rawUrl;
        String queryString = queryStart >= 0 ? rawUrl.substring(queryStart) : "";
        return new UrlParts(path, queryString);
    }

    private record UrlParts(String path, String queryString) {}

    private record PathParts(String staticPath, String suffix) {}
}
