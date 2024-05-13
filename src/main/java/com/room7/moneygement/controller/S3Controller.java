package com.room7.moneygement.controller;

import com.room7.moneygement.dto.ResponseDto;
import com.room7.moneygement.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {

    private final S3Upload s3Upload;


    @PostMapping("/api/auth/image")
    public ResponseEntity<ResponseDto> imageUpload(@RequestPart(name = "profileImg", required = false) MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseDto("Invalid file. Please select a file to upload.", null));
        }

        try {
            String imageUrl = s3Upload.uploadFiles(multipartFile, "static");
            return ResponseEntity.ok(new ResponseDto("File uploaded successfully.", imageUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("Failed to upload file.", null));
        }
    }
}
