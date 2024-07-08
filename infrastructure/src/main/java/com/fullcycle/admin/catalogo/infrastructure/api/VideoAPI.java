package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
  @Operation(summary = "Create a new video with videos")
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
}
