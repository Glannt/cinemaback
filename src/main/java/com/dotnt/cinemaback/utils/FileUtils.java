package com.dotnt.cinemaback.utils;

import com.dotnt.cinemaback.dto.response.ApiResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class FileUtils {
    private static String UPLOADS_FOLDER = "uploads";
    public static void deleteFile(String filename) throws IOException {
        // Đường dẫn đến thư mục chứa file
        java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Đường dẫn đầy đủ đến file cần xóa
        java.nio.file.Path filePath = uploadDir.resolve(filename);

        // Kiểm tra xem file tồn tại hay không
        if (Files.exists(filePath)) {
            // Xóa file
            Files.delete(filePath);
        } else {
            //throw new FileNotFoundException("File not found: " + filename);
        }
    }
    public static boolean isImageFile(MultipartFile file) {
        return true;
        /*
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
         */
        /*
        AutoDetectParser parser = new AutoDetectParser();
        Detector detector = parser.getDetector();
        try {
            Metadata metadata = new Metadata();
            TikaInputStream stream = TikaInputStream.get(file.getInputStream());
            MediaType mediaType = detector.detect(stream, metadata);
            String mimeType =  mediaType.toString();
        } catch (IOException e) {
            return false;
        }
        */
    }

    public static String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = FilenameUtils.getExtension(filename);  // Lấy extension của file gốc
        // Thêm UUID và extension vào tên file để đảm bảo tên file là duy nhất và giữ nguyên extension
        String uniqueFilename = UUID.randomUUID().toString() + "_" + System.nanoTime() + "." + extension;

        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    /**
     * Validate image file size and type
     */
    public static ApiResponse<?> validateImageFile(MultipartFile file) {
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            return ApiResponse.builder()
                    .code(HttpStatus.PAYLOAD_TOO_LARGE.value())
                    .message(MessageKeys.UPLOAD_IMAGES_FILE_LARGE)
                    .build();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.builder()
                    .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .message(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE)
                    .build();
        }
        return null;
    }

}
