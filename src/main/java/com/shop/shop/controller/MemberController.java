package com.shop.shop.controller;

import com.shop.shop.dto.JoinFormDto;
import com.shop.shop.entity.Member;
import com.shop.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor

public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 페이지
    @GetMapping(value = "/new")
    public String memberJoinForm(Model model, JoinFormDto joinFormDto) {
        model.addAttribute("joinFormDto", joinFormDto);
        return "/cart/member/joinForm";
    }

    // 회원가입
    @PostMapping(value = "/new")
    public String memberJoin(@Valid JoinFormDto joinFormDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "/cart/member/joinForm";
        }

        // memberService.saveMember 에서 중복된 회원가입이면 예외 발생
        // 예외 발생하면 에러 메세지를 회원가입 페이지로 넘김
        // 문제 없이 저장되면 메인 페이지로 redirect
        try {
            Member member = Member.createMember(joinFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/cart/member/joinForm";
        }

        return "redirect:/";
    }

}
