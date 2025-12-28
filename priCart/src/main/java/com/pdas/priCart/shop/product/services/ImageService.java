package com.pdas.priCart.shop.product.services;

import com.pdas.priCart.shop.product.dto.ImageDto;
import com.pdas.priCart.shop.product.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file, Long imageId);
}
