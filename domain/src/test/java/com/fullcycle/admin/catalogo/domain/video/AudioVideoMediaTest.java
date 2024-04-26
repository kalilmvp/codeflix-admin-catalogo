package com.fullcycle.admin.catalogo.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallNewAudioVideo_shouldReturnInstance() {
        // given
        final var expectedCheckSum = "123";
        final var expectedName = "Banner.jpg";
        final var expectedRawLocation = "/images/123";
        final var expectedEncodedLocation = "/images/123";
        final var expectedStatus = MediaStatus.PENDING;

        // when
        final var actualAudioVideo = AudioVideoMedia.with(expectedCheckSum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        // then
        assertNotNull(actualAudioVideo);
        assertEquals(expectedCheckSum, actualAudioVideo.checksum());
        assertEquals(expectedName, actualAudioVideo.name());
        assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
        assertEquals(expectedEncodedLocation, actualAudioVideo.encodedLocation());
        assertEquals(expectedStatus, actualAudioVideo.status());
    }

    @Test
    public void givenTwoAudioVideoWithSameCheckSumAndRawLocation_whenCallEquals_shouldReturnTrue() {
        // given
        final var expectedCheckSum = "123";
        final var expectedRawLocation = "/images/123";

        final var img1 = ImageMedia.with(expectedCheckSum, "Random", expectedRawLocation);
        final var img2 = ImageMedia.with(expectedCheckSum, "Simple", expectedRawLocation);

        // then
        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with(null, "Random", "/images", "/images", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("abc", null, "/images", "/images", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("abc", "Random", null, "/images", MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("abc", "Random", "/images", null, MediaStatus.PENDING));
        assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("abc", "Random", "/images", "/images", null));
    }
}
