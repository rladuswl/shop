package com.shop.shop.entity;

import com.shop.shop.constant.Role;
import com.shop.shop.dto.JoinFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
// 회원정보를 저장하는 Entity
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Member 객체 생성
    public static Member createMember(JoinFormDto joinFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(joinFormDto.getName());
        member.setEmail(joinFormDto.getEmail());
        member.setAddress(joinFormDto.getAddress());
        String password = passwordEncoder.encode(joinFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
}
