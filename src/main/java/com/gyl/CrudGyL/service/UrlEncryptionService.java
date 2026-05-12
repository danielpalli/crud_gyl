package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.response.EncryptedUrlResponseDto;

import java.util.List;

public interface UrlEncryptionService {

    EncryptedUrlResponseDto encryptUrl(String rawUrl);

    EncryptedUrlResponseDto encryptUrl(String rawUrl, String algorithm);

    EncryptedUrlResponseDto createEncryptedUrl(String rawUrl, String algorithm);

    EncryptedUrlResponseDto updateEncryptedUrl(String rawUrl, String algorithm);

    EncryptedUrlResponseDto decryptUrl(String rawUrl);

    String decryptRequestPath(String encryptedPath);

    List<EncryptedUrlResponseDto> listEncryptedUrls();

    EncryptedUrlResponseDto getEncryptedUrl(Long id);
}
