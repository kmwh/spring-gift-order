package gift.wishlist.service;

import gift.global.dto.PageResponseDto;
import gift.wishlist.dto.CreateWishRequestDto;
import gift.wishlist.dto.UpdateWishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import org.springframework.data.domain.Pageable;

public interface WishlistService {
    PageResponseDto<WishResponseDto> findAllByMemberId(Long memberId, Pageable pageable);
    WishResponseDto create(Long memberId, CreateWishRequestDto requestDto);
    void update(Long memberId, UpdateWishRequestDto requestDto);
    void delete(Long id);
}
