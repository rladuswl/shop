package com.shop.shop.service;

import com.shop.shop.dto.ItemFormDto;
import com.shop.shop.dto.ItemImgDto;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.ItemImg;
import com.shop.shop.repository.ItemImgRepository;
import com.shop.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    // 상품 저장
    private final ItemRepository itemRepository;
    // 상품 이미지 저장
    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    // 상품 저장
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록 (1번)
        // ItemFormDto 객체를 DB에 저장하기 위해 Item 객체로 변환
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록 (2번)
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

    // 상품 조회
    // item 엔티티와 img 정보 엔티티를 itemFormDto 객체로 변환 후 반환하는 조회 기능
    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {

        // 상품 id를 기반으로 "상품 이미지" Entity 객체 가져오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 상품 이미지 DTO 객체를 담을 그릇 생성
        // 상품 이미지 Entity들을 DTO 객체로 변환하여 itemImgDtoList에 담음
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        // 상품 이미지 Entity 객체를
        // 상품 이미지 DTO 객체로 변환
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 id를 기반으로 "상품" Entity 객체 가져옴
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 상품 Entity 객체를 상품 DTO 객체로 변환
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    // 상품 수정 (상품 정보 수정 + 상품 이미지 수정)
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {

        // 상품 Id를 이용하여 기존 상품 Entity 불러오기
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        // 상품 이미지 수정
        // 기존의 이미지 Id들 불러오기
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        // 기존 Id들에 새로운 이미지 파일들로 각각 업데이트
        for (int i=0; i< itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }
}
