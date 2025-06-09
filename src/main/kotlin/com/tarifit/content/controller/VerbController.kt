package com.tarifit.content.controller

import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.service.VerbService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/verbs")
@CrossOrigin(origins = ["*"])
class VerbController(private val verbService: VerbService) {

    @GetMapping("/search")
    fun searchVerbs(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Verb>> {
        return ResponseEntity.ok(verbService.searchVerbs(q, page, size))
    }

    @GetMapping
    fun getAllVerbs(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Verb>> {
        return ResponseEntity.ok(verbService.getAllVerbs(page, size))
    }

    @GetMapping("/{id}")
    fun getVerbById(@PathVariable id: String): ResponseEntity<Verb> {
        val verb = verbService.getVerbById(id)
        return if (verb != null) {
            ResponseEntity.ok(verb)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/random")
    fun getRandomVerbs(
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<Verb>> {
        return ResponseEntity.ok(verbService.getRandomVerbs(count))
    }

    @GetMapping("/count")
    fun getVerbCount(): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(mapOf("count" to verbService.countAllVerbs()))
    }

    @GetMapping("/search/conjugation")
    fun searchByConjugation(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Verb>> {
        return ResponseEntity.ok(verbService.searchVerbsByConjugation(q, page, size))
    }

    @GetMapping("/search/participe")
    fun searchByParticipe(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Verb>> {
        return ResponseEntity.ok(verbService.searchVerbsByParticipe(q, page, size))
    }
}