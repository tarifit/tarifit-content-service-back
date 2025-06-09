package com.tarifit.content.controller

import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.service.SentenceService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/sentences")
@CrossOrigin(origins = ["*"])
class SentenceController(private val sentenceService: SentenceService) {


    @GetMapping("/search")
    fun searchSentences(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Sentence>> {
        return ResponseEntity.ok(sentenceService.searchSentences(q, page, size))
    }

    @GetMapping("/search/english")
    fun searchByEnglish(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Sentence>> {
        return ResponseEntity.ok(sentenceService.searchByEnglish(q, page, size))
    }

    @GetMapping("/search/rif")
    fun searchByRif(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Sentence>> {
        return ResponseEntity.ok(sentenceService.searchByRif(q, page, size))
    }

    @GetMapping("/{id}")
    fun getSentenceById(@PathVariable id: String): ResponseEntity<Sentence> {
        val sentence = sentenceService.getSentenceById(id)
        return if (sentence != null) {
            ResponseEntity.ok(sentence)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/count")
    fun getSentenceCount(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(mapOf("count" to sentenceService.countAllSentences()))
    }

    @GetMapping
    fun getAllSentences(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Sentence>> {
        return ResponseEntity.ok(sentenceService.getAllSentences(page, size))
    }

    @GetMapping("/random")
    fun getRandomSentences(
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<Sentence>> {
        return ResponseEntity.ok(sentenceService.getRandomSentences(count))
    }
}
