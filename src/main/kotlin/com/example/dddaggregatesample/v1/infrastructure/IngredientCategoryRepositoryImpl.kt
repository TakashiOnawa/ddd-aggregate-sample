package com.example.dddaggregatesample.v1.infrastructure

import com.example.dddaggregatesample.v1.domain.model.ingredientcategory.IngredientCategory
import com.example.dddaggregatesample.v1.domain.model.ingredientcategory.IngredientCategoryId
import com.example.dddaggregatesample.v1.domain.model.ingredientcategory.IngredientCategoryRepository
import org.springframework.stereotype.Component

@Component
class IngredientCategoryRepositoryImpl : IngredientCategoryRepository {
    override fun findBy(id: IngredientCategoryId): IngredientCategory? {
        TODO("DB から復元する")
    }

    override fun save(ingredientCategory: IngredientCategory) {
        TODO("材料カテゴリ全体を DB に保存する")
    }

    override fun delete(ingredientCategory: IngredientCategory) {
        TODO("材料カテゴリ全体を DB から削除する")
    }
}
