package com.example.dddaggregatesample.v1.domain.model.ingredientcategory

// 材料カテゴリ（集約ルート）
class IngredientCategory private constructor(
        val id: IngredientCategoryId,
        val title: IngredientCategoryTitle,
        val ingredientItems: List<IngredientItem>
) {
    companion object {
        private const val INGREDIENT_ITEM_COUNT_MAX = 100

        // 材料カテゴリを新規作成する
        fun create(title: IngredientCategoryTitle, ingredientItems: List<IngredientItem>): IngredientCategory {
            validateIngredientItems(ingredientItems)
            return IngredientCategory(IngredientCategoryId.newId(), title, ingredientItems)
        }

        // 材料カテゴリを永続領域から復元する
        fun reconstruct(
                id: IngredientCategoryId,
                title: IngredientCategoryTitle,
                ingredientItems: List<IngredientItem>
        ): IngredientCategory {
            return IngredientCategory(id, title, ingredientItems)
        }

        private fun validateIngredientItems(ingredientItems: List<IngredientItem>) {
            require(ingredientItems.size <= INGREDIENT_ITEM_COUNT_MAX) {
                "材料は $INGREDIENT_ITEM_COUNT_MAX 個以下でなければなりません。"
            }
        }
    }

    // 材料カテゴリを変更する
    fun change(title: IngredientCategoryTitle, ingredientItems: List<IngredientItem>): IngredientCategory {
        validateIngredientItems(ingredientItems)
        return IngredientCategory(id, title, ingredientItems)
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
