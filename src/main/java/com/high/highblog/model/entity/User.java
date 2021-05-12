package com.high.highblog.model.entity;

import com.high.highblog.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "hb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User
        extends AbstractAuditingColumns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nick_name")
    private String nickName;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", length = 10)
    private GenderType genderType;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "background_path")
    private String backgroundPath;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "location")
    private String location;

    @Column(name = "bio")
    private String bio;

    @Transient
    private Boolean followed;

    @Transient
    private Boolean notified;
}