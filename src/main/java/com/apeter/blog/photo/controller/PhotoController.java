package com.apeter.blog.photo.controller;

import com.apeter.blog.album.exception.AlbumNoExistException;
import com.apeter.blog.base.api.response.OkResponse;
import com.apeter.blog.file.api.response.FileResponse;
import com.apeter.blog.file.mapping.FileMapping;
import com.apeter.blog.file.routes.FileApiRoutes;
import com.apeter.blog.file.service.FileApiService;
import com.apeter.blog.photo.api.response.PhotoResponse;
import com.apeter.blog.photo.exception.PhotoExistException;
import com.apeter.blog.photo.mapping.PhotoMapping;
import com.apeter.blog.photo.model.PhotoDoc;
import com.apeter.blog.photo.routes.PhotoApiRoutes;
import com.apeter.blog.photo.service.PhotoApiService;
import com.apeter.blog.user.exception.UserNoExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Api(value = "Photo API")
public class PhotoController {
    private final PhotoApiService photoApiService;

    @PostMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need to create new photo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Photo already exists")
    })
    public @ResponseBody
    OkResponse<PhotoResponse> create(
            @RequestParam MultipartFile file,
            @RequestParam ObjectId ownerId,
            @RequestParam ObjectId albumId
    ) throws IOException, UserNoExistException, PhotoExistException, AlbumNoExistException {
        return OkResponse.of(PhotoMapping.getInstance().getResponseMapping().convert(photoApiService.create(file, ownerId, albumId)));
    }

    @GetMapping(PhotoApiRoutes.DOWNLOAD)
    @ApiOperation(value = "Find photo by ID", notes = "Use this when you need to find file by id")
    public void byId(
            @ApiParam(value = "Photo id") @PathVariable ObjectId id,
            HttpServletResponse response
    ) throws ChangeSetPersister.NotFoundException, IOException {
        PhotoDoc photoDoc = photoApiService.findById(id).orElseThrow();
        response.addHeader("Content-Type", photoDoc.getContentType());
        response.addHeader("Content-Disposition", "inline; filename=\"" + photoDoc.getTitle() + "\"");
        FileCopyUtils.copy(photoApiService.downloadById(id), response.getOutputStream());
    }
}
