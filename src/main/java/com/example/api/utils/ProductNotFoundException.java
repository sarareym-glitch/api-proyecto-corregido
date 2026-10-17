package com.example.api.utils;

import lombok.experimental.StandardException;

/**
 * Se lanza cuando no existe un producto con el identificador solicitado (HTTP 404).
 */
@StandardException
public class ProductNotFoundException extends ResourceNotFoundException {
}
