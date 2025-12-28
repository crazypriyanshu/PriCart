package com.pdas.priCart.shop.product.repositories;

import com.pdas.priCart.shop.product.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
