package com.high.highblog.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.high.highblog.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
// TODO: Add validate for necessary fields
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    private GenderType genderType;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String returnUrl;
}
