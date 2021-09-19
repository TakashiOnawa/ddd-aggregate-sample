package com.example.dddaggregatesample.v3.domain.model.ingredientcategory

import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
import com.example.dddaggregatesample.v3.domain.support.OptimisticLockingVersion

// 材料カテゴリ（集約ルート）
class IngredientCategory private constructor(
        val id: IngredientCategoryId,
        val title: IngredientCategoryTitle,
        val ingredientItems: List<IngredientItem>,
        val version: OptimisticLockingVersion
) {
    companion object {
        private const val INGREDIENT_ITEM_COUNT_MAX = 100

        // 材料カテゴリを新規作成する
        fun create(
                title: IngredientCategoryTitle,
                ingredientItems: List<IngredientItem>
        ): Validated<IngredientCategoryError, IngredientCategory> {

            return validateIngredientItems(ingredientItems)?.invalid()
                    ?: IngredientCategory(
                            IngredientCategoryId.newId(),
                            title,
                            ingredientItems,
                            OptimisticLockingVersion.initial()
                    ).valid()
        }

        // 材料カテゴリを永続領域から復元する
        fun reconstruct(
                id: IngredientCategoryId,
                title: IngredientCategoryTitle,
                ingredientItems: List<IngredientItem>,
                version: OptimisticLockingVersion
        ): IngredientCategory {
            return IngredientCategory(id, title, ingredientItems, version)
        }

        private fun validateIngredientItems(ingredientItems: List<IngredientItem>): IngredientCategoryError? {
            return if (ingredientItems.size > INGREDIENT_ITEM_COUNT_MAX) {
                IngredientCategoryError.IngredientItemsOverMax(INGREDIENT_ITEM_COUNT_MAX)
            } else {
                null
            }
        }
    }

    // 材料カテゴリを変更する
    fun change(
            title: IngredientCategoryTitle,
            ingredientItems: List<IngredientItem>
    ): Validated<IngredientCategoryError, Pair<IngredientCategory, IngredientCategoryChanged>> {

        return validateIngredientItems(ingredientItems)?.invalid()
                ?: IngredientCategory(id, title, ingredientItems, version)
                        .let { Pair(it, IngredientCategoryChanged(it)) }
                        .valid()

    }

    // 楽観的ロックのバージョンを上げる
    fun updateVersion(nextVersion: OptimisticLockingVersion): IngredientCategory {
        require(nextVersion.isNext(version))
        return IngredientCategory(id, title, ingredientItems, nextVersion)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IngredientCategory

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
