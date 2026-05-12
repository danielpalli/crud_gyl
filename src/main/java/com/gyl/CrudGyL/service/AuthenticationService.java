package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.LoginRequestDto;
import com.gyl.CrudGyL.dto.request.RegistroRequestDto;
import com.gyl.CrudGyL.dto.response.RegistroResponseDto;
import com.gyl.CrudGyL.dto.response.TokenResponseDto;

public interface AuthenticationService {
    RegistroResponseDto registrar(RegistroRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);
}
