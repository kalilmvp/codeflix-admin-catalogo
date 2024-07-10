package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 7/10/24 11:50
 * @email kalilmvp@gmail.com
 */
public record ImageMediaResponse(@JsonProperty("id") String id,
                                 @JsonProperty("checksum") String checksum,
                                 @JsonProperty("name") String name,
                                 @JsonProperty("location") String location) {
}
