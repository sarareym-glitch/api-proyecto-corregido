package com.example.api.service;

import com.example.api.model.User;
import com.example.api.model.UserRequest;
import com.example.api.model.UserResponse;
import com.example.api.repository.UserRepository;
import com.example.api.utils.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio del módulo de usuarios: CRUD, correo único y
 * contraseñas cifradas con BCrypt. Nunca registra ni devuelve contraseñas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lista todos los usuarios (sin contraseña).
     *
     * @return lista de usuarios
     */
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    /**
     * Consulta un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return el usuario encontrado (sin contraseña)
     * @throws UserNotFoundException si no existe
     */
    public UserResponse getUserById(Long id) {
        return UserResponse.from(findOrThrow(id));
    }

    /**
     * Crea un usuario nuevo verificando que el correo no exista.
     *
     * @param request datos del usuario (la contraseña es obligatoria)
     * @return el usuario guardado (sin contraseña)
     * @throws IllegalArgumentException si el correo ya existe o falta la contraseña
     */
    public UserResponse createUser(UserRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo " + email);
        }
        log.info("Creando usuario con correo {}", email);
        User user = User.builder()
                .name(request.getName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleOrDefault(request.getRole()))
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    /**
     * Actualiza un usuario existente. Permite conservar su propio correo,
     * pero rechaza el correo de otro usuario. Si no se envía contraseña, se conserva la actual.
     *
     * @param id      identificador del usuario a actualizar
     * @param request nuevos datos
     * @return el usuario actualizado (sin contraseña)
     * @throws UserNotFoundException    si no existe
     * @throws IllegalArgumentException si el correo pertenece a otro usuario
     */
    public UserResponse updateUser(Long id, UserRequest request) {
        User existing = findOrThrow(id);
        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo " + email);
        }
        log.info("Actualizando usuario con id {}", id);
        existing.setName(request.getName().trim());
        existing.setEmail(email);
        existing.setRole(roleOrDefault(request.getRole()));
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return UserResponse.from(userRepository.save(existing));
    }

    /**
     * Elimina un usuario.
     *
     * @param id identificador del usuario a eliminar
     * @throws UserNotFoundException si no existe
     */
    public void deleteUser(Long id) {
        User existing = findOrThrow(id);
        log.info("Eliminando usuario con id {}", id);
        userRepository.delete(existing);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id " + id));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String roleOrDefault(String role) {
        return (role == null || role.isBlank()) ? DEFAULT_ROLE : role.trim().toUpperCase();
    }
}
