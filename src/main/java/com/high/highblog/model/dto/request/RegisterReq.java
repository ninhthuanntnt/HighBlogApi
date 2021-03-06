package com.high.highblog.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.high.highblog.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
// TODO: Add validate for necessary fields
    @NonNull
    @JsonProperty("first_name")
    private String firstName;

    @NonNull
    @JsonProperty("last_name")
    private String lastName;

    @NonNull
    private String email;

    @NonNull
    @JsonProperty("gender_type")
    private GenderType genderType;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    @JsonProperty("return_url")
    private String returnUrl;
}
