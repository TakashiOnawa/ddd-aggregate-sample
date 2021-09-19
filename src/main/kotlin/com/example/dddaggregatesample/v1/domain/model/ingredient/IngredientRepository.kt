package com.example.dddaggregatesample.v1.domain.model.ingredient

import com.example.dddaggregatesample.v1.domain.model.recipe.RecipeId

interface IngredientRepository {
    fun findBy(recipeId: RecipeId): Ingredient?
    fun save(ingredient: Ingredient)
}
