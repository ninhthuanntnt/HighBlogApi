package com.high.highblog.api;

import com.high.highblog.bloc.FileBloc;
import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.dto.request.FileUploadReq;
import com.high.highblog.model.dto.response.CkFileUploadRes;
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
public class FileController {

    private final FileBloc fileBloc;

    public FileController(final FileBloc fileBloc) {
        this.fileBloc = fileBloc;
    }

    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(final FileUploadReq fileUploadReqs) {
        File image = fileBloc.uploadImage(fileUploadReqs);
        return ResponseEntity.ok(FileMapper.INSTANCE.toFileRes(image));
    }

    @PostMapping("/ck/images")
    public ResponseEntity<?> ckUploadImage(@RequestParam("file") MultipartFile multipartFile) {
        File image = fileBloc.ckUploadImange(multipartFile);
        return ResponseEntity.ok(new CkFileUploadRes(FileHelper.appendDomainToPath(image.getPath())));
    }
}
