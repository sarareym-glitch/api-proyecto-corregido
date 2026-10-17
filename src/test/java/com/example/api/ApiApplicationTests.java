package com.example.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de arranque del contexto de Spring. Requiere MySQL en ejecución
 * (ver README.md: docker compose up -d).
 */
@SpringBootTest
class ApiApplicationTests {

	/** Verifica que el contexto de Spring carga correctamente. */
	@Test
	void contextLoads() {
	}

}
