package com.high.highblog.api.user;

import com.high.highblog.bloc.FileCrudBloc;
import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.dto.request.FileReq;
import com.high.highblog.model.dto.response.CkImageUploadRes;
import com.high.highblog.model.dto.response.FileRes;
import com.high.highblog.model.entity.File;
import com.high.highblog.mapper.FileMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user/files")
public class FileCrudController {

    private final FileCrudBloc fileCrudBloc;

    public FileCrudController(final FileCrudBloc fileCrudBloc) {
        this.fileCrudBloc = fileCrudBloc;
    }

    @PostMapping("/images")
    public ResponseEntity<FileRes> uploadImage(final FileReq fileReqs) {
        File image = fileCrudBloc.uploadImage(fileReqs);
        return ResponseEntity.ok(FileMapper.INSTANCE.toFileRes(image));
    }

    @PostMapping("/ck/images")
    public ResponseEntity<CkImageUploadRes> ckUploadImage(@RequestParam("upload") MultipartFile multipartFile) {
        File image = fileCrudBloc.ckUploadImage(multipartFile);
        return ResponseEntity.ok(new CkImageUploadRes(FileHelper.appendDomainToPath(image.getPath())));
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable final Long id) {
        fileCrudBloc.deleteImageForCurrentUser(id);
        return ResponseEntity.noContent().build();
    }
}
