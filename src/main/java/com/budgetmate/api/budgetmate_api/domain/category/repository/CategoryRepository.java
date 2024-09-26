package com.budgetmate.api.budgetmate_api.domain.category.repository;

import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);

    @Query("select c.name from Category c where c.id = :id")
    String findNameById(@Param("id") Long id);
}
