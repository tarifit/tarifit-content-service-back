package com.tarifit.content.controller

import com.tarifit.content.domain.story.Story
import com.tarifit.content.service.StoryService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content/stories")
@CrossOrigin(origins = ["*"])
class StoryController(private val storyService: StoryService) {

    @GetMapping("/search")
    fun searchStories(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Story>> {
        return ResponseEntity.ok(storyService.searchStories(q, page, size))
    }

    @GetMapping
    fun getAllStories(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Story>> {
        return ResponseEntity.ok(storyService.getAllStories(page, size))
    }

    @GetMapping("/random")
    fun getRandomStories(
        @RequestParam(defaultValue = "10") count: Int
    ): ResponseEntity<List<Story>> {
        return ResponseEntity.ok(storyService.getRandomStories(count))
    }

    @GetMapping("/category/{category}")
    fun getStoriesByCategory(
        @PathVariable category: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Story>> {
        return ResponseEntity.ok(storyService.getStoriesByCategory(category, page, size))
    }

    @GetMapping("/difficulty/{difficulty}")
    fun getStoriesByDifficulty(
        @PathVariable difficulty: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Story>> {
        return ResponseEntity.ok(storyService.getStoriesByDifficulty(difficulty, page, size))
    }

    @GetMapping("/author/{author}")
    fun getStoriesByAuthor(
        @PathVariable author: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Story>> {
        return ResponseEntity.ok(storyService.getStoriesByAuthor(author, page, size))
    }

    @GetMapping("/theme/{theme}")
    fun getStoriesByTheme(
        @PathVariable theme: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<Story>> {
        return ResponseEntity.ok(storyService.getStoriesByTheme(theme, page, size))
    }

    @GetMapping("/{id}")
    fun getStoryById(@PathVariable id: String): ResponseEntity<Story> {
        val story = storyService.getStoryById(id)
        return if (story != null) {
            ResponseEntity.ok(story)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
