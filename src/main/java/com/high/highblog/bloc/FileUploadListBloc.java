package com.high.highblog.bloc;


import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import com.high.highblog.model.entity.File;
import com.high.highblog.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileUploadListBloc {
    private final FileService fileService;

    public FileUploadListBloc(final FileService fileService) {
        this.fileService = fileService;
    }

    public Page<File> fetchListImagesForCurrentUser(final BasePaginationReq basePaginationReq) {
        log.info("Fetch list images for current user");

        PageRequest pageRequest = PaginationHelper.generatePageRequest(basePaginationReq);

        return fileService.fetchListImagesByUserIdWithPageRequest(SecurityHelper.getUserId(), pageRequest);
    }
}
