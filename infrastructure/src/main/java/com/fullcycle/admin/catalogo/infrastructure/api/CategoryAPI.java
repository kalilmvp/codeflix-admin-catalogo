package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
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
@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping()
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get unique category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "422", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    CategoryResponse getById(@PathVariable("id") final String id);

    @PutMapping(value = "{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update unique category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "422", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    ResponseEntity<?> updateById(@PathVariable("id") final String id, @RequestBody UpdateCategoryRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete unique category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "422", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    void deleteById(@PathVariable("id") final String id);
}
