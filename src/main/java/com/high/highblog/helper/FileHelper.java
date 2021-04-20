package com.high.highblog.helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.high.highblog.constant.Constant;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

public class FileHelper {

    private FileHelper() {
    }

    public static String saveFile(final String destDir, final MultipartFile multipartFile)
            throws IOException {
        String fileName = FileHelper
                .generateNormalizeFileNameWithDate(
                        Objects.requireNonNull(multipartFile.getOriginalFilename()));

        Path path = Paths.get(destDir + "/" + fileName);

        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public static String generateNormalizeFileNameWithDate(final String file) {
        String ext = FilenameUtils.getExtension(file);
        String fileName = FilenameUtils.getBaseName(file);
        fileName = fileName.replaceAll(" ", "_");
        long date = System.currentTimeMillis();
        long randomNumber = Math.round(Math.random() * 100000);

        return fileName + date + randomNumber + '.' + ext;
    }

    public static String readResourceAsString(String path) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(path);
        return asString(resource);
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String appendDomainToPath(final String path) {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("").toUriString();
        return url + "/" + path;
    }

    public static boolean isValidFilename(final String filename) {
        Pattern pattern = Pattern.compile(Constant.FILE_NAME_REGEX);
        Matcher matcher = pattern.matcher(filename);

        return matcher.find();
    }
}
