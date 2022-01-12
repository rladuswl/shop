package com.shop.shop.service;

import com.shop.shop.entity.ItemImg;
import com.shop.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

// 상품 이미지 Service
// 상품 이미지 업로드, 상품 이미지 정보 저장 Service
@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    // application.properties 에 설정한 itemImgLocation 값을 가져옴
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    // 상품 이미지 "정보" 저장 - itemImgRepository
    private final ItemImgRepository itemImgRepository;
    // 상품 이미지 업로드 - FileService
    private final FileService fileService;

    // 상품 이미지 정보 저장 + 파일 업로드
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        // 상품의 이미지가 존재하지 않는다면 건너 뜀
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item" + imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    // 상품 이미지 수정
    // 기존의 이미지 정보 객체를 불러옴 (조회)
    // 기존 이미지 파일 존재 -> 삭제
    // 기존 이미지 파일 존재하지 않음 -> 추가
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws IOException {

        // 상품 이미지를 수정했다면 == itemImgFile에 수정된 이미지가 담겨져 있다면
        if (!itemImgFile.isEmpty()) {
            // 기존의 이미지 정보 객체를 불러오기 (조회)
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일이 존재한다면 -> 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg);
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            // 수정한 이미지 파일 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
