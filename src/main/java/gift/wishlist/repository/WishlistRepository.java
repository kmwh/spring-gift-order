package gift.wishlist.repository;

import gift.wishlist.entity.Wish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);
}
