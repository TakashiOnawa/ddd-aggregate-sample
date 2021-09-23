package com.example.dddaggregatesample.v4.application

import com.example.dddaggregatesample.v4.domain.model.ingredient.IngredientCategoryId
import com.example.dddaggregatesample.v4.domain.model.ingredient.IngredientCategoryTitle
import com.example.dddaggregatesample.v4.domain.model.ingredient.IngredientItem
import com.example.dddaggregatesample.v4.domain.model.recipe.RecipeId

data class AddIngredientCategoryCommand(
        val recipeId: RecipeId,
        val title: IngredientCategoryTitle,
        val ingredientItems: List<IngredientItem>
)

data class ChangeIngredientCategoryCommand(
        val recipeId: RecipeId,
        val categoryId: IngredientCategoryId,
        val categoryTitle: IngredientCategoryTitle,
        val ingredientItems: List<IngredientItem>
)

data class ReorderIngredientCategoriesCommand(
        val recipeId: RecipeId,
        val orderings: List<Pair<IngredientCategoryId, Int>>
)

data class DeleteIngredientCategoryCommand(
        val recipeId: RecipeId,
        val categoryId: IngredientCategoryId
)
