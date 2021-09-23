package com.example.dddaggregatesample.v4.domain.model.ingredient

import de.huxhorn.sulky.ulid.ULID

data class IngredientCategoryId(val value: String) {
    companion object {
        fun newId() = IngredientCategoryId(ULID().nextULID())
    }
}
