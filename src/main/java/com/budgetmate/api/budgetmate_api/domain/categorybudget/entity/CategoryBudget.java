package com.budgetmate.api.budgetmate_api.domain.categorybudget.entity;

import com.budgetmate.api.budgetmate_api.domain.budget.entity.Budget;
import com.budgetmate.api.budgetmate_api.domain.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "categoryBudget")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryBudget {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private long amount;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "budget_id", nullable = false)
        private Budget budget;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id",nullable = false)
        private Category category;

        public void  updateAmount(long amount){
                this.amount += amount;
        }

}
