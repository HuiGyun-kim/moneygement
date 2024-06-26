package com.room7.moneygement.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Component
@Slf4j
public class S3Upload {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 업로드 및 URL 반환
    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile,dirName);
    }
    //S3에 파일 업로드
    private String upload(File uploadFile, String dirName){
        String fileName = dirName + "/" + UUID.randomUUID() + "." + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile); // 로컬에 생성된 파일 삭제

        return uploadImageUrl; //업로드된 파일의 S3 URL 주소 반환
    }

   // S3 버킷에 파일 저장
    private String putS3(File uploadFile,String fileName){
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead) //PublicRead 권한으로 업로드
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 생성된 파일 삭제
    private void removeNewFile(File targetFile){
        if (targetFile.delete()){
            log.info("파일 삭제가 완료되었습니다.");
        } else{
            log.info("파일 삭제가 실패되었습니다.");
        }
    }

//    변환
    private Optional<File> convert(MultipartFile files) throws IOException{
        File convertFile = new File(System.getProperty("user.dir") + "/" + files.getOriginalFilename());

        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(files.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}
