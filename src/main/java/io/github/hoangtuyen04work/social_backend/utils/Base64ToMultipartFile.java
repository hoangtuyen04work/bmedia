package io.github.hoangtuyen04work.social_backend.utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64ToMultipartFile {

    public static MultipartFile convertBase64ToMultipart(String base64, String fileName) throws IOException {
        // Tách phần metadata (data:image/png;base64,) nếu có
        String[] parts = base64.split(",");
        String imageString = parts.length > 1 ? parts[1] : parts[0];

        byte[] decodedBytes = Base64.getDecoder().decode(imageString);

        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
                if (fileName.endsWith(".gif")) return "image/gif";
                return "image/png"; // Default
            }

            @Override
            public boolean isEmpty() {
                return decodedBytes.length == 0;
            }

            @Override
            public long getSize() {
                return decodedBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return decodedBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(decodedBytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                FileUtils.writeByteArrayToFile(dest, decodedBytes);
            }
        };
    }
}
