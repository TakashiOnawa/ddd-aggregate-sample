package com.example.dddaggregatesample.v3.domain.model.ingredientcategory

data class IngredientCategoryTitle(val value: String) {
    companion object {
        const val LENGTH_MAX: Int = 50
    }

    init {
        require(value.length <= LENGTH_MAX) {
            "材料カテゴリのタイトルは $LENGTH_MAX 文字以下でなければなりません。"
        }
    }
}
