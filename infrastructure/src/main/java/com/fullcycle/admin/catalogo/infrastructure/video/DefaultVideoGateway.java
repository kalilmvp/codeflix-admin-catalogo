package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.utils.CollectionUtils;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.utils.SqlUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.fullcycle.admin.catalogo.domain.utils.CollectionUtils.mapTo;
import static com.fullcycle.admin.catalogo.domain.utils.CollectionUtils.nullIfEmpty;
import static com.fullcycle.admin.catalogo.infrastructure.utils.SqlUtils.like;
import static com.fullcycle.admin.catalogo.infrastructure.utils.SqlUtils.upper;

/**
 * @author kalil.peixoto
 * @date 6/18/24 20:22
 * @email kalilmvp@gmail.com
 */
@Component
public class DefaultVideoGateway implements VideoGateway {
    private final VideoRepository videoRepository;

    public DefaultVideoGateway(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    @Transactional
    public Video create(Video aVideo) {
        return this.save(aVideo);
    }

    @Override
    @Transactional
    public Video update(Video aVideo) {
        return this.save(aVideo);
    }

    @Override
    public void deleteById(VideoID anId) {
        if (this.videoRepository.existsById(anId.getValue())) {
            this.videoRepository.deleteById(anId.getValue());
        }
    }

    @Override
    /**
     * @Transactional - in case the entity has any lazy relationshios ( avoid LazyInitializationException
     * readOnly = true : Spring informs Hibernate that this operation can only perform reads
     */
    @Transactional(readOnly = true)
    public Optional<Video> findById(VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJPAEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                like(upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(Video aVideo) {
        return this.videoRepository.save(VideoJPAEntity.from(aVideo)).toAggregate();
    }
}
