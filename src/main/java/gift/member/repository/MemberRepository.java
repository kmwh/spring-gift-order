package gift.member.repository;

import gift.member.entity.Member;
import gift.member.vo.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(Email email);
    Optional<Member> findByProviderAndSocialId(String provider, Long socialId);
}
