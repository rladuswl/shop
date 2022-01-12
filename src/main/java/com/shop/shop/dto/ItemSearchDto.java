package com.shop.shop.dto;

import com.shop.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

// 상품 조회 조건 - 상품 등록일, 상품 판매 상태, 상품명 또는 상품 등록자 아이디
@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery = "";
}
