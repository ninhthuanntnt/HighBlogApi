package com.high.highblog.api;

import com.high.highblog.bloc.FileBloc;
import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.dto.request.ImageUploadReq;
import com.high.highblog.model.dto.response.CkImageUploadRes;
import com.high.highblog.model.dto.response.ImageUploadRes;
import com.high.highblog.model.entity.File;
import com.high.highblog.model.mapper.FileMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    private final FileBloc fileBloc;

    public FileUploadController(final FileBloc fileBloc) {
        this.fileBloc = fileBloc;
    }

    @PostMapping("/images")
    public ResponseEntity<ImageUploadRes> uploadImage(final ImageUploadReq imageUploadReqs) {
        File image = fileBloc.uploadImage(imageUploadReqs);
        return ResponseEntity.ok(FileMapper.INSTANCE.toFileRes(image));
    }

    @PostMapping("/ck/images")
    public ResponseEntity<CkImageUploadRes> ckUploadImage(@RequestParam("upload") MultipartFile multipartFile) {
        File image = fileBloc.ckUploadImange(multipartFile);
        return ResponseEntity.ok(new CkImageUploadRes(FileHelper.appendDomainToPath(image.getPath())));
    }
}
