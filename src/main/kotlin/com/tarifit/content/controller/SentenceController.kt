package com.tarifit.content.controller

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.service.ContentService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/sentences")
@CrossOrigin(origins = ["*"])
class SentenceController(private val contentService: ContentService) {

    @GetMapping("/search")
    fun searchSentences(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Sentence>> {
        return ResponseEntity.ok(contentService.searchSentences(q, page, size))
    }

    @GetMapping
    fun getAllSentences(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Sentence>> {
        return ResponseEntity.ok(contentService.getAllSentences(page, size))
    }

    @GetMapping("/random")
    fun getRandomSentences(
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<Sentence>> {
        return ResponseEntity.ok(contentService.getRandomSentences(count))
    }
}
