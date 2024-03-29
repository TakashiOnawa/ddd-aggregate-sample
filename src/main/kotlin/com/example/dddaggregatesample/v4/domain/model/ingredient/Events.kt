package com.example.dddaggregatesample.v4.domain.model.ingredient

import com.example.dddaggregatesample.v4.domain.model.recipe.RecipeId

interface IngredientEvent

data class IngredientCategoryAdded(
        val recipeId: RecipeId,
        val ingredientCategory: IngredientCategory,
        val ordering: Int
) : IngredientEvent

data class IngredientCategoriesReordered(
        val recipeId: RecipeId,
        val orderings: List<IngredientCategoryId>
) : IngredientEvent

data class IngredientCategoryDeleted(
        val recipeId: RecipeId,
        val ingredientCategoryId: IngredientCategoryId,
        val orderings: List<IngredientCategoryId>
) : IngredientEvent

data class IngredientCategoryChanged(
        val recipeId: RecipeId,
        val ingredientCategory: IngredientCategory
) : IngredientEvent
