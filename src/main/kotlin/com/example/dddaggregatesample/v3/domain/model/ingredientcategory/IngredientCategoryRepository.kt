package com.example.dddaggregatesample.v3.domain.model.ingredientcategory

interface IngredientCategoryRepository {
    fun findBy(id: IngredientCategoryId): IngredientCategory?
    fun save(ingredientCategory: IngredientCategory, event: IngredientCategoryEvent): IngredientCategory
}
