package com.room7.moneygement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;
}
