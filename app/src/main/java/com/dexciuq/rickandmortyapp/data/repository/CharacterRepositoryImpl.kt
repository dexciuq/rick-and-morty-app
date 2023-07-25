package com.dexciuq.rickandmortyapp.data.repository

import com.dexciuq.rickandmortyapp.data.mapper.Mapper
import com.dexciuq.rickandmortyapp.data.model.CharacterDto
import com.dexciuq.rickandmortyapp.data.source.RickAndMortyApiService
import com.dexciuq.rickandmortyapp.domain.model.Character
import com.dexciuq.rickandmortyapp.domain.model.Filter
import com.dexciuq.rickandmortyapp.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val apiService: RickAndMortyApiService,
    private val mapper: Mapper<CharacterDto, Character>,
) : CharacterRepository {

    override suspend fun getCharacter(id: Int): Character = mapper.transform(
        apiService.getSingleCharacter(id)
    )

    override suspend fun getAllCharacters(): List<Character> = mapper.transformAll(
        apiService.getAllCharacters().results
    )

    override suspend fun getAllCharactersByFilter(filter: Filter): List<Character> = mapper.transformAll(
        apiService.getAllCharactersByFilter(
            name = filter.name,
            status = filter.status,
            species = filter.species,
            type = filter.type,
            gender = filter.gender,
        ).results
    )
}