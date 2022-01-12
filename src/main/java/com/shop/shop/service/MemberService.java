package com.shop.shop.service;

import com.shop.shop.entity.Member;
import com.shop.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 회원가입한 유저 저장
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 회원가입 시 이메일로 중복 여부 판단
    public void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        // 회원가입한 유저의 이메일을 조회해보았더니 DB에 존재하는 경우
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // UserDetailsService 는 DB에서 회원 정보를 가져오는 인터페이스
    // -> loadUserByUsername() 메소드를 통해 회원정보 조회 -> UserDetails 인터페이스 반환
    // UserDetails 는 회원 정보를 담는 인터페이스스
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
