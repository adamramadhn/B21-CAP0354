package com.akiramenaide.core.di

import androidx.room.Room
import com.akiramenaide.core.data.FruitRepository
import com.akiramenaide.core.data.source.local.LocalDataSource
import com.akiramenaide.core.data.source.local.room.FruitDatabase
import com.akiramenaide.core.data.source.remote.RemoteDataSource
import com.akiramenaide.core.data.source.remote.network.ApiService
import com.akiramenaide.core.domain.repository.IFruitRepository
import com.akiramenaide.core.util.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    factory { get<FruitDatabase>().fruitDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("akiramenaide".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            FruitDatabase::class.java, "Fruit.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IFruitRepository> {
        FruitRepository(get(), get(), get())
    }
}

val networkModule = module {
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.126.135.227:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}