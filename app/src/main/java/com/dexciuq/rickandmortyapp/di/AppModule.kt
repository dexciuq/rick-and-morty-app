package com.dexciuq.rickandmortyapp.di

import com.dexciuq.rickandmortyapp.data.mapper.CharacterMapper
import com.dexciuq.rickandmortyapp.data.mapper.Mapper
import com.dexciuq.rickandmortyapp.data.model.CharacterDto
import com.dexciuq.rickandmortyapp.data.repository.CharacterRepositoryImpl
import com.dexciuq.rickandmortyapp.data.source.CharacterDataSource
import com.dexciuq.rickandmortyapp.data.source.remote.RemoteDataSource
import com.dexciuq.rickandmortyapp.data.source.remote.RickAndMortyApiService
import com.dexciuq.rickandmortyapp.domain.model.Character
import com.dexciuq.rickandmortyapp.domain.repository.CharacterRepository
import com.dexciuq.rickandmortyapp.domain.usecase.GetAllCharactersUseCase
import com.dexciuq.rickandmortyapp.presentation.screen.main.MainViewModel
import com.dexciuq.rickandmortyapp.common.Const
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

val appModule = module {

    // data source
    single<CharacterDataSource> {
        RemoteDataSource(apiService = get(), mapper = get())
    }

    // mapper
    single<Mapper<CharacterDto, Character>> {
        CharacterMapper()
    }

    // repository
    single<CharacterRepository> {
        CharacterRepositoryImpl(characterDataSource = get())
    }

    // network
    single<Converter.Factory> {
        GsonConverterFactory.create()
    }

    single {
        OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    single<RickAndMortyApiService> {
        Retrofit.Builder()
            .addConverterFactory(get())
            .client(get())
            .baseUrl(Const.BASE_URL)
            .build()
            .create()
    }

    // use case
    single { GetAllCharactersUseCase(characterRepository = get()) }

    // presentation
    viewModel {
        MainViewModel(getAllCharactersUseCase = get())
    }
}