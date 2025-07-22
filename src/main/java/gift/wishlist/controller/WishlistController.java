package gift.wishlist.controller;

import gift.global.dto.PageResponseDto;
import gift.wishlist.dto.CreateWishRequestDto;
import gift.wishlist.dto.UpdateWishRequestDto;
import gift.wishlist.dto.WishResponseDto;
import gift.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<WishResponseDto>> findAllByMemberId(
        HttpServletRequest request,
        Pageable pageable
    ) {
        Long memberId = (Long) request.getAttribute("memberId");

        return ResponseEntity.ok(wishlistService.findAllByMemberId(memberId, pageable));
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> create(
        HttpServletRequest request,
        @RequestBody CreateWishRequestDto requestDto
    ) {
        Long memberId = (Long) request.getAttribute("memberId");

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(wishlistService.create(memberId, requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody UpdateWishRequestDto requestDto
    ) {
        wishlistService.update(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wishlistService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
