package com.example.dddaggregatesample.v4.domain.model.ingredient

import com.example.dddaggregatesample.v4.domain.model.recipe.RecipeId

interface IngredientRepository {
    fun findBy(recipeId: RecipeId): Ingredient?
    fun save(ingredient: Ingredient, event: IngredientEvent): Ingredient
}
