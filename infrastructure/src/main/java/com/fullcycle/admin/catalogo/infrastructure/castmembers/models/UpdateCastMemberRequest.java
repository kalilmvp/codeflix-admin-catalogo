package com.fullcycle.admin.catalogo.infrastructure.castmembers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;

/**
 * @author kalil.peixoto
 * @date 10/17/23 23:13
 * @email kalilmvp@gmail.com
 */
public record UpdateCastMemberRequest(@JsonProperty("name") String name,
                                      @JsonProperty("type") CastMemberType type) {
}
