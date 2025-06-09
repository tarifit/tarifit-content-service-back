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

    @Test
    fun `searchAqelei with custom pagination should work correctly`() {
        val query = "test"
        val page = 1
        val size = 10
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(emptyList<DictionaryAqelei>())
        every { 
            aqeleiRepository.findByWordOrTranslationContainingIgnoreCase(query, pageable) 
        } returns mockResult

        val result = dictionaryService.searchAqelei(query, page, size)

        assertEquals(0, result.totalElements)
        verify { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase(query, pageable) }
    }

    @Test
    fun `searchWaryaghri with custom pagination should work correctly`() {
        val query = "test"
        val page = 2
        val size = 5
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(emptyList<DictionaryWaryaghri>())
        every { 
            waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase(query, pageable) 
        } returns mockResult

        val result = dictionaryService.searchWaryaghri(query, page, size)

        assertEquals(0, result.totalElements)
        verify { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase(query, pageable) }
    }

    @Test
    fun `getAllAqelei with custom pagination should work correctly`() {
        val page = 3
        val size = 15
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            DictionaryAqelei(id = "1", word = "test", translation = "test")
        ))
        every { aqeleiRepository.findAll(pageable) } returns mockResult

        val result = dictionaryService.getAllAqelei(page, size)

        assertEquals(1, result.totalElements)
        verify { aqeleiRepository.findAll(pageable) }
    }

    @Test
    fun `getAllWaryaghri with custom pagination should work correctly`() {
        val page = 1
        val size = 25
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            DictionaryWaryaghri(id = "1", mot = "test", definitionFr = "test")
        ))
        every { waryaghriRepository.findAll(pageable) } returns mockResult

        val result = dictionaryService.getAllWaryaghri(page, size)

        assertEquals(1, result.totalElements)
        verify { waryaghriRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomAqeleiWords with different count should work correctly`() {
        val count = 15
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            DictionaryAqelei(id = "1", word = "random1", translation = "random1"),
            DictionaryAqelei(id = "2", word = "random2", translation = "random2"),
            DictionaryAqelei(id = "3", word = "random3", translation = "random3")
        )
        val mockPage = PageImpl(mockContent)
        every { aqeleiRepository.findAll(pageable) } returns mockPage

        val result = dictionaryService.getRandomAqeleiWords(count)

        assertEquals(3, result.size)
        verify { aqeleiRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomWaryaghriWords with different count should work correctly`() {
        val count = 8
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            DictionaryWaryaghri(id = "1", mot = "random1", definitionFr = "random1")
        )
        val mockPage = PageImpl(mockContent)
        every { waryaghriRepository.findAll(pageable) } returns mockPage

        val result = dictionaryService.getRandomWaryaghriWords(count)

        assertEquals(1, result.size)
        verify { waryaghriRepository.findAll(pageable) }
    }
}
