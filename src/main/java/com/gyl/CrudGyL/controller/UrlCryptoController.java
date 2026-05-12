package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.UrlCryptoRequestDto;
import com.gyl.CrudGyL.dto.response.EncryptedUrlResponseDto;
import com.gyl.CrudGyL.service.UrlEncryptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/url-crypto")
@RequiredArgsConstructor
public class UrlCryptoController {
    private final UrlEncryptionService urlEncryptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EncryptedUrlResponseDto crearUrlCifrada(@Valid @RequestBody UrlCryptoRequestDto dto) {
        return urlEncryptionService.createEncryptedUrl(dto.url(), dto.algoritmo());
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public EncryptedUrlResponseDto actualizarUrlCifrada(@Valid @RequestBody UrlCryptoRequestDto dto) {
        return urlEncryptionService.updateEncryptedUrl(dto.url(), dto.algoritmo());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EncryptedUrlResponseDto> listarUrlsCifradas() {
        return urlEncryptionService.listEncryptedUrls();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EncryptedUrlResponseDto buscarUrlCifradaPorId(@PathVariable Long id) {
        return urlEncryptionService.getEncryptedUrl(id);
    }

    @GetMapping("/encriptar")
    @ResponseStatus(HttpStatus.OK)
    public EncryptedUrlResponseDto encriptar(
            @RequestParam String url,
            @RequestParam(required = false) String algoritmo) {
        return urlEncryptionService.encryptUrl(url, algoritmo);
    }

    @GetMapping("/desencriptar")
    @ResponseStatus(HttpStatus.OK)
    public EncryptedUrlResponseDto desencriptar(@RequestParam String url) {
        return urlEncryptionService.decryptUrl(url);
    }
}
