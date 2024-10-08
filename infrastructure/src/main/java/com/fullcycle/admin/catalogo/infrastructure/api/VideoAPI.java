package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoListResponse;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.Set;

/**
 * @author kalil.peixoto
 * @date 10/9/23 23:02
 * @email kalilmvp@gmail.com
 */
@RequestMapping(value = "videos")
@Tag(name = "Videos")
public interface VideoAPI {

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video with params full")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<?> createFull(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "year_launched", required = false) Integer yearLaunched,
            @RequestParam(name = "duration", required = false) Double duration,
            @RequestParam(name = "rating", required = false) String rating,
            @RequestParam(name = "opened", required = false) Boolean opened,
            @RequestParam(name = "published", required = false) Boolean published,
            @RequestParam(name = "categories_id", required = false) Set<String> categories,
            @RequestParam(name = "genres_id", required = false) Set<String> genres,
            @RequestParam(name = "cast_members_id", required = false) Set<String> castMembers,
            @RequestParam(name = "video_file", required = false) MultipartFile videoFile,
            @RequestParam(name = "trailer_file", required = false) MultipartFile trailerFile,
            @RequestParam(name = "banner_file", required = false) MultipartFile bannerFile,
            @RequestParam(name = "thumb_file", required = false) MultipartFile thumbFile,
            @RequestParam(name = "thumb_half_file", required = false) MultipartFile thumbHalfFile);

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video draft, without medias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<?> createPartial(@RequestBody CreateVideoRequest payload);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a video by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<?> updateVideo(@PathVariable(name = "id") String id,
                                  @RequestBody UpdateVideoRequest payload);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a video by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    VideoResponse getById(@PathVariable(name = "id") String id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List videos paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Videos retrieved successfully"),
            @ApiResponse(responseCode = "422", description = "Query param was invalid"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    Pagination<VideoListResponse> list(@RequestParam(name = "search", required = false, defaultValue = "") final String search,
                                       @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
                                       @RequestParam(name = "perPage", required = false, defaultValue = "25") final int perPage,
                                       @RequestParam(name = "sort", required = false, defaultValue = "title") final String sort,
                                       @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction,
                                       @RequestParam(name = "categories_ids", required = false, defaultValue = "") final Set<String> categories,
                                       @RequestParam(name = "genres_ids", required = false, defaultValue = "") final Set<String> genres,
                                       @RequestParam(name = "cast_members_ids", required = false, defaultValue = "") final Set<String> castMembers);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a video by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Video deleted"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    void deleteById(@PathVariable(name = "id") String id);

    @GetMapping(value = "{id}/media/{type}")
    @Operation(summary = "Get a video media by it's type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Media was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<byte[]> getMediaByType(@PathVariable(name = "id") String id,
                                          @PathVariable(name = "type") String type);

    @PostMapping(
            value = "{id}/media/{type}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Upload a video by it's type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload media successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<?> uploadMedia(@PathVariable(name = "id") String id,
                                  @PathVariable(name = "type") String type,
                                  @RequestParam(name = "media_file") MultipartFile mediaFile);

}


