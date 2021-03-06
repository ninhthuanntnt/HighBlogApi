package com.high.highblog.service;

import com.high.highblog.config.ApplicationConfigProperties;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.entity.File;
import com.high.highblog.repository.FileRepository;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.config.ApplicationConfigProperties.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileService {

    private final FileUpload fileUploadConfigProperties;
    private final FileRepository fileRepository;
    private final String imageSubDir;

    public FileService(final ApplicationConfigProperties applicationConfigProperties,
                       final FileRepository fileRepository) {
        this.fileUploadConfigProperties = applicationConfigProperties.getFileUpload();
        this.fileRepository = fileRepository;
        this.imageSubDir = applicationConfigProperties.getFileUpload().getImagesSubDir();
    }

    @PostConstruct
    public void postConstruct() {
        String imageDir = fileUploadConfigProperties.getRootDir() + "/" + this.imageSubDir;
        Path path = Paths.get(imageDir);

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.info("Can't create directories #{}", path.toString());
        }
    }

    @Transactional
    public File save(final String name, final String path) {
        log.info("Save file info with name #{} and path #{}", name, path);

        Long userId = SecurityHelper.getUserId();

        return fileRepository.save(buildFile(userId, name, path));
    }

    public String saveImageToStorage(MultipartFile multipartFile) {
        try {
            validateImage(multipartFile);

            String imagesDir = fileUploadConfigProperties.getRootDir()
                    + "/"
                    + this.imageSubDir;

            String fileName = FileHelper.saveFile(imagesDir, multipartFile);

            return fileUploadConfigProperties.getImagesSubDir() + "/" + fileName;
        } catch (IOException e) {
            log.error("Cannot save file #{} by #{}",
                      multipartFile.getOriginalFilename(),
                      e.getMessage());
            throw new ValidatorException("Unsuccessful", "image");
        }

    }

    private void validateImage(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        if (StringUtils.isEmpty(fileName)) {
            throw new ValidatorException("Invalid", "image");
        } else {
            String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            if (StringUtils.isEmpty(ext)) {
                throw new ObjectNotFoundException("fileExtension");
            } else if (!fileUploadConfigProperties.getAllowedImageExtensions().contains(ext)) {
                throw new ValidatorException("Invalid extension", "image");
            }
        }
    }

    private File buildFile(final Long userId, final String name, final String path) {
        return File.builder()
                   .userId(userId)
                   .name(name)
                   .path(path)
                   .build();
    }
}
