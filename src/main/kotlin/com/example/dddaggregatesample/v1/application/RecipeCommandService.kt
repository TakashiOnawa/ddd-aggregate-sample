package com.example.dddaggregatesample.v1.application

import com.example.dddaggregatesample.v1.domain.model.ingredient.IngredientRepository
import com.example.dddaggregatesample.v1.domain.model.ingredientcategory.IngredientCategory
import com.example.dddaggregatesample.v1.domain.model.ingredientcategory.IngredientCategoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RecipeCommandService(
        private val ingredientRepository: IngredientRepository,
        private val ingredientCategoryRepository: IngredientCategoryRepository
) {
    // 材料カテゴリを追加する
    @Transactional
    fun handle(command: AddIngredientCategoryCommand) {
        val ingredient = ingredientRepository.findBy(command.recipeId)
                ?: throw IngredientNotFound()

        val ingredientCategory = IngredientCategory.create(command.title, command.ingredientItems)
        val newIngredient = ingredient.addCategory(ingredientCategory.id)

        ingredientCategoryRepository.save(ingredientCategory)
        ingredientRepository.save(newIngredient)
    }

    // 材料カテゴリを変更する
    @Transactional
    fun handle(command: ChangeIngredientCategoryCommand) {
        val ingredientCategory = ingredientCategoryRepository.findBy(command.id)
                ?: throw IngredientCategoryNotFound()

        val newIngredientCategory = ingredientCategory.change(command.title, command.ingredientItems)

        ingredientCategoryRepository.save(newIngredientCategory)
    }

    // 材料カテゴリを並び替える
    @Transactional
    fun handle(command: ReorderIngredientCategoriesCommand) {
        val ingredient = ingredientRepository.findBy(command.recipeId)
                ?: throw IngredientNotFound()

        val newIngredient = ingredient.reorderCategories(command.orderings)

        ingredientRepository.save(newIngredient)
    }

    // 材料カテゴリを削除する
    @Transactional
    fun handle(command: DeleteIngredientCategoryCommand) {
        val ingredient = ingredientRepository.findBy(command.recipeId)
                ?: throw IngredientNotFound()

        val ingredientCategory = ingredientCategoryRepository.findBy(command.id)
                ?: throw IngredientCategoryNotFound()

        val newIngredient = ingredient.deleteCategory(ingredientCategory.id)

        ingredientRepository.save(newIngredient)
        ingredientCategoryRepository.delete(ingredientCategory)
    }

    // その他のユースケースは省略
}
