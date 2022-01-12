package com.shop.shop.controller;

import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 페이지 접근
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "cart/item/itemForm";
    }

    // 상품 등록 (Post)
    // POST 입력으로 들어온 이미지 파일 (name = "itemImgFile")을 MultipartFile 객체로 받음
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam(name = "itemImgFile")List<MultipartFile> itemImgFileList) {

        // 입력 값이 비정상일 경우 다시 상품 등록 페이지로 돌아감
        if (bindingResult.hasErrors()) {
            return "cart/item/itemForm";
        }

        // 첫 번째 상품 이미지를 지정하지 않았으면 다시 상품 등록 페이지로 돌아감
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 값 입니다.");
            return "cart/item/itemForm";
        }

        // 입력 값이 정상일 경우
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "cart/item/itemForm";
        }
        return "redirect:/";
    }
}
