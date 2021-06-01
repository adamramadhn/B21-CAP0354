package com.akiramenaide.core.domain.usecase

import com.akiramenaide.core.data.source.remote.network.ApiResponse
import com.akiramenaide.core.data.source.remote.response.PostedImage
import com.akiramenaide.core.domain.model.Fruit
import com.akiramenaide.core.domain.repository.IFruitRepository
import kotlinx.coroutines.flow.Flow

class FruitInteractor(private val fruitRepository: IFruitRepository): FruitUseCase {
    override fun getAllFruits(): Flow<List<Fruit>> = fruitRepository.getAllFruits()

    override fun insertFruit(fruit: Fruit) = fruitRepository.insertFruit(fruit)

    override fun updateFruitInfo(fruit: Fruit) = fruitRepository.updateFruitInfo(fruit)

    override fun getPredict(predict: String): Flow<ApiResponse<PostedImage>> = fruitRepository.getPredict(predict)
}