package com.example.dddaggregatesample.v4.domain.model.ingredient

import com.example.dddaggregatesample.v4.domain.model.recipe.RecipeId
import com.example.dddaggregatesample.v4.domain.support.OptimisticLockingVersion

// 材料（集約ルート）
class Ingredient private constructor(
        val recipeId: RecipeId,
        val categories: List<IngredientCategory>,
        val version: OptimisticLockingVersion
) {
    companion object {
        private const val CATEGORIES_COUNT_MAX = 10

        // 材料を永続領域から復元する
        fun reconstruct(
                recipeId: RecipeId,
                categories: List<IngredientCategory>,
                version: OptimisticLockingVersion
        ): Ingredient {
            return Ingredient(recipeId, categories, version)
        }
    }

    // 材料カテゴリを追加する
    fun addCategory(category: IngredientCategory): Pair<Ingredient, IngredientCategoryAdded> {
        require(!categories.any { it == category }) {
            "材料カテゴリが既に存在します。"
        }

        return categories.plus(category).let {
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
            reorderedCategories.find { it.id == pair.first }?.let {
                reorderedCategories.remove(it)
                reorderedCategories.add(pair.second - 1, it)
            }
        }

        return Pair(
                Ingredient(recipeId, reorderedCategories, version),
                IngredientCategoriesReordered(recipeId, reorderedCategories.map { it.id })
        )
    }

    // 材料カテゴリを削除する
    fun deleteCategory(categoryId: IngredientCategoryId): Pair<Ingredient, IngredientCategoryDeleted> {
        return categories
                .filter { it.id != categoryId }
                .let {
                    Pair(
                            Ingredient(recipeId, it, version),
                            IngredientCategoryDeleted(recipeId, categoryId, it.map { e -> e.id })
                    )
                }
    }

    // 材料カテゴリを変更する
    fun changeCategory(
            categoryId: IngredientCategoryId,
            categoryTitle: IngredientCategoryTitle,
            ingredientItems: List<IngredientItem>
    ): Pair<Ingredient, IngredientCategoryChanged> {

        return categories
                .map {
                    if (it.id == categoryId) {
                        it.change(categoryTitle, ingredientItems)
                    } else {
                        it
                    }
                }
                .let {
                    Pair(
                            Ingredient(recipeId, it, version),
                            IngredientCategoryChanged(it.single { e -> e.id == categoryId })
                    )
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
