package com.picus.core.post.application.port.in;

import com.picus.core.post.application.port.in.result.LoadGalleryResult;

import java.util.Optional;

/**
 * 특정 전문가의 갤러리를 조회한다.
 * ※ 갤러리: 전문가가 고정처리한 게시물
 */
public interface LoadGalleryUseCase {

    Optional<LoadGalleryResult> load(String expertNo);
}
