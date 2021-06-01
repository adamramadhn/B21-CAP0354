package com.akiramenaide.core.data.source.remote

import com.akiramenaide.core.data.source.remote.network.ApiResponse
import com.akiramenaide.core.data.source.remote.network.ApiService
import com.akiramenaide.core.data.source.remote.response.PostedImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getPredict(predict: String): Flow<ApiResponse<PostedImage>> {
        return flow {
            try {
                val response = apiService.getPredict(predict)
                val dataResponse = response
                if (dataResponse.className.isNotEmpty()) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}