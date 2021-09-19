package com.example.dddaggregatesample.v2.domain.model.ingredientcategory

import com.example.dddaggregatesample.v2.domain.support.OptimisticLockingVersion

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
        fun create(title: IngredientCategoryTitle, ingredientItems: List<IngredientItem>): IngredientCategory {
            validateIngredientItems(ingredientItems)
            return IngredientCategory(
                    IngredientCategoryId.newId(),
                    title,
                    ingredientItems,
                    OptimisticLockingVersion.initial()
            )
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

        private fun validateIngredientItems(ingredientItems: List<IngredientItem>) {
            require(ingredientItems.size <= INGREDIENT_ITEM_COUNT_MAX) {
                "材料は $INGREDIENT_ITEM_COUNT_MAX 個以下でなければなりません。"
            }
        }
    }

    // 材料カテゴリを変更する
    fun change(
            title: IngredientCategoryTitle,
            ingredientItems: List<IngredientItem>
    ): Pair<IngredientCategory, IngredientCategoryChanged> {

        validateIngredientItems(ingredientItems)
        return IngredientCategory(id, title, ingredientItems, version)
                .let { Pair(it, IngredientCategoryChanged(it)) }
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
