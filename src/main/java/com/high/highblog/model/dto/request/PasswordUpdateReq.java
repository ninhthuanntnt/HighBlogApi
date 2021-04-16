package com.high.highblog.model.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordUpdateReq {
    @NotNull
    @Size(min = 8, max = 255)
    private String oldPassword;

    @NotNull
    @Size(min = 8, max = 255)
    private String newPassword;
}
