package com.fullcycle.admin.catalogo.infrastructure.castmembers.models;

/**
 * @author kalil.peixoto
 * @date 11/13/23 22:50
 * @email kalilmvp@gmail.com
 */
public record CastMemberResponse(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt) {
}
