package com.high.highblog.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {
    @NotNull
    @Size(min = 6, max = 100)
    private String username;

    @NotNull
    @Size(min = 8, max = 255)
    private String password;

    @JsonProperty("remember_me")
    private boolean rememberMe = true;
}
