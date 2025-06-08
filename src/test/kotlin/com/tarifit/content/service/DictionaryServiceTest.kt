package com.tarifit.content.service

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.repository.dictionary.DictionaryAqeleiRepository
import com.tarifit.content.repository.dictionary.DictionaryWaryaghriRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import kotlin.test.assertEquals

class DictionaryServiceTest {

    private lateinit var aqeleiRepository: DictionaryAqeleiRepository
    private lateinit var waryaghriRepository: DictionaryWaryaghriRepository
    private lateinit var dictionaryService: DictionaryService

    @BeforeEach
    fun setup() {
        aqeleiRepository = mockk()
        waryaghriRepository = mockk()
        dictionaryService = DictionaryService(aqeleiRepository, waryaghriRepository)
    }

    @Test
    fun `searchAqelei should search by word and translation`() {
        // Given
        val query = "water"
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            DictionaryAqelei(id = "1", word = "eau", translation = "aman")
        ))
        every { 
            aqeleiRepository.findByWordOrTranslationContainingIgnoreCase(query, pageable) 
        } returns mockResult

        // When
        val result = dictionaryService.searchAqelei(query, page, size)

        // Then
        assertEquals(mockResult, result)
        verify { 
            aqeleiRepository.findByWordOrTranslationContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `searchWaryaghri should search by mot and definition`() {
        // Given
        val query = "aman"
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau")
        ))
        every { 
            waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase(query, pageable) 
        } returns mockResult

        // When
        val result = dictionaryService.searchWaryaghri(query, page, size)

        // Then
        assertEquals(mockResult, result)
        verify { 
            waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `getAllAqelei should return all entries with pagination`() {
        // Given
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            DictionaryAqelei(id = "1", word = "eau", translation = "aman")
        ))
        every { aqeleiRepository.findAll(pageable) } returns mockResult

        // When
        val result = dictionaryService.getAllAqelei(page, size)

        // Then
        assertEquals(mockResult, result)
        verify { aqeleiRepository.findAll(pageable) }
    }

    @Test
    fun `getAllWaryaghri should return all entries with pagination`() {
        // Given
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau")
        ))
        every { waryaghriRepository.findAll(pageable) } returns mockResult

        // When
        val result = dictionaryService.getAllWaryaghri(page, size)

        // Then
        assertEquals(mockResult, result)
        verify { waryaghriRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomAqeleiWords should return random sample from pagination`() {
        // Given
        val count = 5
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            DictionaryAqelei(id = "1", word = "eau", translation = "aman"),
            DictionaryAqelei(id = "2", word = "pain", translation = "aghrum")
        )
        val mockPage = PageImpl(mockContent)
        every { aqeleiRepository.findAll(pageable) } returns mockPage

        // When
        val result = dictionaryService.getRandomAqeleiWords(count)

        // Then
        assertEquals(mockContent, result)
        verify { aqeleiRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomWaryaghriWords should return random sample from pagination`() {
        // Given
        val count = 5
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau"),
            DictionaryWaryaghri(id = "2", mot = "aghrum", definitionFr = "pain")
        )
        val mockPage = PageImpl(mockContent)
        every { waryaghriRepository.findAll(pageable) } returns mockPage

        // When
        val result = dictionaryService.getRandomWaryaghriWords(count)

        // Then
        assertEquals(mockContent, result)
        verify { waryaghriRepository.findAll(pageable) }
    }
}
