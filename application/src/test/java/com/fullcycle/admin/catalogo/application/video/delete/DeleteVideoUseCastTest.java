package com.fullcycle.admin.catalogo.application.video.delete;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author kalil.peixoto
 * @date 5/3/24 16:23
 * @email kalilmvp@gmail.com
 */
public class DeleteVideoUseCastTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        // given
        final var expectedId = VideoID.unique();

        doNothing()
                .when(this.videoGateway).deleteById(any());
        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        // then
        verify(this.videoGateway, times(1)).deleteById(eq(expectedId));

    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        final var expectedId = VideoID.from("123123");

        doNothing()
                .when(this.videoGateway).deleteById(any());
        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        // then
        verify(this.videoGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var expectedId = VideoID.from("123123");

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
                .when(this.videoGateway).deleteById(any());

        // when
        assertThrows(InternalErrorException.class, () -> this.useCase.execute(expectedId.getValue()));

        // then
        verify(this.videoGateway, times(1)).deleteById(eq(expectedId));
    }
}
