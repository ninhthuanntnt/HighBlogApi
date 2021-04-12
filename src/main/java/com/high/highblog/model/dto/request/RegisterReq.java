package com.high.highblog.model.dto.request;

import com.high.highblog.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
// TODO: Add validate for necessary fields

    @NonNull
    @Pattern(regexp = "[A-Za-z0-9_]")
    private String nickName;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NonNull
    private GenderType genderType;

    @NonNull
    @Pattern(regexp = "[A-Za-z0-9_]")
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String returnUrl;
}
