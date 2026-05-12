package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.LoginRequestDto;
import com.gyl.CrudGyL.dto.request.RegistroRequestDto;
import com.gyl.CrudGyL.dto.response.RegistroResponseDto;
import com.gyl.CrudGyL.dto.response.TokenResponseDto;
import com.gyl.CrudGyL.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public RegistroResponseDto registrar(@Valid @RequestBody RegistroRequestDto dto) {
        return authenticationService.registrar(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto autenticar(@Valid @RequestBody LoginRequestDto dto) {
        return authenticationService.login(dto);
    }
}
