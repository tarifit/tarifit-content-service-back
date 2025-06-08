package com.tarifit.content.controller

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.service.ContentService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/verbs")
@CrossOrigin(origins = ["*"])
class VerbController(private val contentService: ContentService) {

    @GetMapping("/search")
    fun searchVerbs(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Verb>> {
        return ResponseEntity.ok(contentService.searchVerbs(q, page, size))
    }

    @GetMapping
    fun getAllVerbs(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Verb>> {
        return ResponseEntity.ok(contentService.getAllVerbs(page, size))
    }

    @GetMapping("/random")
    fun getRandomVerbs(
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<Verb>> {
        return ResponseEntity.ok(contentService.getRandomVerbs(count))
    }
}
