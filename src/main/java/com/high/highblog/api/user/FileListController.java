package com.high.highblog.api.user;

import com.high.highblog.bloc.FileListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.FileMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.response.BasePaginationRes;
import com.high.highblog.model.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/files")
public class FileListController {

    private final FileListBloc fileListBloc;

    public FileListController(final FileListBloc fileListBloc) {
        this.fileListBloc = fileListBloc;
    }

    @GetMapping("/images")
    public ResponseEntity<BasePaginationRes> fetchListImages(final BasePaginationReq basePaginationReq){
        Page<File> images = fileListBloc.fetchListImagesForCurrentUser(basePaginationReq);

        return ResponseEntity.ok(PaginationHelper.buildBasePaginationRes(images.map(FileMapper.INSTANCE::toImageUploadRes)));
    }
}
