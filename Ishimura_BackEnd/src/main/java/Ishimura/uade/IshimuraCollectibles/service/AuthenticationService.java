package Ishimura.uade.IshimuraCollectibles.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import Ishimura.uade.IshimuraCollectibles.controllers.auth.AuthenticationRequest;
import Ishimura.uade.IshimuraCollectibles.controllers.auth.AuthenticationResponse;
import Ishimura.uade.IshimuraCollectibles.controllers.auth.RegisterRequest;
import Ishimura.uade.IshimuraCollectibles.controllers.config.JwtService;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                // 1) Validación de negocio: email único
                repository.findByEmail(request.getEmail()).ifPresent(u -> {
                        throw new Ishimura.uade.IshimuraCollectibles.exceptions.UserAlreadyExistsException(request.getEmail());
                });

                var usuario = Usuario.builder()
                                .email(request.getEmail())
                                .nombre(request.getNombre())
                                .apellido(request.getApellido())
                                .direccion(request.getDireccion())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .rol(request.getRol())
                                .build();

                // 2) Persistencia con defensa por condición de carrera
                try {
                        repository.save(usuario);
                } catch (DataIntegrityViolationException e) {
                        throw new Ishimura.uade.IshimuraCollectibles.exceptions.UserAlreadyExistsException(request.getEmail());
                }
                var jwtToken = jwtService.generateToken(usuario);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .role(usuario.getRol() != null ? usuario.getRol().name() : null)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.UserNotFoundException(request.getEmail()));
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .role(user.getRol() != null ? user.getRol().name() : null)
                                .build();
        }
}
