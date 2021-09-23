package com.example.dddaggregatesample.v4.domain.model.ingredientcategory

data class IngredientItemQuantity(val value: String) {
    companion object {
        const val LENGTH_MAX: Int = 20
    }

    init {
        require(value.length <= LENGTH_MAX) {
            "材料の分量は $LENGTH_MAX 文字以下でなければなりません。"
        }
    }
}
