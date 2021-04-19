package com.high.highblog.api.user;


import com.high.highblog.bloc.UserCrudBloc;
import com.high.highblog.model.dto.request.UserUpdateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}