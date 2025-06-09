package com.tarifit.content.service

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.repository.verb.VerbRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VerbServiceTest {

    private lateinit var verbRepository: VerbRepository
    private lateinit var verbService: VerbService

    @BeforeEach
    fun setup() {
        verbRepository = mockk()
        verbService = VerbService(verbRepository)
    }

    @Test
    fun `searchVerbs should return paginated results`() {
        val verbs = listOf(
            Verb(verb = "aca", translation = "sentir", conjugations = mapOf("necc" to mapOf("prétérit" to "uciɣ")))
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 1)
        every { verbRepository.findByVerbOrTranslationContainingIgnoreCase("aca", any()) } returns page

        val result = verbService.searchVerbs("aca")

        assertEquals(1, result.totalElements)
        assertEquals("aca", result.content[0].verb)
        verify { verbRepository.findByVerbOrTranslationContainingIgnoreCase("aca", any()) }
    }

    @Test
    fun `getAllVerbs should return paginated results`() {
        val verbs = listOf(
            Verb(verb = "verb1", translation = "translation1"),
            Verb(verb = "verb2", translation = "translation2")
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 2)
        every { verbRepository.findAll(any<PageRequest>()) } returns page

        val result = verbService.getAllVerbs()

        assertEquals(2, result.totalElements)
        assertEquals(2, result.content.size)
        verify { verbRepository.findAll(any<PageRequest>()) }
    }

    @Test
    fun `getVerbById should return verb when found`() {
        val verb = Verb(id = "1", verb = "test", translation = "test")
        every { verbRepository.findById("1") } returns Optional.of(verb)

        val result = verbService.getVerbById("1")

        assertEquals("test", result?.verb)
        verify { verbRepository.findById("1") }
    }

    @Test
    fun `getVerbById should return null when not found`() {
        every { verbRepository.findById("999") } returns Optional.empty()

        val result = verbService.getVerbById("999")

        assertNull(result)
        verify { verbRepository.findById("999") }
    }

    @Test
    fun `getRandomVerbs should return limited entries`() {
        val verbs = listOf(
            Verb(verb = "random1", translation = "trans1"),
            Verb(verb = "random2", translation = "trans2")
        )
        val page = PageImpl(verbs, PageRequest.of(0, 5), 2)
        every { verbRepository.findAll(PageRequest.of(0, 5)) } returns page

        val result = verbService.getRandomVerbs(5)

        assertEquals(2, result.size)
        assertEquals("random1", result[0].verb)
        verify { verbRepository.findAll(PageRequest.of(0, 5)) }
    }

    @Test
    fun `countAllVerbs should return total count`() {
        every { verbRepository.count() } returns 150L

        val result = verbService.countAllVerbs()

        assertEquals(150L, result)
        verify { verbRepository.count() }
    }

    @Test
    fun `searchVerbsByConjugation should return paginated results`() {
        val verbs = listOf(
            Verb(verb = "conj", translation = "conjugated", conjugations = mapOf("test" to mapOf("prétérit" to "value")))
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 1)
        every { verbRepository.findByConjugationsContaining("test", any()) } returns page

        val result = verbService.searchVerbsByConjugation("test")

        assertEquals(1, result.totalElements)
        assertEquals("conj", result.content[0].verb)
        verify { verbRepository.findByConjugationsContaining("test", any()) }
    }

    @Test
    fun `searchVerbsByParticipe should return paginated results`() {
        val verbs = listOf(
            Verb(verb = "part", translation = "with participe", participes = mapOf("test" to "value"))
        )
        val page = PageImpl(verbs, PageRequest.of(0, 20), 1)
        every { verbRepository.findByParticipesContaining("test", any()) } returns page

        val result = verbService.searchVerbsByParticipe("test")

        assertEquals(1, result.totalElements)
        assertEquals("part", result.content[0].verb)
        verify { verbRepository.findByParticipesContaining("test", any()) }
    }

    @Test
    fun `searchVerbs with custom pagination should work correctly`() {
        val verbs = listOf(
            Verb(verb = "custom", translation = "personnalisé")
        )
        val page = PageImpl(verbs, PageRequest.of(1, 5), 6)
        every { verbRepository.findByVerbOrTranslationContainingIgnoreCase("custom", PageRequest.of(1, 5)) } returns page

        val result = verbService.searchVerbs("custom", 1, 5)

        assertEquals(6, result.totalElements)
        assertEquals(1, result.content.size)
        verify { verbRepository.findByVerbOrTranslationContainingIgnoreCase("custom", PageRequest.of(1, 5)) }
    }

    @Test
    fun `getAllVerbs with custom pagination should work correctly`() {
        val verbs = listOf(
            Verb(verb = "paged", translation = "avec pagination")
        )
        val page = PageImpl(verbs, PageRequest.of(2, 10), 21) // Total 21, page 2 has 1 item
        every { verbRepository.findAll(PageRequest.of(2, 10)) } returns page

        val result = verbService.getAllVerbs(2, 10)

        assertEquals(21, result.totalElements) // Total elements is 21
        assertEquals(1, result.content.size)
        verify { verbRepository.findAll(PageRequest.of(2, 10)) }
    }

    @Test
    fun `getRandomVerbs with different count should work correctly`() {
        val verbs = listOf(
            Verb(verb = "rand1", translation = "random1"),
            Verb(verb = "rand2", translation = "random2"),
            Verb(verb = "rand3", translation = "random3")
        )
        val page = PageImpl(verbs, PageRequest.of(0, 15), 3)
        every { verbRepository.findAll(PageRequest.of(0, 15)) } returns page

        val result = verbService.getRandomVerbs(15)

        assertEquals(3, result.size)
        assertEquals("rand1", result[0].verb)
        verify { verbRepository.findAll(PageRequest.of(0, 15)) }
    }

    @Test
    fun `searchVerbsByConjugation with custom pagination should work correctly`() {
        val verbs = listOf(
            Verb(verb = "conj2", translation = "conjugated2")
        )
        val page = PageImpl(verbs, PageRequest.of(1, 8), 9) // Total 9, page 1 has 1 item
        every { verbRepository.findByConjugationsContaining("pattern", PageRequest.of(1, 8)) } returns page

        val result = verbService.searchVerbsByConjugation("pattern", 1, 8)

        assertEquals(9, result.totalElements) // Total elements is 9
        assertEquals(1, result.content.size)
        verify { verbRepository.findByConjugationsContaining("pattern", PageRequest.of(1, 8)) }
    }

    @Test
    fun `searchVerbsByParticipe with custom pagination should work correctly`() {
        val verbs = listOf(
            Verb(verb = "part2", translation = "participe2")
        )
        val page = PageImpl(verbs, PageRequest.of(0, 12), 1) // Total 1, page 0 has 1 item
        every { verbRepository.findByParticipesContaining("participe", PageRequest.of(0, 12)) } returns page

        val result = verbService.searchVerbsByParticipe("participe", 0, 12)

        assertEquals(1, result.totalElements) // Total elements is 1
        assertEquals(1, result.content.size)
        verify { verbRepository.findByParticipesContaining("participe", PageRequest.of(0, 12)) }
    }

    @Test
    fun `searchVerbs should handle empty results`() {
        val page = PageImpl(emptyList<Verb>(), PageRequest.of(0, 20), 0)
        every { verbRepository.findByVerbOrTranslationContainingIgnoreCase("nonexistent", any()) } returns page

        val result = verbService.searchVerbs("nonexistent")

        assertEquals(0, result.totalElements)
        assertEquals(0, result.content.size)
        verify { verbRepository.findByVerbOrTranslationContainingIgnoreCase("nonexistent", any()) }
    }
}