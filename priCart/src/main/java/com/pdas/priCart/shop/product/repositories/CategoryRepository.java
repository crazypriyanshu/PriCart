package com.pdas.priCart.shop.product.repositories;

import com.pdas.priCart.shop.product.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    Optional<Category> findCategoryById(Long id);
    Page<Category> findAll(Pageable pageable);

    Optional<Category> findByNameIgnoreCase(String name);
}
