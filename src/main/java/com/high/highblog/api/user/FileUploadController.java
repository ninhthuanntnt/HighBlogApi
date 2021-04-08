package com.high.highblog.api.user;

import com.high.highblog.bloc.FileUploadBloc;
import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.dto.request.ImageUploadReq;
import com.high.highblog.model.dto.response.CkImageUploadRes;
import com.high.highblog.model.dto.response.ImageUploadRes;
import com.high.highblog.model.entity.File;
import com.high.highblog.mapper.FileMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user/files")
public class FileUploadController {

    private final FileUploadBloc fileUploadBloc;

    public FileUploadController(final FileUploadBloc fileUploadBloc) {
        this.fileUploadBloc = fileUploadBloc;
    }

    @PostMapping("/images")
    public ResponseEntity<ImageUploadRes> uploadImage(final ImageUploadReq imageUploadReqs) {
        File image = fileUploadBloc.uploadImage(imageUploadReqs);
        return ResponseEntity.ok(FileMapper.INSTANCE.toImageUploadRes(image));
    }

    @PostMapping("/ck/images")
    public ResponseEntity<CkImageUploadRes> ckUploadImage(@RequestParam("upload") MultipartFile multipartFile) {
        File image = fileUploadBloc.ckUploadImage(multipartFile);
        return ResponseEntity.ok(new CkImageUploadRes(FileHelper.appendDomainToPath(image.getPath())));
    }
}
