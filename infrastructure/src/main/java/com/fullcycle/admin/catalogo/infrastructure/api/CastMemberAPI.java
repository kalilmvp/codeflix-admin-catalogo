package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberListResponse;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CastMemberResponse;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.castmembers.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author kalil.peixoto
 * @date 10/9/23 23:02
 * @email kalilmvp@gmail.com
 */
@RequestMapping(value = "cast_members")
@Tag(name = "CastMembers")
public interface CastMemberAPI {

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Create a new cast member")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Created successfully"),
    @ApiResponse(responseCode = "422", description = "A validation error"),
    @ApiResponse(responseCode = "500", description = "Internal Server Error"),
})
  ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);

  @GetMapping
  @Operation(summary = "List all cast members paginated")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Listed successfully"),
          @ApiResponse(responseCode = "422", description = "A validation error"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error"),
  })
  Pagination<CastMemberListResponse> list(
    @RequestParam(name = "search", required = false, defaultValue = "") final String search,
    @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
    @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
    @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
    @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
  );

  @GetMapping(value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get unique cast member")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
          @ApiResponse(responseCode = "422", description = "Genre was not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error"),
  })
  CastMemberResponse getById(@PathVariable("id") final String id);

  @PutMapping(value = "{id}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update unique cast member")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
          @ApiResponse(responseCode = "422", description = "Genre was not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error"),
  })
  ResponseEntity<?> updateById(@PathVariable("id") final String id, @RequestBody UpdateCastMemberRequest input);

  @DeleteMapping(value = "{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete unique cast member")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Genre retrieved successfully"),
          @ApiResponse(responseCode = "422", description = "Genre was not found"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error"),
  })
  void deleteById(@PathVariable("id") final String id);
}
