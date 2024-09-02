package com.duva.duva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;

@SpringBootApplication
public class DuvaApplication {

	public static void main(String[] args) {
		try {
			// Set the library path to the directory containing the jniortools.dll
			System.setProperty("java.library.path", "C:/Users/BYTEWORKS/Documents/duva/libs");

			// Force JVM to reload the library paths
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);

		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions here if necessary
		}

		SpringApplication.run(DuvaApplication.class, args);
	}
}
