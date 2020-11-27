package com.apeter.blog.file.controller;

import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.file.api.response.FileResponse;
import com.apeter.blog.file.exception.FileExistException;
import com.apeter.blog.file.exception.FileNoExistException;
import com.apeter.blog.file.mapping.FileMapping;
import com.apeter.blog.file.model.FileDoc;
import com.apeter.blog.file.routes.FileApiRoutes;
import com.apeter.blog.file.service.FileApiService;
import com.apeter.blog.user.exception.UserNoExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Api(value = "File API")
public class FileController {
    private final FileApiService fileApiService;

    @PostMapping(FileApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need to create new file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "File already exists")
    })
    public @ResponseBody
    OkResponse<FileResponse> create(@RequestParam MultipartFile file) throws IOException, UserNoExistException, AuthException {
        return OkResponse.of(FileMapping.getInstance().getResponseMapping().convert(fileApiService.create(file)));
    }

    @GetMapping(FileApiRoutes.DOWNLOAD)
    @ApiOperation(value = "Find file by ID", notes = "Use this when you need to find file by id")
    public void byId(
            @ApiParam(value = "File id") @PathVariable ObjectId id,
            HttpServletResponse response
    ) throws ChangeSetPersister.NotFoundException, IOException {
        FileDoc fileDoc = fileApiService.findById(id).orElseThrow();
        response.addHeader("Content-Type", fileDoc.getContentType());
        response.addHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileDoc.getTitle());
        FileCopyUtils.copy(fileApiService.downloadById(id), response.getOutputStream());
    }
}
