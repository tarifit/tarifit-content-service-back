package com.tarifit.content.controller

import com.tarifit.content.service.ContentService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/dictionary")
@CrossOrigin(origins = ["*"])
class DictionaryController(private val contentService: ContentService) {

    @GetMapping("/search")
    fun searchDictionary(
        @RequestParam q: String,
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<*>> {
        return ResponseEntity.ok(contentService.searchDictionary(q, type, page, size))
    }

    @GetMapping("/random")
    fun getRandomWords(
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<*>> {
        return ResponseEntity.ok(contentService.getRandomWords(type, count))
    }

    @GetMapping
    fun getAllDictionaryEntries(
        @RequestParam(defaultValue = "aqelei") type: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<*>> {
        return ResponseEntity.ok(contentService.getAllDictionaryEntries(type, page, size))
    }
}
