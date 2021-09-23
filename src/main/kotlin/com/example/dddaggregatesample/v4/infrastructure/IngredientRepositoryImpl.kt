package com.example.dddaggregatesample.v4.infrastructure

import com.example.dddaggregatesample.v4.domain.model.ingredient.*
import com.example.dddaggregatesample.v4.domain.model.recipe.RecipeId
import com.example.dddaggregatesample.v4.domain.support.OptimisticLockingError
import com.example.dddaggregatesample.v4.domain.support.OptimisticLockingVersion
import org.springframework.stereotype.Component

@Component
class IngredientRepositoryImpl : IngredientRepository {
    override fun findBy(recipeId: RecipeId): Ingredient? {
        val ingredient = TODO("省略（DB から復元する。）")

        // 下記 SQL の結果から集約を再構築するイメージ。
        /**
          SELECT
            *
          FROM
            recipe_ingredient AS ingredient
            left join recipe_ingredient_category AS category ON ingredient.id = category.recipe_ingredient_id
            left join recipe_ingredient_item AS item ON category.id = item.recipe_ingredient_category_id
          WHERE
            ingredient.recipe_id = {recipeId.value}
          ORDER BY
            category.order_no,
            item.order_no
          ;
         */

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
            is IngredientCategoryChanged -> save(ingredient, event)
            else -> throw IllegalArgumentException("イベントがサポートされていません。event: ${event.javaClass.name}")
        }
    }

    // 材料カテゴリの追加を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoryAdded): Ingredient {
        // 楽観的ロック
        val nextVersion = optimisticLocking(event.recipeId, ingredient.version)

        // 省略
        //   event が持つ情報を利用して材料カテゴリのテーブルに INSERT する。（イメージは下記の通り。）
        /**
          INSERT INTO recipe_ingredient_category(id, recipe_id, title, order_no)
          VALUES
          (
            {event.recipeId.value},
            {event.ingredientCategory.id.value},
            {event.ingredientCategory.title.value},
            {event.ordering}
          )
          ;
         */
        event.ingredientCategory.ingredientItems.forEachIndexed { index, it ->
            /**
              INSERT INTO recipe_ingredient_item(id, recipe_ingredient_category_id, name, quantity, order_no)
              VALUES
              (
                {ULID().nextULID()},
                {event.ingredientCategory.id.value},
                {it.name.value},
                {it.quantity.value},
                {index + 1}
              )
              ;
             */
        }

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }

    // 材料カテゴリの順番変更を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoriesReordered): Ingredient {
        // 楽観的ロック
        val nextVersion = optimisticLocking(event.recipeId, ingredient.version)

        // 省略
        //   event が持つ情報を利用して材料カテゴリのテーブルの順番カラムを UPDATE する。（イメージは下記の通り。）
        event.orderings.forEachIndexed { index, it ->
            /**
              UPDATE recipe_ingredient_category SET order_no = {index + 1} WHERE id = {it.value};
             */
        }

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }

    // 材料カテゴリの削除を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoryDeleted): Ingredient {
        // 楽観的ロック
        val nextVersion = optimisticLocking(event.recipeId, ingredient.version)

        // 省略
        //   event が持つ情報を利用して材料カテゴリのテーブルから DELETE し、材料カテゴリのテーブルの順番カラムを UPDATE する。（イメージは下記の通り。）
        /**
          DELETE FROM recipe_ingredient_category WHERE id = {event.ingredientCategoryId.value};
         */

        event.orderings.forEachIndexed { index, it ->
            /**
              UPDATE recipe_ingredient_category SET order_no = {index + 1} WHERE id = {it.value};
             */
        }

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }

    // 材料カテゴリの変更を保存する。
    private fun save(ingredient: Ingredient, event: IngredientCategoryChanged): Ingredient {
        // 楽観的ロック
        val nextVersion = optimisticLocking(event.recipeId, ingredient.version)

        // 省略
        //   event が持つ情報を利用して対象の材料カテゴリのテーブルを UPDATE する。（イメージは下記の通り。）
        /**
          DELETE FROM recipe_ingredient_item WHERE recipe_ingredient_category_id = {event.ingredientCategory.id.value};
         */
        event.ingredientCategory.ingredientItems.forEachIndexed { index, it ->
            /**
              INSERT INTO recipe_ingredient_item(id, recipe_ingredient_category_id, name, quantity, order_no)
              VALUES
              (
                {ULID().nextULID()},
                {event.ingredientCategory.id.value},
                {it.name.value},
                {it.quantity.value},
                {index + 1}
              )
              ;
             */
        }

        // 新たなバージョンで更新した集約を返却する。
        return ingredient.updateVersion(nextVersion)
    }

    private fun optimisticLocking(recipeId: RecipeId, version: OptimisticLockingVersion): OptimisticLockingVersion {
        val nextVersion = version.next()

        // 楽観的ロックする。（イメージは下記の通り。結果が 0 件なら OptimisticLockingError をスロー）
        /**
          UPDATE ingredient SET version = {nextVersion.value} WHERE recipe_id = {recipeId.value} AND version = {version.value};
         */

        return nextVersion
    }
}
