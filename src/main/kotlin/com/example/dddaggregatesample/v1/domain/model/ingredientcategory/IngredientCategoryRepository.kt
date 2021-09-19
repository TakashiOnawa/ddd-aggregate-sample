package com.example.dddaggregatesample.v1.domain.model.ingredientcategory

interface IngredientCategoryRepository {
    fun findBy(id: IngredientCategoryId): IngredientCategory?
    fun save(ingredientCategory: IngredientCategory)
    fun delete(ingredientCategory: IngredientCategory)
}
