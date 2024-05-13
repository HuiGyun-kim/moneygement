package com.room7.moneygement.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;

    public ResponseDto() {
        // 기본 생성자
    }

    public ResponseDto(String message) {
        this.message = message;
    }

}
