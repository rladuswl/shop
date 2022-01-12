package com.shop.shop.dto;


// 상품 관련 DTO - ItemImgDto, ItemFormDto, ItemSearchDto, MainItemDto
// 상품을 등록 및 조회할 때 지정된 필드뿐 아니라 추가적인 데이터들의 이동이 많으므로 여러 DTO 이용

import com.shop.shop.constant.ItemSellStatus;
import com.shop.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

// 화면으로부터 입력 받은 상품 데이터 정보 DTO
// Item Entity 객체와 DTO 객체 간의 변환
@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName; // 상품명

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price; // 상품 가격

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail; // 상품 상세 설명

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock; // 상품 재고

    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO -> Entity
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    // Entity -> DTO
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
