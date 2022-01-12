package com.shop.shop.repository;

import com.shop.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원가입 시 중복 여부 판단하기 위해 이메일 사용
    Member findByEmail(String email);
}
