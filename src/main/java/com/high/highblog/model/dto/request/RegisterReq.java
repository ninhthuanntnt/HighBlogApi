package com.high.highblog.model.dto.request;

import com.high.highblog.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
// TODO: Add validate for necessary fields

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9_]+")
    private String nickName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotNull
    private GenderType genderType;

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9_]+")
    @Size(min = 6, max = 100)
    private String username;

    @NotNull
    @Size(min = 8, max = 255)
    private String password;

    @NotNull
    private String returnUrl;
}
