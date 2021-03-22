package com.high.highblog.bloc;

import com.high.highblog.model.dto.request.ImageUploadReq;
import com.high.highblog.model.entity.File;
import com.high.highblog.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileBloc {

    private final FileService fileService;

    public FileBloc(final FileService fileService) {
        this.fileService = fileService;
    }


    @Transactional
    public File uploadImage(final ImageUploadReq imageUploadReq) {

        String url = fileService.saveImageToStorage(imageUploadReq.getImage());

        String name = imageUploadReq.getName();

        if (ObjectUtils.isEmpty(name)) {
            name = imageUploadReq.getImage().getOriginalFilename();
        }

        return fileService.save(name, url);
    }

    @Transactional
    public File ckUploadImange(final MultipartFile image) {

        String url = fileService.saveImageToStorage(image);

        String name = image.getOriginalFilename();

        return fileService.save(name, url);
    }
}
