package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author kalil.peixoto
 * @date 7/10/24 11:50
 * @email kalilmvp@gmail.com
 */
public record AudioVideoMediaResponse(@JsonProperty("id") String id,
                                      @JsonProperty("checksum") String checksum,
                                      @JsonProperty("name") String name,
                                      @JsonProperty("raw_location") String rawLocation,
                                      @JsonProperty("encoded_location") String encodedLocation,
                                      @JsonProperty("status") String status) {
}
