package com.tuandat.clothingshop.repositories;

import com.tuandat.clothingshop.models.SocialAccount;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, UUID> {
}
