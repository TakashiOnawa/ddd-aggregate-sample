package com.example.dddaggregatesample.v3.domain.model.ingredientcategory

sealed class IngredientCategoryError(message: String) : Throwable(message) {
    data class IngredientItemsOverMax(val maxCount: Int) :
            IngredientCategoryError("材料カ要素は $maxCount 個以下でなければなりません。")
}
