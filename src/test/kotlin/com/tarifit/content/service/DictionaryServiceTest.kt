package com.tarifit.content.service

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryEntry
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.repository.dictionary.DictionaryAqeleiRepository
import com.tarifit.content.repository.dictionary.DictionaryWaryaghriRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun `searchAqelei should return paginated results`() {
        val entries = listOf(
            DictionaryAqelei(word = "test", translation = "test translation", type = "noun")
        )
        val page = PageImpl(entries, PageRequest.of(0, 20), 1)
        every { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase("test", any()) } returns page

        val result = dictionaryService.searchAqelei("test")

        assertEquals(1, result.totalElements)
        assertEquals("test", result.content[0].word)
        verify { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase("test", any()) }
    }

    @Test
    fun `searchWaryaghri should return paginated results`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "test", mot = "word", definitionFr = "definition", type = listOf("noun"))
        )
        val page = PageImpl(entries, PageRequest.of(0, 20), 1)
        every { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("test", any()) } returns page

        val result = dictionaryService.searchWaryaghri("test")

        assertEquals(1, result.totalElements)
        assertEquals("word", result.content[0].mot)
        verify { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("test", any()) }
    }

    @Test
    fun `getRandomAqeleiWords should return limited entries`() {
        val entries = listOf(
            DictionaryAqelei(word = "random1", translation = "translation1", type = "noun"),
            DictionaryAqelei(word = "random2", translation = "translation2", type = "verb")
        )
        val page = PageImpl(entries, PageRequest.of(0, 5), 2)
        every { aqeleiRepository.findAll(PageRequest.of(0, 5)) } returns page

        val result = dictionaryService.getRandomAqeleiWords(5)

        assertEquals(2, result.size)
        assertEquals("random1", result[0].word)
        verify { aqeleiRepository.findAll(PageRequest.of(0, 5)) }
    }

    @Test
    fun `getRandomWaryaghriWords should return limited entries`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "test1", mot = "word1", definitionFr = "def1", type = listOf("noun"))
        )
        val page = PageImpl(entries, PageRequest.of(0, 10), 1)
        every { waryaghriRepository.findAll(PageRequest.of(0, 10)) } returns page

        val result = dictionaryService.getRandomWaryaghriWords(10)

        assertEquals(1, result.size)
        assertEquals("word1", result[0].mot)
        verify { waryaghriRepository.findAll(PageRequest.of(0, 10)) }
    }

    @Test
    fun `getAllAqelei should return paginated results`() {
        val entries = listOf(
            DictionaryAqelei(word = "all1", translation = "trans1", type = "noun"),
            DictionaryAqelei(word = "all2", translation = "trans2", type = "verb")
        )
        val page = PageImpl(entries, PageRequest.of(0, 20), 2)
        every { aqeleiRepository.findAll(any<PageRequest>()) } returns page

        val result = dictionaryService.getAllAqelei()

        assertEquals(2, result.totalElements)
        assertEquals(2, result.content.size)
        verify { aqeleiRepository.findAll(any<PageRequest>()) }
    }

    @Test
    fun `getAllWaryaghri should return paginated results`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "all1", mot = "word1", definitionFr = "def1", type = listOf("noun"))
        )
        val page = PageImpl(entries, PageRequest.of(1, 10), 11) // Total 11, page 1 has 1 item
        every { waryaghriRepository.findAll(PageRequest.of(1, 10)) } returns page

        val result = dictionaryService.getAllWaryaghri(1, 10)

        assertEquals(11, result.totalElements) // Total elements is 11
        assertEquals(1, result.content.size) // Current page has 1 item
        assertEquals("word1", result.content[0].mot)
        verify { waryaghriRepository.findAll(PageRequest.of(1, 10)) }
    }

    @Test
    fun `searchDictionary should search waryaghri when type is waryaghri`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "test", mot = "search", definitionFr = "definition", type = listOf("noun"))
        )
        val page = PageImpl(entries, PageRequest.of(0, 20), 1)
        every { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("search", any()) } returns page

        val result = dictionaryService.searchDictionary("search", "waryaghri")

        assertEquals(1, result.totalElements)
        assertEquals("search", (result.content[0] as DictionaryWaryaghri).mot)
        verify { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("search", any()) }
    }

    @Test
    fun `searchDictionary should search aqelei when type is not waryaghri`() {
        val entries = listOf(
            DictionaryAqelei(word = "search", translation = "chercher", type = "verb")
        )
        val page = PageImpl(entries, PageRequest.of(0, 20), 1)
        every { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase("search", any()) } returns page

        val result = dictionaryService.searchDictionary("search", "aqelei")

        assertEquals(1, result.totalElements)
        assertEquals("search", (result.content[0] as DictionaryAqelei).word)
        verify { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase("search", any()) }
    }

    @Test
    fun `getRandomWords should return waryaghri words when type is waryaghri`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "rand", mot = "random", definitionFr = "aléatoire", type = listOf("adj"))
        )
        val page = PageImpl(entries, PageRequest.of(0, 15), 1)
        every { waryaghriRepository.findAll(PageRequest.of(0, 15)) } returns page

        val result = dictionaryService.getRandomWords("WARYAGHRI", 15)

        assertEquals(1, result.size)
        assertEquals("random", (result[0] as DictionaryWaryaghri).mot)
        verify { waryaghriRepository.findAll(PageRequest.of(0, 15)) }
    }

    @Test
    fun `getRandomWords should return aqelei words when type is not waryaghri`() {
        val entries = listOf(
            DictionaryAqelei(word = "random", translation = "aléatoire", type = "adj")
        )
        val page = PageImpl(entries, PageRequest.of(0, 8), 1)
        every { aqeleiRepository.findAll(PageRequest.of(0, 8)) } returns page

        val result = dictionaryService.getRandomWords("aqelei", 8)

        assertEquals(1, result.size)
        assertEquals("random", (result[0] as DictionaryAqelei).word)
        verify { aqeleiRepository.findAll(PageRequest.of(0, 8)) }
    }

    @Test
    fun `getAllDictionaryEntries should return waryaghri entries when type is waryaghri`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "all", mot = "tous", definitionFr = "everything", type = listOf("det"))
        )
        val page = PageImpl(entries, PageRequest.of(2, 5), 11) // Total 11, page 2 has 1 item
        every { waryaghriRepository.findAll(PageRequest.of(2, 5)) } returns page

        val result = dictionaryService.getAllDictionaryEntries("waryaghri", 2, 5)

        assertEquals(11, result.totalElements) // Total elements is 11
        assertEquals(1, result.content.size) // Current page has 1 item
        assertEquals("tous", (result.content[0] as DictionaryWaryaghri).mot)
        verify { waryaghriRepository.findAll(PageRequest.of(2, 5)) }
    }

    @Test
    fun `getAllDictionaryEntries should return aqelei entries when type is not waryaghri`() {
        val entries = listOf(
            DictionaryAqelei(word = "all", translation = "tous", type = "det")
        )
        val page = PageImpl(entries, PageRequest.of(0, 25), 1)
        every { aqeleiRepository.findAll(PageRequest.of(0, 25)) } returns page

        val result = dictionaryService.getAllDictionaryEntries("other", 0, 25)

        assertEquals(1, result.totalElements)
        assertEquals("all", (result.content[0] as DictionaryAqelei).word)
        verify { aqeleiRepository.findAll(PageRequest.of(0, 25)) }
    }

    @Test
    fun `searchAqelei with custom pagination should work correctly`() {
        val entries = listOf(
            DictionaryAqelei(word = "custom", translation = "personnalisé", type = "adj")
        )
        val page = PageImpl(entries, PageRequest.of(1, 5), 6)
        every { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase("custom", PageRequest.of(1, 5)) } returns page

        val result = dictionaryService.searchAqelei("custom", 1, 5)

        assertEquals(6, result.totalElements)
        assertEquals(1, result.content.size)
        verify { aqeleiRepository.findByWordOrTranslationContainingIgnoreCase("custom", PageRequest.of(1, 5)) }
    }

    @Test
    fun `searchWaryaghri with custom pagination should work correctly`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "cust", mot = "custom", definitionFr = "personnalisé", type = listOf("adj"))
        )
        val page = PageImpl(entries, PageRequest.of(2, 3), 10)
        every { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("custom", PageRequest.of(2, 3)) } returns page

        val result = dictionaryService.searchWaryaghri("custom", 2, 3)

        assertEquals(10, result.totalElements)
        assertEquals(1, result.content.size)
        verify { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("custom", PageRequest.of(2, 3)) }
    }

    @Test
    fun `searchDictionary should handle case-insensitive type matching`() {
        val entries = listOf(
            DictionaryWaryaghri(tifinagh = "test", mot = "CASE", definitionFr = "case test", type = listOf("noun"))
        )
        val page = PageImpl(entries, PageRequest.of(0, 20), 1)
        every { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("case", any()) } returns page

        val result = dictionaryService.searchDictionary("case", "WARYAGHRI")

        assertEquals(1, result.totalElements)
        verify { waryaghriRepository.findByMotOrDefinitionContainingIgnoreCase("case", any()) }
    }

    @Test
    fun `getRandomWords should handle case-insensitive type matching`() {
        val entries = listOf(
            DictionaryAqelei(word = "case", translation = "cas", type = "noun")
        )
        val page = PageImpl(entries, PageRequest.of(0, 12), 1)
        every { aqeleiRepository.findAll(PageRequest.of(0, 12)) } returns page

        val result = dictionaryService.getRandomWords("AQELEI", 12)

        assertEquals(1, result.size)
        verify { aqeleiRepository.findAll(PageRequest.of(0, 12)) }
    }

    @Test
    fun `getAllDictionaryEntries should handle empty results`() {
        val page = PageImpl(emptyList<DictionaryAqelei>(), PageRequest.of(5, 10), 0)
        every { aqeleiRepository.findAll(PageRequest.of(5, 10)) } returns page

        val result = dictionaryService.getAllDictionaryEntries("aqelei", 5, 10)

        assertEquals(0, result.totalElements)
        assertEquals(0, result.content.size)
        verify { aqeleiRepository.findAll(PageRequest.of(5, 10)) }
    }
}