package com.example.dddaggregatesample.v3.domain.model.ingredient

import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientCategory
import com.example.dddaggregatesample.v3.domain.model.ingredientcategory.IngredientCategoryId
import com.example.dddaggregatesample.v3.domain.model.recipe.RecipeId
import com.example.dddaggregatesample.v3.domain.support.OptimisticLockingVersion

// 材料（集約ルート）
class Ingredient private constructor(
        val recipeId: RecipeId,
        val categories: List<IngredientCategoryId>,
        val version: OptimisticLockingVersion
) {
    companion object {
        private const val CATEGORIES_COUNT_MAX = 10

        // 材料を永続領域から復元する
        fun reconstruct(
                recipeId: RecipeId,
                categories: List<IngredientCategoryId>,
                version: OptimisticLockingVersion
        ): Ingredient {
            return Ingredient(recipeId, categories, version)
        }
    }

    // 材料カテゴリを追加する
    fun addCategory(
            category: IngredientCategory
    ): Validated<IngredientError, Pair<Ingredient, IngredientCategoryAdded>> {

        categories.plus(category.id).let {
            return if (it.size > CATEGORIES_COUNT_MAX) {
                IngredientError.CategoriesOverMax(CATEGORIES_COUNT_MAX)
                        .invalid()
            } else {
                Pair(Ingredient(recipeId, it, version), IngredientCategoryAdded(recipeId, category))
                        .valid()
            }
        }
    }

    // 材料カテゴリを並び替える
    fun reorderCategories(orderings: List<Pair<IngredientCategoryId, Int>>): Pair<Ingredient, IngredientCategoriesReordered> {
        val reorderedCategories = categories.toMutableList()

        orderings.forEach { pair ->
            reorderedCategories.find { it == pair.first }?.let {
                reorderedCategories.remove(it)
                reorderedCategories.add(pair.second - 1, it)
            }
        }

        return Pair(
                Ingredient(recipeId, reorderedCategories, version),
                IngredientCategoriesReordered(recipeId, reorderedCategories)
        )
    }

    // 材料カテゴリを削除する
    fun deleteCategory(categoryId: IngredientCategoryId): Pair<Ingredient, IngredientCategoryDeleted> {
        return categories.minus(categoryId).let {
            Pair(Ingredient(recipeId, it, version), IngredientCategoryDeleted(recipeId, categoryId, it))
        }
    }

    // 楽観的ロックのバージョンを上げる
    fun updateVersion(nextVersion: OptimisticLockingVersion): Ingredient {
        require(nextVersion.isNext(version))
        return Ingredient(recipeId, categories, nextVersion)
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
