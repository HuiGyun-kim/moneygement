package com.room7.moneygement.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;
    private String imageUrl;

    public ResponseDto(String message, String imageUrl) {
        this.message = message;
        this.imageUrl = imageUrl;
    }
}
