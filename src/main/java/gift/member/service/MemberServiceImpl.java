package gift.member.service;

import static gift.global.util.Assert.check;

import gift.global.exception.InvalidPasswordException;
import gift.global.exception.MemberEmailAlreadyExistsException;
import gift.global.exception.MemberEmailNotFoundException;
import gift.global.exception.MemberNotFoundException;
import gift.global.security.JwtProvider;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.member.vo.Email;
import gift.member.vo.Name;
import gift.member.vo.Password;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    @Override
    public void register(MemberRegisterRequestDto requestDto) {
        if (memberRepository.findByEmail(new Email(requestDto.email())) != null) {
            throw new MemberEmailAlreadyExistsException();
        }

        Member member = new Member(
            null,
            new Name(requestDto.name()),
            new Email(requestDto.email()),
            new Password(requestDto.password())
        );
        memberRepository.save(member);
    }

    @Override
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = validMember(requestDto.email(), requestDto.password());
        String token = jwtProvider.createToken(member);

        return new MemberLoginResponseDto(token);
    }

    private Member validMember(String email, String password) {
        Member member = memberRepository.findByEmail(new Email(email));

        check(member != null, new MemberEmailNotFoundException());
        check(member.getPassword().matches(password), new InvalidPasswordException());
        return member;
    }

    @Override
    public List<MemberResponseDto> findAll() {
        return memberRepository
            .findAll()
            .stream()
            .map(MemberResponseDto::from)
            .toList();
    }

    @Override
    public MemberResponseDto findById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        Member member = memberOptional.orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.from(member);
    }

    @Transactional
    @Override
    public void update(Long id, MemberRegisterRequestDto requestDto) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }

        Member member = new Member(
            id,
            new Name(requestDto.name()),
            new Email(requestDto.email()),
            new Password(requestDto.password())
        );
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }

        memberRepository.deleteById(id);
    }
}
