package com.high.highblog.bloc;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.FileReq;
import com.high.highblog.model.entity.File;
import com.high.highblog.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileCrudBloc {

    private final FileService fileService;

    public FileCrudBloc(final FileService fileService) {
        this.fileService = fileService;
    }


    @Transactional
    public File uploadImage(final FileReq fileReq) {

        String path = fileService.saveNewImageToStorage(fileReq.getImage());

        String name = fileReq.getName();

        log.info("Upload image with path #{}", path);
        if (ObjectUtils.isEmpty(name)) {
            name = fileReq.getImage().getOriginalFilename();
        }

        return fileService.saveNew(buildFileToSaveNew(name, path));
    }

    @Transactional
    public File ckUploadImage(final MultipartFile image) {

        String path = fileService.saveNewImageToStorage(image);

        String name = image.getOriginalFilename();

        log.info("Ck upload image with path #{}", path);
        return fileService.saveNew(buildFileToSaveNew(name, path));
    }

    private File buildFileToSaveNew(final String name, final String path) {
        Long userId = SecurityHelper.getUserId();

        return File.builder()
                   .userId(userId)
                   .name(name)
                   .path(path)
                   .build();
    }

    @Transactional
    public void deleteImageForCurrentUser(final Long id) {
        log.info("Delete image for current user by id #{}", id);

        Long userId = SecurityHelper.getUserId();

        fileService.deleteImageFromStorageByIdAndUserId(id, userId);
        fileService.deleteByIdAndUserId(id, userId);
    }
}
