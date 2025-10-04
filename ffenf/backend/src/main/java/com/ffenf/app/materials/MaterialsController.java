package com.ffenf.app.materials;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ffenf.app.ai.OpenAiFileService;
import com.ffenf.app.domain.CoinTransaction;
import com.ffenf.app.domain.Material;
import com.ffenf.app.domain.User;
import com.ffenf.app.repo.CoinTransactionRepository;
import com.ffenf.app.repo.MaterialBookmarkRepository;
import com.ffenf.app.repo.MaterialRepository;
import com.ffenf.app.repo.UserRepository;
import com.ffenf.app.storage.FileStorageService;


@RestController
@RequestMapping("/materials")
public class MaterialsController {

    private final MaterialRepository materials;
    private final UserRepository users;
    private final CoinTransactionRepository coinTransactions;
    private final MaterialBookmarkRepository materialBookmarks;
    private final FileStorageService fileStorageService;
    private final OpenAiFileService openAiFileService;

    public MaterialsController(MaterialRepository materials, UserRepository users, CoinTransactionRepository coinTransactions, MaterialBookmarkRepository materialBookmarks, FileStorageService fileStorageService, OpenAiFileService openAiFileService) {
        this.materials = materials;
        this.users = users;
        this.coinTransactions = coinTransactions;
        this.materialBookmarks = materialBookmarks;
        this.fileStorageService = fileStorageService;
        this.openAiFileService = openAiFileService;
    }

