package com.shop.shop.controller;

import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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

    // 상품 수정 페이지
    // 상품 등록 페이지를 동일하게 사용 (저장 버튼을 수정 버튼으로 출력)
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {

        try {
            // 상품 조회해서 itemFormDto 객체에 넣기
            // itemFormDto 객체를 기반으로 상품 수정 페이지 생성 및 반환
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
        }
        return "cart/item/itemForm";
    }

    // 상품 수정 (Post)
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                             @RequestParam(name = "itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }
}
