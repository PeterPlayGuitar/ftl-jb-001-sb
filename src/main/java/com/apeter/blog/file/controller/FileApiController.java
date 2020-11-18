package com.apeter.blog.file.controller;

import com.apeter.blog.base.api.request.SearchRequest;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.base.api.response.SearchResponse;
import com.apeter.blog.file.exception.FileNoExistException;
import com.apeter.blog.file.routes.FileApiRoutes;
import com.apeter.blog.file.api.response.FileResponse;
import com.apeter.blog.file.exception.FileExistException;
import com.apeter.blog.file.mapping.FileMapping;
import com.apeter.blog.file.service.FileApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(value = "File API")
public class FileApiController {
    private final FileApiService fileApiService;

    @GetMapping(FileApiRoutes.BY_ID)
    @ApiOperation(value = "Find file by if", notes = "Use this when you need to find file by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 404, message = "File not found")
            }
    )
    public OkResponse<FileResponse> byId(
            @ApiParam(value = "File id") @PathVariable ObjectId id
    ) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(FileMapping.getInstance().getResponseMapping().convert(
                fileApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(FileApiRoutes.ROOT)
    @ApiOperation(value = "Search files", notes = "Use this when you need to search files by last name first name or email with skip and size of the response")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<SearchResponse<FileResponse>> search(
            @ModelAttribute SearchRequest request
    ) {
        return OkResponse.of(FileMapping.getInstance().getSearchMapping().convert(
                fileApiService.search(request)
        ));
    }

    @DeleteMapping(FileApiRoutes.BY_ID)
    @ApiOperation(value = "delete file by id", notes = "Use this when you need to delete file by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success")
            }
    )
    public OkResponse<String> deleteById(
            @ApiParam(value = "File id")
            @PathVariable ObjectId id
    ) {
        fileApiService.deleteById(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
