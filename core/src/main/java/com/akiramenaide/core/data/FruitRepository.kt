package com.akiramenaide.core.data

import android.util.Log
import com.akiramenaide.core.data.source.local.LocalDataSource
import com.akiramenaide.core.data.source.remote.RemoteDataSource
import com.akiramenaide.core.data.source.remote.network.ApiResponse
import com.akiramenaide.core.data.source.remote.response.PostedImage
import com.akiramenaide.core.domain.model.Fruit
import com.akiramenaide.core.domain.repository.IFruitRepository
import com.akiramenaide.core.util.AppExecutors
import com.akiramenaide.core.util.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class FruitRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val appExecutors: AppExecutors
    ): IFruitRepository {
    override fun getAllFruits(): Flow<List<Fruit>> =
        localDataSource.getAllFruits().map {
            DataMapper.mapEntitiesToDomain(it)
        }

    override fun insertFruit(fruit: Fruit) =
        appExecutors.diskIO().execute { localDataSource.insertFruit(DataMapper.mapDomainToEntity(fruit)) }

    override fun updateFruitInfo(fruit: Fruit) {
        val fruitEntity = DataMapper.mapDomainToEntity(fruit)
        Log.d("Repo", "${fruit.name}, ${fruit.total}, ${fruit.freshTotal}")
        Log.d("Repo", "${fruitEntity.name}, ${fruitEntity.total}, ${fruitEntity.freshTotal}")
        appExecutors.diskIO().execute {localDataSource.updateFruitInfo(fruitEntity) }

    }

    override fun getPredict(predict: String): Flow<ApiResponse<PostedImage>> {
        return runBlocking {
            remoteDataSource.getPredict(predict)
        }
    }
}