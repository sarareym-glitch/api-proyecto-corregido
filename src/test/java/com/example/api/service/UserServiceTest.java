package com.example.api.service;

import com.example.api.model.User;
import com.example.api.model.UserRequest;
import com.example.api.model.UserResponse;
import com.example.api.repository.UserRepository;
import com.example.api.utils.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de {@link UserService}: correo único y contraseña cifrada.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequest request(String email, String password) {
        return UserRequest.builder().name("Ana").email(email).password(password).build();
    }

    @Test
    void creaUsuarioCifrandoLaContrasena() {
        when(userRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secreto1")).thenReturn("HASH");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse r = userService.createUser(request(" Ana@Example.com ", "secreto1"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("HASH", captor.getValue().getPassword());
        assertEquals("ana@example.com", r.getEmail());
        assertEquals("USER", r.getRole());
        assertFalse(r.toString().contains("HASH"));
    }

    @Test
    void correoDuplicadoAlCrearLanzaIllegalArgument() {
        when(userRepository.existsByEmail("ana@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(request("ana@example.com", "secreto1")));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void crearSinContrasenaLanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(request("ana@example.com", null)));
    }

    @Test
    void actualizarConservandoSuPropioCorreoEsPermitido() {
        User existing = User.builder().id(1L).name("Ana").email("ana@example.com").password("HASH").role("USER").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot("ana@example.com", 1L)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse r = userService.updateUser(1L, request("ana@example.com", null));

        assertEquals("ana@example.com", r.getEmail());
        assertEquals("HASH", existing.getPassword());
    }

    @Test
    void actualizarConCorreoDeOtroUsuarioLanzaIllegalArgument() {
        User existing = User.builder().id(1L).name("Ana").email("ana@example.com").password("HASH").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot("otro@example.com", 1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1L, request("otro@example.com", null)));
    }

    @Test
    void consultarInexistenteLanzaNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void eliminarInexistenteLanzaNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
    }
}
