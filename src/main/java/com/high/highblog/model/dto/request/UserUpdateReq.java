package com.high.highblog.model.dto.request;

import com.high.highblog.enums.GenderType;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateReq {
// TODO: Add validate for necessary fields

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9_]")
    private String nickName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private GenderType genderType;

    private String websiteUrl;

    private String location;

    private String bio;

}