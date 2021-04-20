package com.high.highblog.api.user;


import com.high.highblog.bloc.UserCrudBloc;
import com.high.highblog.helper.FileHelper;
import com.high.highblog.mapper.FileMapper;
import com.high.highblog.model.dto.request.FileReq;
import com.high.highblog.model.dto.request.UserUpdateReq;
import com.high.highblog.model.dto.response.ImageUploadRes;
import com.high.highblog.model.entity.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController("userUserCrudBloc")
@RequestMapping("/api/v1/user")
public class UserCrudController {

    private final UserCrudBloc userCrudBloc;

    public UserCrudController(final UserCrudBloc userCrudBloc) {
        this.userCrudBloc = userCrudBloc;
    }
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody final UserUpdateReq userUpdateReq) {
        userCrudBloc.updateUser(userUpdateReq);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/avatar")
    public  ResponseEntity<?> updateAvatar(@RequestParam("upload") MultipartFile avatarReq){
        String path = userCrudBloc.uploadAvatar(avatarReq);
        return ResponseEntity.ok(new ImageUploadRes((FileHelper.appendDomainToPath(path))));
    }
}