package com.bank.account.repository;

import com.bank.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for CRUD operations on Account entity.
 * Called by AccountService to avoid handwritten SQL.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
