package com.fullcycle.admin.catalogo.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallNewImage_shouldReturnInstance() {
        // given
        final var expectedCheckSum = "123";
        final var expectedName = "Banner.jpg";
        final var expectedLocation = "/images/123";

        // when
        final var actualImage = ImageMedia.with(expectedCheckSum, expectedName, expectedLocation);

        // then
        assertNotNull(actualImage);
        assertEquals(expectedCheckSum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameCheckSumAndLocation_whenCallEquals_shouldReturnTrue() {
        // given
        final var expectedCheckSum = "123";
        final var expectedLocation = "/images/123";

        final var img1 = ImageMedia.with(expectedCheckSum, "Random", expectedLocation);
        final var img2 = ImageMedia.with(expectedCheckSum, "Simple", expectedLocation);

        // then
        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_shouldReturnError() {
        assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "Random", "/images"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("abc", null, "/images"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with(null,  null, "/images"));
    }
}
