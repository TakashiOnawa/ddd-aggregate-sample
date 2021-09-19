package com.example.dddaggregatesample.v2.domain.model.ingredientcategory

interface IngredientCategoryEvent

data class IngredientCategoryChanged(
        val ingredientCategory: IngredientCategory
) : IngredientCategoryEvent
