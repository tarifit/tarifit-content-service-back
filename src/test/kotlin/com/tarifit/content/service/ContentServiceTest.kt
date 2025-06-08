package com.tarifit.content.service

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.domain.verb.Verb
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals

class ContentServiceTest {

    private lateinit var dictionaryService: DictionaryService
    private lateinit var sentenceService: SentenceService
    private lateinit var verbService: VerbService
    private lateinit var contentService: ContentService

    @BeforeEach
    fun setup() {
        dictionaryService = mockk()
        sentenceService = mockk()
        verbService = mockk()
        contentService = ContentService(dictionaryService, sentenceService, verbService)
    }

    @Test
    fun `searchDictionary should delegate to aqelei dictionary by default`() {
        // Given
        val query = "water"
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            DictionaryAqelei(id = "1", word = "eau", translation = "aman")
        ))
        every { dictionaryService.searchAqelei(query, page, size) } returns mockResult

        // When
        val result = contentService.searchDictionary(query, "aqelei", page, size)

        // Then
        assertEquals(mockResult, result)
        verify { dictionaryService.searchAqelei(query, page, size) }
    }

    @Test
    fun `searchDictionary should delegate to waryaghri dictionary when specified`() {
        // Given
        val query = "aman"
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau")
        ))
        every { dictionaryService.searchWaryaghri(query, page, size) } returns mockResult

        // When
        val result = contentService.searchDictionary(query, "waryaghri", page, size)

        // Then
        assertEquals(mockResult, result)
        verify { dictionaryService.searchWaryaghri(query, page, size) }
    }

    @Test
    fun `getRandomWords should delegate to aqelei dictionary by default`() {
        // Given
        val count = 5
        val mockResult = listOf(
            DictionaryAqelei(id = "1", word = "eau", translation = "aman")
        )
        every { dictionaryService.getRandomAqeleiWords(count) } returns mockResult

        // When
        val result = contentService.getRandomWords("aqelei", count)

        // Then
        assertEquals(mockResult, result)
        verify { dictionaryService.getRandomAqeleiWords(count) }
    }

    @Test
    fun `getRandomWords should delegate to waryaghri dictionary when specified`() {
        // Given
        val count = 5
        val mockResult = listOf(
            DictionaryWaryaghri(id = "1", mot = "aman", definitionFr = "eau")
        )
        every { dictionaryService.getRandomWaryaghriWords(count) } returns mockResult

        // When
        val result = contentService.getRandomWords("waryaghri", count)

        // Then
        assertEquals(mockResult, result)
        verify { dictionaryService.getRandomWaryaghriWords(count) }
    }

    @Test
    fun `searchSentences should delegate to sentence service`() {
        // Given
        val query = "hello"
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        ))
        every { sentenceService.searchSentences(query, page, size) } returns mockResult

        // When
        val result = contentService.searchSentences(query, page, size)

        // Then
        assertEquals(mockResult, result)
        verify { sentenceService.searchSentences(query, page, size) }
    }

    @Test
    fun `getAllSentences should delegate to sentence service`() {
        // Given
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        ))
        every { sentenceService.getAllSentences(page, size) } returns mockResult

        // When
        val result = contentService.getAllSentences(page, size)

        // Then
        assertEquals(mockResult, result)
        verify { sentenceService.getAllSentences(page, size) }
    }

    @Test
    fun `getRandomSentences should delegate to sentence service`() {
        // Given
        val count = 10
        val mockResult = listOf(
            Sentence(id = "1", englishSentence = "Hello", rifSentence = "Azul")
        )
        every { sentenceService.getRandomSentences(count) } returns mockResult

        // When
        val result = contentService.getRandomSentences(count)

        // Then
        assertEquals(mockResult, result)
        verify { sentenceService.getRandomSentences(count) }
    }

    @Test
    fun `searchVerbs should delegate to verb service`() {
        // Given
        val query = "run"
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            Verb(id = "1", verb = "azwel", translation = "run")
        ))
        every { verbService.searchVerbs(query, page, size) } returns mockResult

        // When
        val result = contentService.searchVerbs(query, page, size)

        // Then
        assertEquals(mockResult, result)
        verify { verbService.searchVerbs(query, page, size) }
    }

    @Test
    fun `getAllVerbs should delegate to verb service`() {
        // Given
        val page = 0
        val size = 20
        val mockResult = PageImpl(listOf(
            Verb(id = "1", verb = "azwel", translation = "run")
        ))
        every { verbService.getAllVerbs(page, size) } returns mockResult

        // When
        val result = contentService.getAllVerbs(page, size)

        // Then
        assertEquals(mockResult, result)
        verify { verbService.getAllVerbs(page, size) }
    }

    @Test
    fun `getRandomVerbs should delegate to verb service`() {
        // Given
        val count = 10
        val mockResult = listOf(
            Verb(id = "1", verb = "azwel", translation = "run")
        )
        every { verbService.getRandomVerbs(count) } returns mockResult

        // When
        val result = contentService.getRandomVerbs(count)

        // Then
        assertEquals(mockResult, result)
        verify { verbService.getRandomVerbs(count) }
    }
}
