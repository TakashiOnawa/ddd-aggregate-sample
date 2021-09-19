package com.example.dddaggregatesample.v2.infrastructure

import com.example.dddaggregatesample.v2.domain.model.ingredientcategory.*
import com.example.dddaggregatesample.v2.domain.support.OptimisticLockingError
import org.springframework.stereotype.Component

@Component
class IngredientCategoryRepositoryImpl : IngredientCategoryRepository {
    override fun findBy(id: IngredientCategoryId): IngredientCategory? {
        val ingredientCategory = TODO("省略（DB から復元する）")
        return ingredientCategory
    }

    /**
     * イベントの内容を用いて DB 更新する。
     * @throws OptimisticLockingError 楽観的ロックエラー
     */
    override fun save(ingredientCategory: IngredientCategory, event: IngredientCategoryEvent): IngredientCategory {
        // イベントごとに DB への保存処理を分ける。
        return when (event) {
            is IngredientCategoryChanged -> save(ingredientCategory, event)
            else -> throw IllegalArgumentException("イベントがサポートされていません。event: ${event.javaClass.name}")
        }
    }

    // 材料カテゴリの変更を保存する。
    private fun save(ingredientCategory: IngredientCategory, event: IngredientCategoryChanged): IngredientCategory {
        val version = ingredientCategory.version
        val nextVersion = ingredientCategory.version.next()

        // 省略
        //   version、nextVersion を用いて楽観的ロックをしつつ、
        //   event が持つ情報を利用して対象の材料カテゴリのテーブルを UPDATE する。

        // 新たなバージョンで更新した集約を返却する。
        return ingredientCategory.updateVersion(nextVersion)
    }
}
