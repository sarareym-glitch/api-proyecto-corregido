package com.example.api.controller;

import com.example.api.model.UserRequest;
import com.example.api.model.UserResponse;
import com.example.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST de usuarios. Ruta base: {@code /api/usuarios}.
 * Ninguna respuesta incluye la contraseña.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/usuarios: lista todos los usuarios.
     *
     * @return HTTP 200 con la lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * GET /api/usuarios/{id}: consulta un usuario.
     *
     * @param id identificador del usuario
     * @return HTTP 200 con el usuario, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * POST /api/usuarios: crea un usuario.
     *
     * @param request cuerpo JSON validado
     * @return HTTP 201 con el usuario creado, o 400 si el correo ya existe
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    /**
     * PUT /api/usuarios/{id}: actualiza un usuario.
     *
     * @param id      identificador del usuario
     * @param request cuerpo JSON validado
     * @return HTTP 200 con el usuario actualizado, 404 si no existe o 400 si el correo es de otro usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    /**
     * DELETE /api/usuarios/{id}: elimina un usuario.
     *
     * @param id identificador del usuario
     * @return HTTP 204 sin contenido, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
