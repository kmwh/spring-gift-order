package gift.wishlist.service;

import gift.global.dto.PageResponseDto;
import gift.global.exception.WishlistNotFoundException;
import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.dto.CreateWishRequestDto;
import gift.wishlist.dto.UpdateWishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.entity.Wish;
import gift.wishlist.repository.WishlistRepository;
import gift.wishlist.vo.Amount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistServiceImpl implements WishlistService{
    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public PageResponseDto<WishResponseDto> findAllByMemberId(Long memberId, Pageable pageable) {
        Page<WishResponseDto> wishResponseDtoPage =
            wishlistRepository.findAllByMemberId(memberId, pageable)
                .map(WishResponseDto::from);
        return PageResponseDto.from(wishResponseDtoPage);
    }

    @Transactional
    @Override
    public WishResponseDto create(Long memberId, CreateWishRequestDto requestDto) {
        Wish wish = new Wish(
            new Member(memberId),
            new Product(requestDto.productId()),
            new Amount(requestDto.amount())
        );
        Wish wishResponse = wishlistRepository.save(wish);

        return WishResponseDto.from(wishResponse);
    }

    @Transactional
    @Override
    public void update(Long id, UpdateWishRequestDto requestDto) {
        Wish wish = wishlistRepository.findById(id)
            .orElseThrow(WishlistNotFoundException::new);

        wish.changeAmount(new Amount(requestDto.amount()));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!wishlistRepository.existsById(id)) {
            throw new WishlistNotFoundException();
        }

        wishlistRepository.deleteById(id);
    }
}
