package com.room7.moneygement.controller;

import com.room7.moneygement.dto.ResponseDto;
import com.room7.moneygement.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class S3Controller {

    private final S3Upload s3Upload;

    @PostMapping("/api/auth/image")
    public ResponseDto imageUpload(@RequestPart(required = false)MultipartFile multipartFile){
        if (multipartFile.isEmpty()){
            return new ResponseDto("파일이 유효하지 않습니다.");
        }
        try{
            return new ResponseDto(s3Upload.uploadFiles(multipartFile, "static"));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseDto("파일이 유호하지 않습니다.");
        }
    }
}