    public record SearchRequest(String q, String subject, String courseCode, int page, int size) {}

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "courseCode", required = false) String courseCode,
            @RequestParam(value = "tags", required = false) List<String> tags,
            Authentication auth) {
        try {
            String email = auth.getName();
            User currentUser = users.findByEmail(email).orElseThrow(() -> 
                new RuntimeException("User not found: " + email));

            System.out.println("Upload request - File: " + file.getOriginalFilename() + ", Title: " + title + ", User: " + currentUser.getEmail());
            
            // Upload PDF to OpenAI and get file ID
            String openaiFileId = null;
            if (file.getContentType() != null && file.getContentType().equals("application/pdf")) {
                try {
                    openaiFileId = openAiFileService.uploadPdfToOpenAI(file);
                    System.out.println("PDF uploaded to OpenAI with file ID: " + openaiFileId);
                } catch (Exception e) {
                    System.err.println("Failed to upload PDF to OpenAI: " + e.getMessage());
                    e.printStackTrace();
                    return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload PDF to OpenAI: " + e.getMessage()));
                }
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Only PDF files are supported"));
            }
            
            // Store the file locally as backup
            String storageKey = fileStorageService.storeFile(file);
            System.out.println("File stored locally with key: " + storageKey);

            Material material = new Material();
            material.setUserId(currentUser.getId());
            material.setTitle(title);
            material.setSubject(subject);
            material.setCourseCode(courseCode);
            material.setTags(tags != null ? String.join(",", tags) : "");
            material.setStorageKey(storageKey);
            material.setFileSize(file.getSize());
            material.setMimeType(file.getContentType());
            material.setStatus("uploaded");
            material.setTextExtract(openaiFileId); // Store OpenAI file ID instead of extracted text

            Material savedMaterial = materials.save(material);
            System.out.println("Material saved with ID: " + savedMaterial.getId());

            // Reward user with a coin for uploading
            currentUser.setCoins(currentUser.getCoins() + 1);
            users.save(currentUser);
            System.out.println("User rewarded 1 coin, new balance: " + currentUser.getCoins());

            CoinTransaction coinTransaction = new CoinTransaction();
            coinTransaction.setUserId(currentUser.getId());
            coinTransaction.setDelta(1);
            coinTransaction.setReason("upload_reward");
            coinTransaction.setRefId(savedMaterial.getId());
            coinTransactions.save(coinTransaction);
            System.out.println("Coin transaction recorded");

            return ResponseEntity.ok(savedMaterial);
        } catch (Exception e) {
            System.err.println("Upload error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Upload failed",
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "") String q,
                                   @RequestParam(required = false) String subject,
                                   @RequestParam(required = false) String courseCode,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   Authentication auth) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("avgRating").descending()
                .and(Sort.by("createdAt").descending()));
        
        Page<Material> results;
        
        if (q.isEmpty() && subject == null && courseCode == null) {
            results = materials.findAll(pageable);
        } else {
            // Simple search - in production, use full-text search
            if (!q.isEmpty()) {
                results = materials.findByTitleContainingIgnoreCaseOrSubjectContainingIgnoreCaseOrCourseCodeContainingIgnoreCase(
                    q, q, q, pageable);
            } else {
                results = materials.findAll(pageable);
            }
        }

        List<Map<String, Object>> materialsList = results.getContent().stream()
            .map(m -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", m.getId());
                map.put("title", m.getTitle());
                map.put("subject", m.getSubject() != null ? m.getSubject() : "");
                map.put("courseCode", m.getCourseCode() != null ? m.getCourseCode() : "");
                map.put("avgRating", m.getAvgRating());
                map.put("ratingsCount", m.getRatingsCount());
                map.put("downloadsCount", m.getDownloadsCount());
                map.put("createdAt", m.getCreatedAt());
                
                // Add user information
                User user = users.findById(m.getUserId()).orElse(null);
                if (user != null) {
                    map.put("user", Map.of(
                        "id", user.getId(),
                        "name", user.getName()
                    ));
                } else {
                    map.put("user", Map.of(
                        "id", m.getUserId(),
                        "name", "Unknown User"
                    ));
                }
                
                // Add bookmark status
                if (auth != null) {
                    String email = auth.getName();
                    User currentUser = users.findByEmail(email).orElse(null);
                    if (currentUser != null) {
                        boolean isBookmarked = materialBookmarks.existsByUserIdAndMaterialId(currentUser.getId(), m.getId());
                        map.put("isBookmarked", isBookmarked);
                    } else {
                        map.put("isBookmarked", false);
                    }
                } else {
                    map.put("isBookmarked", false);
                }
                
                return map;
            })
            .toList();

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("materials", materialsList);
        response.put("totalElements", results.getTotalElements());
        response.put("totalPages", results.getTotalPages());
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterial(@PathVariable UUID id, Authentication auth) {
        Material material = materials.findById(id).orElse(null);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", material.getId());
        response.put("title", material.getTitle());
        response.put("subject", material.getSubject() != null ? material.getSubject() : "");
        response.put("courseCode", material.getCourseCode() != null ? material.getCourseCode() : "");
        response.put("tags", material.getTags() != null ? material.getTags() : "");
        response.put("avgRating", material.getAvgRating());
        response.put("ratingsCount", material.getRatingsCount());
        response.put("downloadsCount", material.getDownloadsCount());
        response.put("summary", material.getSummary() != null ? material.getSummary() : "");
        response.put("flashcardsJson", material.getFlashcardsJson() != null ? material.getFlashcardsJson() : "");
        response.put("createdAt", material.getCreatedAt());
        
        // Check if material is bookmarked by current user
        if (auth != null) {
            String email = auth.getName();
            User user = users.findByEmail(email).orElse(null);
            if (user != null) {
                boolean isBookmarked = materialBookmarks.existsByUserIdAndMaterialId(user.getId(), id);
                response.put("isBookmarked", isBookmarked);
            } else {
                response.put("isBookmarked", false);
            }
        } else {
            response.put("isBookmarked", false);
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<?> trackDownload(@PathVariable UUID id, Authentication auth) {
        Material material = materials.findById(id).orElse(null);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }

        // Increment download count
        material.setDownloadsCount(material.getDownloadsCount() + 1);
        materials.save(material);

        // Return download URL
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("downloadUrl", "/materials/" + material.getId() + "/file");
        response.put("fileName", material.getTitle() + ".pdf");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID id) {
        Material material = materials.findById(id).orElse(null);
        if (material == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = fileStorageService.getFilePath(material.getStorageKey());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = material.getMimeType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getTitle() + ".pdf\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
