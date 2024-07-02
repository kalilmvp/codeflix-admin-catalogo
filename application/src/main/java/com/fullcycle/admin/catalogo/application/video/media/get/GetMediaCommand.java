package com.fullcycle.admin.catalogo.application.video.media.get;

/**
 * @author kalil.peixoto
 * @date 7/1/24 11:20
 * @email kalilmvp@gmail.com
 */
public record GetMediaCommand(
        String videoId,
        String mediaType
) {

    public static GetMediaCommand with(String anId, String aMediaType) {
        return new GetMediaCommand(anId, aMediaType);
    }
}
