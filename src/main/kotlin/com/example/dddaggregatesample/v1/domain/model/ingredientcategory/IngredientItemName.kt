package com.example.dddaggregatesample.v1.domain.model.ingredientcategory

data class IngredientItemName(val value: String) {
    companion object {
        const val LENGTH_MIN: Int = 1
        const val LENGTH_MAX: Int = 50
    }

    init {
        require(value.length in LENGTH_MIN..LENGTH_MAX) {
            "材料名は $LENGTH_MIN 文字以上 $LENGTH_MAX 文字以下でなければなりません。"
        }
    }
}
