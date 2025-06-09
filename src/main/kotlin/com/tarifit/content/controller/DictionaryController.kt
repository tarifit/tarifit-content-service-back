package com.tarifit.content.controller

import com.tarifit.content.domain.dictionary.DictionaryEntry
import com.tarifit.content.service.DictionaryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/dictionary")
@CrossOrigin(origins = ["*"])
class DictionaryController(private val dictionaryService: DictionaryService) {

    @GetMapping("/search")
    fun searchDictionary(
        @RequestParam q: String,
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<DictionaryEntry>> {
        val result: Page<DictionaryEntry> = if (type.lowercase() == "waryaghri") {
            val waryaghriResult = dictionaryService.searchWaryaghri(q, page, size)
            PageImpl(waryaghriResult.content.map { it as DictionaryEntry }, waryaghriResult.pageable, waryaghriResult.totalElements)
        } else {
            val aqeleiResult = dictionaryService.searchAqelei(q, page, size)
            PageImpl(aqeleiResult.content.map { it as DictionaryEntry }, aqeleiResult.pageable, aqeleiResult.totalElements)
        }
        return ResponseEntity.ok(result)
    }

    @GetMapping("/random")
    fun getRandomWords(
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<DictionaryEntry>> {
        val result: List<DictionaryEntry> = if (type.lowercase() == "waryaghri") {
            dictionaryService.getRandomWaryaghriWords(count).map { it as DictionaryEntry }
        } else {
            dictionaryService.getRandomAqeleiWords(count).map { it as DictionaryEntry }
        }
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getAllDictionaryEntries(
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<DictionaryEntry>> {
        val result: Page<DictionaryEntry> = if (type.lowercase() == "waryaghri") {
            val waryaghriResult = dictionaryService.getAllWaryaghri(page, size)
            PageImpl(waryaghriResult.content.map { it as DictionaryEntry }, waryaghriResult.pageable, waryaghriResult.totalElements)
        } else {
            val aqeleiResult = dictionaryService.getAllAqelei(page, size)
            PageImpl(aqeleiResult.content.map { it as DictionaryEntry }, aqeleiResult.pageable, aqeleiResult.totalElements)
        }
        return ResponseEntity.ok(result)
    }
}
