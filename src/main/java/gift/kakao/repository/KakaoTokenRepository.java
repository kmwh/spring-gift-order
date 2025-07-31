package gift.kakao.repository;

import gift.kakao.entity.KakaoToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findByMemberId(Long memberId);
}
