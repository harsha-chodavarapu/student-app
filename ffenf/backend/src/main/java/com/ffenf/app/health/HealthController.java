package com.ffenf.app.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

	@Value("${app.storage.path:/tmp/uploads}")
	private String storagePath;

	@GetMapping("/health")
	public ResponseEntity<Map<String, Object>> health() {
		Map<String, Object> health = new HashMap<>();
		health.put("status", "UP");
		health.put("timestamp", System.currentTimeMillis());
		
		// Check storage directories
		Map<String, Object> storage = new HashMap<>();
		String[] storagePaths = {
			storagePath,
			"/tmp/uploads",
			System.getProperty("java.io.tmpdir") + "/uploads",
			"/app/uploads"
		};
		
		for (String path : storagePaths) {
			try {
				Path testPath = Paths.get(path);
				boolean exists = Files.exists(testPath);
				boolean writable = exists && Files.isWritable(testPath);
				storage.put(path, Map.of(
					"exists", exists,
					"writable", writable
				));
			} catch (Exception e) {
				storage.put(path, Map.of(
					"exists", false,
					"writable", false,
					"error", e.getMessage()
				));
			}
		}
		
		health.put("storage", storage);
		
		// System info
		health.put("javaVersion", System.getProperty("java.version"));
		health.put("osName", System.getProperty("os.name"));
		health.put("tempDir", System.getProperty("java.io.tmpdir"));
		
		return ResponseEntity.ok(health);
	}
}


