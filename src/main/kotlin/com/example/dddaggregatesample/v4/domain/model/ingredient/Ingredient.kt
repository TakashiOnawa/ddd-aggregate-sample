package com.example.dddaggregatesample.v4.domain.model.ingredient

import com.example.dddaggregatesample.v4.domain.model.ingredientcategory.IngredientCategory
import com.example.dddaggregatesample.v4.domain.model.ingredientcategory.IngredientCategoryId
import com.example.dddaggregatesample.v4.domain.model.recipe.RecipeId
import com.example.dddaggregatesample.v4.domain.support.OptimisticLockingVersion

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
    fun addCategory(category: IngredientCategory): Pair<Ingredient, IngredientCategoryAdded> {
        return categories.plus(category.id).let {
            require(it.size <= CATEGORIES_COUNT_MAX) {
                "材料カテゴリは $CATEGORIES_COUNT_MAX 個以下でなければなりません。"
            }
            Pair(Ingredient(recipeId, it, version), IngredientCategoryAdded(recipeId, category))
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
