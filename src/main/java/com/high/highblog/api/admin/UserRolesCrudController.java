package com.high.highblog.api.admin;


import com.high.highblog.bloc.admin.UserRolesCrudBloc;
import com.high.highblog.model.dto.request.admin.RolesUpdateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin/users-roles")
public class UserRolesCrudController {
    private final UserRolesCrudBloc userRolesCrudBloc;

    public UserRolesCrudController(UserRolesCrudBloc userRolesCrudBloc) {
        this.userRolesCrudBloc = userRolesCrudBloc;
    }
    @PutMapping
    public ResponseEntity<?> updateUserRoles(final RolesUpdateReq rolesUpdateReq){
        userRolesCrudBloc.updateUserRoles(rolesUpdateReq);
        return ResponseEntity.noContent().build();
    }
}
