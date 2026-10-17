package com.example.api.utils;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción base para recursos que no existen. Se traduce a HTTP 404.
 * Lombok ({@code @StandardException}) genera los constructores estándar.
 */
@StandardException
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
}
