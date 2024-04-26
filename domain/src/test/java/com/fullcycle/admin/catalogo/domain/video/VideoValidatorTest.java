package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kalil.peixoto
 * @date 4/24/24 21:17
 * @email kalilmvp@gmail.com
 */
public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = null;
        final var expectedDescription = """
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "";
        final var expectedDescription = """
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }


    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = """
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final var expectedDescription = """
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                 Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                 Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                 Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
                 Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
                 Dive deep into the crucial world of system design interviews with this comprehensive guide. 
                This video offers viewers an in-depth look at the strategies and thought processes top engineers use to 
                tackle complex system design questions. Whether you're a budding software engineer or an experienced 
                developer, these insights will help you understand key concepts and prepare effectively for your next 
                big interview.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'ratings' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}
