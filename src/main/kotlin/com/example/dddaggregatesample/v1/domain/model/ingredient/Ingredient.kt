package com.example.dddaggregatesample.v1.domain.model.ingredient

import com.example.dddaggregatesample.v1.domain.model.ingredientcategory.IngredientCategoryId
import com.example.dddaggregatesample.v1.domain.model.recipe.RecipeId

// 材料（集約ルート）
class Ingredient private constructor(
        val recipeId: RecipeId,
        val categories: List<IngredientCategoryId>
) {
    companion object {
        private const val CATEGORIES_COUNT_MAX = 10

        // 材料を永続領域から復元する
        fun reconstruct(recipeId: RecipeId, categories: List<IngredientCategoryId>): Ingredient {
            return Ingredient(recipeId, categories)
        }
    }

    // 材料カテゴリを追加する
    fun addCategory(categoryId: IngredientCategoryId): Ingredient {
        return categories.plus(categoryId).let {
            require(it.size <= CATEGORIES_COUNT_MAX) {
                "材料カテゴリは $CATEGORIES_COUNT_MAX 個以下でなければなりません。"
            }
            Ingredient(recipeId, it)
        }
    }

    // 材料カテゴリを並び替える
    fun reorderCategories(orderings: List<Pair<IngredientCategoryId, Int>>): Ingredient {
        val reorderedCategories = categories.toMutableList()

        orderings.forEach { pair ->
            reorderedCategories.find { it == pair.first }?.let {
                reorderedCategories.remove(it)
                reorderedCategories.add(pair.second - 1, it)
            }
        }

        return Ingredient(recipeId, reorderedCategories)
    }

    // 材料カテゴリを削除する
    fun deleteCategory(categoryId: IngredientCategoryId): Ingredient {
        return Ingredient(recipeId, categories.minus(categoryId))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ingredient

        if (recipeId != other.recipeId) return false

        return true
    }

    override fun hashCode(): Int {
        return recipeId.hashCode()
    }
}
