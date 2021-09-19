package com.example.dddaggregatesample.v1.infrastructure

import com.example.dddaggregatesample.v1.domain.model.ingredient.Ingredient
import com.example.dddaggregatesample.v1.domain.model.ingredient.IngredientRepository
import com.example.dddaggregatesample.v1.domain.model.recipe.RecipeId
import org.springframework.stereotype.Component

@Component
class IngredientRepositoryImpl : IngredientRepository {
    override fun findBy(recipeId: RecipeId): Ingredient? {
        TODO("DB から復元する")
    }

    override fun save(ingredient: Ingredient) {
        TODO("材料全体を DB に保存する")
    }
}
