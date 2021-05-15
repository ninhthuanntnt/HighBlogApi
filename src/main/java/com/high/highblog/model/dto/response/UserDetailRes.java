package com.high.highblog.model.dto.response;

import com.high.highblog.enums.GenderType;

public class UserDetailRes {

    private Long id;

    private String nickName;

    private String firstName;

    private String lastName;

    private String imagePath;

    private GenderType genderType;

    private String backgroundPath;

    private Boolean followed;

    private Boolean notified;

    private Long createdDate;

    private String bio;

    private String websiteUrl;

    private String location;

    private Long numberOfFollowers;
}
