package com.example.dddaggregatesample.v4.domain.model.ingredientcategory

interface IngredientCategoryEvent

data class IngredientCategoryChanged(
        val ingredientCategory: IngredientCategory
) : IngredientCategoryEvent
