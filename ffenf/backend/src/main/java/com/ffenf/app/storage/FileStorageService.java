package com.ffenf.app.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${app.storage.path:./uploads}")
    private String storagePath;

    public String storeFile(MultipartFile file) throws IOException {
        // Create storage directory if it doesn't exist
        Path uploadDir = Paths.get(storagePath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // Store file
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public Path getFilePath(String filename) {
        return Paths.get(storagePath).resolve(filename);
    }

    public boolean fileExists(String filename) {
        return Files.exists(getFilePath(filename));
    }

    public void deleteFile(String filename) throws IOException {
        Path filePath = getFilePath(filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    // AskHub specific methods
    public String storeQuestionImage(MultipartFile image, UUID userId) throws IOException {
        validateImageFile(image);
        return storeFile(image, "questions", userId);
    }

    public String storeAnswerImage(MultipartFile image, UUID userId) throws IOException {
        validateImageFile(image);
        return storeFile(image, "answers", userId);
    }

    private void validateImageFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Image file is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File is not an image");
        }

        // Check file size (max 5MB for images)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IOException("Image file is too large (max 5MB)");
        }

        // Validate image extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("Invalid filename");
        }
        
        Pattern imagePattern = Pattern.compile(".*\\.(jpg|jpeg|png|gif|webp)$", Pattern.CASE_INSENSITIVE);
        if (!imagePattern.matcher(originalFilename).matches()) {
            throw new IOException("Invalid image format. Supported: JPG, PNG, GIF, WebP");
        }
    }

    public String storeFile(MultipartFile file, String subdirectory, UUID userId) throws IOException {
        Path uploadDir = Paths.get(storagePath, subdirectory, userId.toString());
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension;

        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return subdirectory + "/" + userId + "/" + filename;
    }

    public byte[] getImage(String filename) throws IOException {
        Path filePath = Paths.get(storagePath).resolve(filename);
        return Files.readAllBytes(filePath);
    }

    public String getImageContentType(String filename) throws IOException {
        Path filePath = Paths.get(storagePath).resolve(filename);
        String contentType = Files.probeContentType(filePath);
        
        // Default content type for images
        if (contentType == null) {
            String extension = filename.toLowerCase();
            if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (extension.endsWith(".png")) {
                return "image/png";
            } else if (extension.endsWith(".gif")) {
                return "image/gif";
            } else if (extension.endsWith(".webp")) {
                return "image/webp";
            }
            return "image/jpeg"; // fallback
        }
        
        return contentType;
    }
}
