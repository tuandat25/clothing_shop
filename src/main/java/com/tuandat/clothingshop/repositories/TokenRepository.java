package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
