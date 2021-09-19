package com.example.dddaggregatesample.v2.infrastructure

import com.example.dddaggregatesample.v2.domain.model.ingredient.*
import com.example.dddaggregatesample.v2.domain.model.recipe.RecipeId
import com.example.dddaggregatesample.v2.domain.support.OptimisticLockingError
import org.springframework.stereotype.Component

@Component
class IngredientRepositoryImpl : IngredientRepository {
    override fun findBy(recipeId: RecipeId): Ingredient? {
        val ingredient = TODO("省略（DB から復元する。）")
        return ingredient
    }

    /**
     * イベントの内容を用いて DB 更新する。
     * @throws OptimisticLockingError 楽観的ロックエラー
     */
    override fun save(ingredient: Ingredient, event: IngredientEvent): Ingredient {
        // イベントごとに DB への保存処理を分ける。
        return when (event) {
            is IngredientCategoryAdded -> save(ingredient, event)
            is IngredientCategoriesReordered -> save(ingredient, event)
            is IngredientCategoryDeleted -> save(ingredient, event)
            else -> throw IllegalArgumentException("イベントがサポートされていません。event: ${event.javaClass.name}")
        }
    }

    // 材料カテゴリの追加を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoryAdded): Ingredient {
        val currentVersion = ingredient.version
        val nextVersion = ingredient.version.next()

        // 省略
        //   version、nextVersion を用いて楽観的ロックをしつつ、
        //   event が持つ情報を利用して材料カテゴリのテーブルに INSERT する。

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }

    // 材料カテゴリの順番変更を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoriesReordered): Ingredient {
        val currentVersion = ingredient.version
        val nextVersion = ingredient.version.next()

        // 省略
        //   version、nextVersion を用いて楽観的ロックをしつつ、
        //   event が持つ情報を利用して材料カテゴリのテーブルの順番カラムを UPDATE する。

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }

    // 材料カテゴリの削除を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoryDeleted): Ingredient {
        val currentVersion = ingredient.version
        val nextVersion = ingredient.version.next()

        // 省略
        //   version、nextVersion を用いて楽観的ロックをしつつ、
        //   event が持つ情報を利用して材料カテゴリのテーブルから DELETE し、材料カテゴリのテーブルの順番カラムを UPDATE する。

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }
}
