package com.shop.shop.service;

import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.ItemImg;
import com.shop.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    // 상품 저장
    private final ItemRepository itemRepository;
    // 상품 이미지 저장
    private final ItemImgService itemImgService;

    // 상품 저장
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        // ItemFormDto 객체를 DB에 저장하기 위해 Item 객체로 변환
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        // 첫번째 이미지는 대표 사진으로 설정
        for (int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i==0) {
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

}
