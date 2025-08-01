package gift.member.entity;

import gift.member.vo.Email;
import gift.member.vo.Name;
import gift.member.vo.Password;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Column(name = "provider")
    private SocialType provider;

    @Column(name = "social_id")
    private Long socialId;

    protected Member() {}

    public Member(Long id) {
        this.id = id;
    }

    public Member(Name name, Email email, Password password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Name name, Email email, Password password, SocialType provider, Long socialId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.socialId = socialId;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public static Member createFromKakao(Long socialId) {
        return new Member(
            new Name("", true),
            new Email("", true),
            new Password("", true),
            SocialType.KAKAO,
            socialId
        );
    }
}
