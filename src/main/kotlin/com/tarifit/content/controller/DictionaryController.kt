package com.tarifit.content.controller

import com.tarifit.content.domain.dictionary.DictionaryEntry
import com.tarifit.content.service.DictionaryService
import org.springframework.data.domain.Page
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
        return ResponseEntity.ok(dictionaryService.searchDictionary(q, type, page, size))
    }

    @GetMapping("/random")
    fun getRandomWords(
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<DictionaryEntry>> {
        return ResponseEntity.ok(dictionaryService.getRandomWords(type, count))
    }

    @GetMapping
    fun getAllDictionaryEntries(
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<DictionaryEntry>> {
        return ResponseEntity.ok(dictionaryService.getAllDictionaryEntries(type, page, size))
    }
}
