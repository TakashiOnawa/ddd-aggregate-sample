package com.example.dddaggregatesample.v3.domain.model.ingredient

import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientCategory
import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientCategoryId
import com.example.dddaggregatesample.v3.domain.model.recipe.RecipeId

interface IngredientEvent

data class IngredientCategoryAdded(
        val recipeId: RecipeId,
        val ingredientCategory: IngredientCategory
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
