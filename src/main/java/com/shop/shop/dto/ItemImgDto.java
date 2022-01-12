package com.shop.shop.dto;

// 상품 관련 DTO - ItemImgDto, ItemFormDto, ItemSearchDto, MainItemDto
// 상품을 등록 및 조회할 때 지정된 필드뿐 아니라 추가적인 데이터들의 이동이 많으므로 여러 DTO 이용


import com.shop.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

// 상품 이미지에 대한 DTO
// ItemImg Entity 객체를 ItemImgDto 객체로 변환
@Getter
@Setter
public class ItemImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
