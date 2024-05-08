package com.fullcycle.admin.catalogo.application.video.retrieve.list;

import com.fullcycle.admin.catalogo.application.UseCase;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.domain.video.VideoSearchQuery;

/**
 * @author kalil.peixoto
 * @date 3/8/23 20:55
 * @email kalilmvp@gmail.com
 */
public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideosListOutput>> {

}
