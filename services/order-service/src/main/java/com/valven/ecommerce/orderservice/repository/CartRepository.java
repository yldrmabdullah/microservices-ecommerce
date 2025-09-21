package com.valven.ecommerce.orderservice.repository;

import com.valven.ecommerce.orderservice.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(UUID userId);
}


