package com.shop.shop.repository;

import com.shop.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;




public interface ItemRepository extends JpaRepository<Item, Long> {
}

