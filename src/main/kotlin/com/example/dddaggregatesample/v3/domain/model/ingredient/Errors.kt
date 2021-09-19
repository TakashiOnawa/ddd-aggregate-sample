package com.example.dddaggregatesample.v3.domain.model.ingredient

sealed class IngredientError(message: String) : Throwable(message) {
    data class CategoriesOverMax(val maxCount: Int) :
            IngredientError("材料カテゴリは $maxCount 個以下でなければなりません。")
}
