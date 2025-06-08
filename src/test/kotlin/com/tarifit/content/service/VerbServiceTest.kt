package com.tarifit.content.service

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.repository.verb.VerbRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals

class VerbServiceTest {

    private lateinit var verbRepository: VerbRepository
    private lateinit var verbService: VerbService

    @BeforeEach
    fun setup() {
        verbRepository = mockk()
        verbService = VerbService(verbRepository)
    }

    @Test
    fun `searchVerbs should search by verb and translation`() {
        // Given
        val query = "run"
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            Verb(id = "1", verb = "azwel", translation = "run")
        ))
        every { 
            verbRepository.findByVerbOrTranslationContainingIgnoreCase(query, pageable) 
        } returns mockResult

        // When
        val result = verbService.searchVerbs(query, page, size)

        // Then
        assertEquals(mockResult, result)
        verify { 
            verbRepository.findByVerbOrTranslationContainingIgnoreCase(query, pageable) 
        }
    }

    @Test
    fun `getAllVerbs should return all verbs with pagination`() {
        // Given
        val page = 0
        val size = 20
        val pageable = PageRequest.of(page, size)
        val mockResult = PageImpl(listOf(
            Verb(id = "1", verb = "azwel", translation = "run")
        ))
        every { verbRepository.findAll(pageable) } returns mockResult

        // When
        val result = verbService.getAllVerbs(page, size)

        // Then
        assertEquals(mockResult, result)
        verify { verbRepository.findAll(pageable) }
    }

    @Test
    fun `getRandomVerbs should return random sample from pagination`() {
        // Given
        val count = 10
        val pageable = PageRequest.of(0, count)
        val mockContent = listOf(
            Verb(id = "1", verb = "azwel", translation = "run"),
            Verb(id = "2", verb = "ssiwel", translation = "walk")
        )
        val mockPage = PageImpl(mockContent)
        every { verbRepository.findAll(pageable) } returns mockPage

        // When
        val result = verbService.getRandomVerbs(count)

        // Then
        assertEquals(mockContent, result)
        verify { verbRepository.findAll(pageable) }
    }
}
