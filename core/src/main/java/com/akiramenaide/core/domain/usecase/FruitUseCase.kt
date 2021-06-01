package com.akiramenaide.core.domain.usecase

import com.akiramenaide.core.data.source.remote.network.ApiResponse
import com.akiramenaide.core.data.source.remote.response.PostedImage
import com.akiramenaide.core.domain.model.Fruit
import kotlinx.coroutines.flow.Flow

interface FruitUseCase {
    fun getAllFruits(): Flow<List<Fruit>>
    fun insertFruit(fruit: Fruit)
    fun updateFruitInfo(fruit: Fruit)
    fun getPredict(predict: String): Flow<ApiResponse<PostedImage>>
}