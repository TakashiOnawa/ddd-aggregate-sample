package com.example.dddaggregatesample.v3.domain.model.ingredient

import com.example.dddaggregatesample.v3.domain.model.recipe.RecipeId

interface IngredientRepository {
    fun findBy(recipeId: RecipeId): Ingredient?
    fun save(ingredient: Ingredient, event: IngredientEvent): Ingredient
}
