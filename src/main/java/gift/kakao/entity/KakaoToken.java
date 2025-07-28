package gift.kakao.entity;

import gift.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "kakao_token")
public class KakaoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "access_token_expires_at", nullable = false)
    private LocalDateTime accessTokenExpiresIn;

    @Column(name = "refresh_token_expires_at", nullable = false)
    private LocalDateTime refreshTokenExpiresIn;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(this.accessTokenExpiresIn);
    }

    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(this.refreshTokenExpiresIn);
    }
}
