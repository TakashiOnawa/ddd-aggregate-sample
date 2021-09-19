package com.example.dddaggregatesample.v3.domain.model.ingredientcategory

interface IngredientCategoryEvent

data class IngredientCategoryChanged(
        val ingredientCategory: IngredientCategory
) : IngredientCategoryEvent
