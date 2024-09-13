package com.budgetmate.api.budgetmate_api.domain.user.repository;

import com.budgetmate.api.budgetmate_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
