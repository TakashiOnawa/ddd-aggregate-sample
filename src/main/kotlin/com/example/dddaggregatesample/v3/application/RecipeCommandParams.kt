package com.example.dddaggregatesample.v3.application

import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientCategoryId
import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientCategoryTitle
import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientItem
import com.example.dddaggregatesample.v3.domain.model.recipe.RecipeId

data class AddIngredientCategoryCommand(
        val recipeId: RecipeId,
        val title: IngredientCategoryTitle,
        val ingredientItems: List<IngredientItem>
)

data class ChangeIngredientCategoryCommand(
        val id: IngredientCategoryId,
        val title: IngredientCategoryTitle,
        val ingredientItems: List<IngredientItem>
)

data class ReorderIngredientCategoriesCommand(
        val recipeId: RecipeId,
        val orderings: List<Pair<IngredientCategoryId, Int>>
)

data class DeleteIngredientCategoryCommand(
        val recipeId: RecipeId,
        val id: IngredientCategoryId
)
