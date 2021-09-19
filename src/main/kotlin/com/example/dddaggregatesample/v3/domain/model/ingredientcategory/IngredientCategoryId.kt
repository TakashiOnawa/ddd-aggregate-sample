package com.example.dddaggregatesample.v3.domain.model.ingredientcategory

import de.huxhorn.sulky.ulid.ULID

data class IngredientCategoryId(val value: String) {
    companion object {
        fun newId() = IngredientCategoryId(ULID().nextULID())
    }
}
