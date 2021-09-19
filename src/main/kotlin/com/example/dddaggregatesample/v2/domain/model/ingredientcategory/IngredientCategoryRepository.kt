package com.example.dddaggregatesample.v2.domain.model.ingredientcategory

interface IngredientCategoryRepository {
    fun findBy(id: IngredientCategoryId): IngredientCategory?
    fun save(ingredientCategory: IngredientCategory, event: IngredientCategoryEvent): IngredientCategory
}
