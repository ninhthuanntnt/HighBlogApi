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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileService {

    private final FileUpload fileUploadConfigProperties;
    private final FileRepository fileRepository;
    private final String imagesRootDir;
    private final String imageSubDir;

    public FileService(final ApplicationConfigProperties applicationConfigProperties,
                       final FileRepository fileRepository) {
        this.fileUploadConfigProperties = applicationConfigProperties.getFileUpload();
        this.fileRepository = fileRepository;
        this.imagesRootDir = applicationConfigProperties.getFileUpload().getRootDir();
        this.imageSubDir = applicationConfigProperties.getFileUpload().getImagesSubDir();
    }

    @PostConstruct
    public void postConstruct() {
        String imageDir = fileUploadConfigProperties.getRootDir() + "/" + this.imageSubDir;
        Path path = Paths.get(imageDir);

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.info("Can't create directories #{}", path);
        }
    }

    @Transactional(readOnly = true)
    public File getByIdAndUserId(final Long id, final Long userId) {
        log.info("Get file by id #{}", id);

        return fileRepository.findByIdAndUserId(id, userId)
                             .orElseThrow(() -> new ObjectNotFoundException("file"));
    }

    @Transactional
    public File saveNew(final File file) {
        log.info("Save new file info with name #{} and path #{}", file.getName(), file.getPath());

        validateFileBeforeSaveNew(file);

        return fileRepository.save(file);
    }

    @Transactional
    public Page<File> fetchListImagesByUserIdWithPageRequest(final Long userId, final PageRequest pageRequest) {
        log.info("Fetch list images by userId #{}", userId);

        return fileRepository.findByUserId(userId, pageRequest);
    }

    public String saveNewImageToStorage(final MultipartFile multipartFile) {
        validateImage(multipartFile);
        try {
            String imagesDir = fileUploadConfigProperties.getRootDir()
                    + "/"
                    + this.imageSubDir;

            String fileName = FileHelper.saveFile(imagesDir, multipartFile);

            return this.imageSubDir + "/" + fileName;
        } catch (IOException e) {
            log.error("Cannot save file #{} by #{}",
                      multipartFile.getOriginalFilename(),
                      e.getMessage());
            throw new ValidatorException("Unsuccessful", "image");
        }
    }

    @Transactional
    public void deleteImageFromStorageByIdAndUserId(final Long id, Long userId) {
        log.info("Delete image from storage by id #{} and userId #{}", id, userId);

        File file = getByIdAndUserId(id, userId);

        String storagePath = this.imagesRootDir + "/" + file.getPath();

        try {
            FileUtils.forceDelete(new java.io.File(storagePath));
        } catch (IOException e) {
            log.error("Cannot delete file at storage path #{}", storagePath);
            e.printStackTrace();
            throw new ValidatorException("Can't delete image", "image");
        }
    }
    @Transactional
    public void deleteImageFromStorageByPath(final String path) {
        log.info("Delete image from storage by path #{} ", path);

        String storagePath = this.imagesRootDir + "/" + path;

        try {
            FileUtils.forceDelete(new java.io.File(storagePath));
        } catch (IOException e) {
            log.error("Cannot delete file at storage path #{}", storagePath);
            e.printStackTrace();
//            throw new ValidatorException("Can't delete image", "image");
        }
    }

    @Transactional
    public void deleteByIdAndUserId(final Long id, final Long userId) {
        log.info("Delete image by id #{} and userId #{}", id, userId);
        File file = getByIdAndUserId(id, userId);

        fileRepository.delete(file);
    }

    private void validateImage(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        if (StringUtils.isEmpty(fileName)) {
            throw new ValidatorException("Invalid", "image");
        } else if (!FileHelper.isValidFilename(fileName)) {

            throw new ValidatorException("Invalid file name", "image");

        } else {
            String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            if (StringUtils.isEmpty(ext)) {
                throw new ObjectNotFoundException("fileExtension");
            } else if (!fileUploadConfigProperties.getAllowedImageExtensions().contains(ext)) {
                throw new ValidatorException("Invalid extension", "image");
            }
        }
    }

    private void validateFileBeforeSaveNew(final File file) {
        if (ObjectUtils.isNotEmpty(file.getId()))
            throw new ValidatorException("Invalid file id", "id");
    }

}
