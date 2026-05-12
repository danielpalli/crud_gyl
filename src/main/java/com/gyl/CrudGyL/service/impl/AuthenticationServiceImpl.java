package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.LoginRequestDto;
import com.gyl.CrudGyL.dto.request.RegistroRequestDto;
import com.gyl.CrudGyL.dto.response.RegistroResponseDto;
import com.gyl.CrudGyL.dto.response.TokenResponseDto;
import com.gyl.CrudGyL.entity.Usuario;
import com.gyl.CrudGyL.mapper.UsuarioMapper;
import com.gyl.CrudGyL.repository.UsuarioRepository;
import com.gyl.CrudGyL.security.TokenService;
import com.gyl.CrudGyL.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public final UsuarioMapper mapper;

    @Override
    public RegistroResponseDto registrar(RegistroRequestDto dto) {
        String password = passwordEncoder.encode(dto.password());

        Usuario usuario = mapper.toEntity(dto, password);

        usuarioRepository.save(usuario);

        return new RegistroResponseDto(usuario.getUsername(), usuario.getPassword());
    }

    @Override
    public TokenResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.username(),dto.password())
        );

        UserDetails usuario = usuarioRepository.findByUsername(dto.username())
            .orElseThrow();

        String token = tokenService.getToken(usuario);
        return new TokenResponseDto(token);
    }
}
