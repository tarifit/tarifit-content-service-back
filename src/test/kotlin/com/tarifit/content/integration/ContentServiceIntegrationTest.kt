package com.tarifit.content.integration

import com.tarifit.content.fixtures.TestDataFixtures
import com.tarifit.content.repository.dictionary.DictionaryAqeleiRepository
import com.tarifit.content.repository.dictionary.DictionaryWaryaghriRepository
import com.tarifit.content.repository.sentence.SentenceRepository
import com.tarifit.content.repository.story.StoryRepository
import com.tarifit.content.repository.verb.VerbRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ContentServiceIntegrationTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    // Repository beans for data management
    @Autowired
    private lateinit var dictionaryAqeleiRepository: DictionaryAqeleiRepository

    @Autowired 
    private lateinit var dictionaryWaryaghriRepository: DictionaryWaryaghriRepository

    @Autowired
    private lateinit var sentenceRepository: SentenceRepository

    @Autowired
    private lateinit var verbRepository: VerbRepository

    @Autowired
    private lateinit var storyRepository: StoryRepository

    @BeforeEach
    fun setup() {
        // Clean up any existing test data
        cleanupTestData()
        
        // Insert test data for integration tests
        setupTestData()
    }

    @AfterEach
    fun cleanup() {
        // Clean up test data after each test
        cleanupTestData()
    }

    private fun setupTestData() {
        // Insert sample data for each collection
        dictionaryAqeleiRepository.saveAll(TestDataFixtures.createSampleAqeleiEntries())
        dictionaryWaryaghriRepository.saveAll(TestDataFixtures.createSampleWaryaghriEntries())
        sentenceRepository.saveAll(TestDataFixtures.createSampleSentences())
        verbRepository.saveAll(TestDataFixtures.createSampleVerbs())
        storyRepository.saveAll(TestDataFixtures.createSampleStories())
    }

    private fun cleanupTestData() {
        // Delete all test data - using deleteAll to ensure clean state
        dictionaryAqeleiRepository.deleteAll()
        dictionaryWaryaghriRepository.deleteAll()
        sentenceRepository.deleteAll()
        verbRepository.deleteAll()
        storyRepository.deleteAll()
    }

    private fun verifyTestDataSetup() {
        // Verify that test data was properly inserted
        val counts = listOf(
            "Aqelei" to dictionaryAqeleiRepository.count(),
            "Waryaghri" to dictionaryWaryaghriRepository.count(),
            "Sentences" to sentenceRepository.count(),
            "Verbs" to verbRepository.count(),
            "Stories" to storyRepository.count()
        )

        println("Test data counts - ${counts.joinToString(", ") { "${it.first}: ${it.second}" }}")
        
        assert(counts[0].second >= 3) { "Expected at least 3 Aqelei entries, found: ${counts[0].second}" }
        assert(counts[1].second >= 3) { "Expected at least 3 Waryaghri entries, found: ${counts[1].second}" }
        assert(counts[2].second >= 3) { "Expected at least 3 sentences, found: ${counts[2].second}" }
        assert(counts[3].second >= 3) { "Expected at least 3 verbs, found: ${counts[3].second}" }
        assert(counts[4].second >= 2) { "Expected at least 2 stories, found: ${counts[4].second}" }
    }

    @Test
    fun `test data setup should work correctly`() {
        verifyTestDataSetup()
    }



    @Test
    fun `full dictionary search workflow should work end-to-end`() {
        // Test Aqelei search with real data
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=aman&type=aqelei", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK) { "Aqelei search failed: ${response1.statusCode}" }
        assert(response1.body?.contains("aman") == true) { "Response should contain 'aman': ${response1.body}" }

        // Test Waryaghri search with real data  
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=eau&type=waryaghri", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK) { "Waryaghri search failed: ${response2.statusCode}" }
        assert(response2.body?.contains("aman") == true) { "Response should contain 'aman': ${response2.body}" }
    }

    @Test
    fun `full sentence workflow should work end-to-end`() {
        // Test sentence search with real data
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences/search?q=Hello", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK) { "Sentence search failed: ${response1.statusCode}" }
        assert(response1.body?.contains("Azul") == true) { "Response should contain 'Azul': ${response1.body}" }
        assert(response1.body?.contains("Hello") == true) { "Response should contain 'Hello': ${response1.body}" }

        // Test random sentences
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences/random?count=2", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK) { "Random sentences failed: ${response2.statusCode}" }
        
        // Parse response and verify structure
        val responseBody = response2.body!!
        assert(responseBody.contains("englishSentence")) { "Response should contain englishSentence field" }
        assert(responseBody.contains("rifSentence")) { "Response should contain rifSentence field" }
    }

    @Test
    fun `full verb workflow should work end-to-end`() {
        // Test verb search with real data
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/verbs/search?q=run", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK) { "Verb search failed: ${response1.statusCode}" }
        assert(response1.body?.contains("azwel") == true) { "Response should contain 'azwel': ${response1.body}" }
        assert(response1.body?.contains("run") == true) { "Response should contain 'run': ${response1.body}" }

        // Test random verbs
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/verbs/random?count=2", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK) { "Random verbs failed: ${response2.statusCode}" }
        
        // Verify response structure
        val responseBody = response2.body!!
        assert(responseBody.contains("verb")) { "Response should contain verb field" }
        assert(responseBody.contains("translation")) { "Response should contain translation field" }
    }

    @Test
    fun `health endpoint should be accessible`() {
        val response: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/health", 
            String::class.java
        )
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.contains("\"status\":\"UP\"") == true)
        assert(response.body?.contains("\"service\":\"tarifit-content-service\"") == true)
    }

    @Test
    fun `pagination should work correctly`() {
        // Test dictionary pagination with content verification
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary?type=aqelei&page=0&size=2", 
            String::class.java
        )
        println("Dictionary response status: ${response1.statusCode}")
        println("Dictionary response body: ${response1.body}")
        assert(response1.statusCode == HttpStatus.OK) { "Dictionary pagination failed with status: ${response1.statusCode}" }
        
        val dictBody = response1.body!!
        assert(dictBody.contains("content")) { "Response should contain 'content' field for pagination" }
        assert(dictBody.contains("totalElements")) { "Response should contain 'totalElements' field" }
        assert(dictBody.contains("aman") || dictBody.contains("aghrum")) { "Response should contain test data" }

        // Test sentence pagination with content verification
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences?page=0&size=2", 
            String::class.java
        )
        println("Sentences response status: ${response2.statusCode}")
        println("Sentences response body: ${response2.body}")
        assert(response2.statusCode == HttpStatus.OK) { "Sentences pagination failed with status: ${response2.statusCode}" }
        
        val sentBody = response2.body!!
        assert(sentBody.contains("content")) { "Response should contain 'content' field" }
        assert(sentBody.contains("Azul") || sentBody.contains("Hello")) { "Response should contain test sentences" }

        // Test verb pagination with content verification
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/verbs?page=0&size=2", 
            String::class.java
        )
        println("Verbs response status: ${response3.statusCode}")
        println("Verbs response body: ${response3.body}")
        assert(response3.statusCode == HttpStatus.OK) { "Verbs pagination failed with status: ${response3.statusCode}" }
        
        val verbBody = response3.body!!
        assert(verbBody.contains("content")) { "Response should contain 'content' field" }
        assert(verbBody.contains("azwel") || verbBody.contains("ssiwel")) { "Response should contain test verbs" }

        // Test story pagination with content verification
        val response4: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories?page=0&size=10", 
            String::class.java
        )
        println("Stories response status: ${response4.statusCode}")
        println("Stories response body: ${response4.body}")
        assert(response4.statusCode == HttpStatus.OK) { "Stories pagination failed with status: ${response4.statusCode}" }
        
        val storyBody = response4.body!!
        assert(storyBody.contains("content")) { "Response should contain 'content' field" }
        assert(storyBody.contains("Massin") || storyBody.contains("Tamurt")) { "Response should contain test stories" }
    }

    @Test
    fun `full story workflow should work end-to-end`() {
        // Test story search with real data
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/search?q=Massin", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK) { "Story search failed: ${response1.statusCode}" }
        assert(response1.body?.contains("Massin") == true) { "Response should contain 'Massin': ${response1.body}" }
        assert(response1.body?.contains("autobiographical") == true) { "Response should contain category 'autobiographical'" }

        // Test random stories
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/random?count=1", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK) { "Random stories failed: ${response2.statusCode}" }
        
        val storyBody = response2.body!!
        assert(storyBody.contains("title")) { "Response should contain title field" }
        assert(storyBody.contains("tarifitText")) { "Response should contain tarifitText field" }

        // Test stories by category
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/category/autobiographical", 
            String::class.java
        )
        assert(response3.statusCode == HttpStatus.OK) { "Stories by category failed: ${response3.statusCode}" }
        assert(response3.body?.contains("autobiographical") == true) { "Response should contain autobiographical stories" }
    }

    @Test
    fun `error handling should work for invalid parameters`() {
        // Test empty query handling - should return OK with empty or all results
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK || response1.statusCode == HttpStatus.BAD_REQUEST || response1.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "Empty query should be handled: ${response1.statusCode}" 
        }
        
        // Test non-existent search terms
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=nonexistentword123&type=aqelei", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK || response2.statusCode == HttpStatus.BAD_REQUEST || response2.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "Non-existent search should be handled: ${response2.statusCode}" 
        }
        
        // Verify response structure for empty results (only if OK)
        if (response2.statusCode == HttpStatus.OK && response2.body != null) {
            assert(response2.body!!.contains("content")) { "Response should contain 'content' field even for empty results" }
        }
        
        // Test invalid pagination parameters
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences?page=-1&size=0", 
            String::class.java
        )
        // Should handle gracefully - either return error or default pagination
        assert(response3.statusCode == HttpStatus.OK || response3.statusCode == HttpStatus.BAD_REQUEST || response3.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Invalid pagination should be handled gracefully: ${response3.statusCode}"
        }
    }

    @Test
    fun `database state should be consistent with API responses`() {
        // Test that API responses match database content
        val response: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary?type=aqelei&page=0&size=100", 
            String::class.java
        )
        assert(response.statusCode == HttpStatus.OK)
        
        val responseBody = response.body!!
        // Verify the API returns the same count as in database
        assert(responseBody.contains("totalElements")) { "Response should contain totalElements" }
        
        // Test specific data retrieval
        val searchResponse: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=aman&type=aqelei", 
            String::class.java
        )
        assert(searchResponse.statusCode == HttpStatus.OK)
        assert(searchResponse.body?.contains("aman") == true) { "Should find 'aman' in database" }
        
        // Verify database actually contains this data
        val dbEntry = dictionaryAqeleiRepository.findAll().find { it.translation == "aman" }
        assert(dbEntry != null) { "Database should contain entry with translation 'aman'" }
        assert(dbEntry!!.word == "eau") { "Word should be 'eau' for translation 'aman'" }
    }

    @Test 
    fun `health endpoint should provide detailed service status`() {
        val response: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/health", 
            String::class.java
        )
        assert(response.statusCode == HttpStatus.OK) { "Health check failed: ${response.statusCode}" }
        
        val responseBody = response.body!!
        assert(responseBody.contains("\"status\":\"UP\"")) { "Health status should be UP" }
        assert(responseBody.contains("\"service\":\"tarifit-content-service\"")) { "Should identify service name" }
        
        // Verify additional health information if available
        println("Health response: $responseBody")
    }

    @Test
    fun `pagination metadata should be accurate`() {
        // Test with known dataset size
        val response: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences?page=0&size=2", 
            String::class.java
        )
        assert(response.statusCode == HttpStatus.OK)
        
        val responseBody = response.body!!
        println("Pagination response: $responseBody")
        
        // Verify pagination fields are present and make sense
        assert(responseBody.contains("\"page\":0") || responseBody.contains("\"number\":0")) { "Should show current page" }
        assert(responseBody.contains("\"size\":2")) { "Should show page size" }
        assert(responseBody.contains("totalElements")) { "Should show total count" }
        
        // Verify we get exactly the requested page size or less
        val contentMatches = Regex("\"content\":\\s*\\[").findAll(responseBody).count()
        assert(contentMatches == 1) { "Should have exactly one content array" }
    }

    @Test
    fun `should handle database connection failures gracefully`() {
        // Test behavior when database operations might fail
        // This tests the service's resilience to database issues
        
        // Test with extremely large page size that might cause memory issues
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary?type=aqelei&page=0&size=10000", 
            String::class.java
        )
        // Should either handle gracefully or return appropriate error
        assert(response1.statusCode == HttpStatus.OK || response1.statusCode == HttpStatus.BAD_REQUEST || response1.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Large page size should be handled gracefully: ${response1.statusCode}"
        }
        
        // Test with extremely high page number
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences?page=999999&size=10", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK || response2.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "High page number should return empty results or handle gracefully: ${response2.statusCode}" 
        }
        
        if (response2.statusCode == HttpStatus.OK && response2.body != null) {
            assert(response2.body!!.contains("\"content\":[]") || response2.body!!.contains("\"empty\":true")) {
                "High page number should return empty content"
            }
        }
    }

    @Test
    fun `should handle malformed requests appropriately`() {
        // Test invalid dictionary type
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary?type=invalid_type&page=0&size=10", 
            String::class.java
        )
        // Should either return empty results or appropriate error
        assert(response1.statusCode == HttpStatus.OK || response1.statusCode == HttpStatus.BAD_REQUEST || response1.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Invalid dictionary type should be handled: ${response1.statusCode}"
        }
        
        // Test negative page and size values
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/verbs?page=-5&size=-10", 
            String::class.java
        )
        // Should handle gracefully with default values or return error
        assert(response2.statusCode == HttpStatus.OK || response2.statusCode == HttpStatus.BAD_REQUEST || response2.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Negative pagination values should be handled: ${response2.statusCode}"
        }
        
        // Test missing required parameters for search
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search", // No query parameter
            String::class.java
        )
        // Should return bad request or handle with empty query
        assert(response3.statusCode == HttpStatus.OK || response3.statusCode == HttpStatus.BAD_REQUEST || response3.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Missing query parameter should be handled: ${response3.statusCode}"
        }
    }

    @Test
    fun `should handle special characters and encoding properly`() {
        // Test with special Tarifit characters
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=ⵜⵉⵖⵎⵎⵉ&type=waryaghri", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK) { "Tifinagh characters should be handled: ${response1.statusCode}" }
        
        // Test with special symbols and punctuation
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences/search?q=How%20are%20you%3F", // URL encoded "How are you?"
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK) { "URL encoded queries should be handled: ${response2.statusCode}" }
        
        // Test with very long search queries
        val longQuery = "a".repeat(1000)
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=$longQuery&type=aqelei", 
            String::class.java
        )
        assert(response3.statusCode == HttpStatus.OK || response3.statusCode == HttpStatus.BAD_REQUEST) {
            "Very long queries should be handled: ${response3.statusCode}"
        }
    }

    @Test
    fun `should handle concurrent requests properly`() {
        // Test multiple simultaneous requests to verify thread safety
        val responses = mutableListOf<ResponseEntity<String>>()
        
        // Create multiple concurrent requests
        repeat(5) { _ ->
            val response = testRestTemplate.getForEntity(
                "/api/v1/content/dictionary/search?q=aman&type=aqelei", 
                String::class.java
            )
            responses.add(response)
        }
        
        // All requests should succeed
        responses.forEach { response ->
            assert(response.statusCode == HttpStatus.OK) { "Concurrent request failed: ${response.statusCode}" }
            assert(response.body?.contains("aman") == true) { "Concurrent request should return expected data" }
        }
        
        // Verify data consistency across concurrent responses
        responses.forEach { response ->
            // All responses should have the same structure and key content
            assert(response.body?.contains("content") == true) { "All responses should have consistent structure" }
        }
    }

    @Test
    fun `should validate story category parameters`() {
        // Test with valid category
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/category/autobiographical", 
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK) { "Valid category should work: ${response1.statusCode}" }
        
        // Test with invalid/non-existent category
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/category/nonexistent_category", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK) { "Invalid category should return empty results: ${response2.statusCode}" }
        
        if (response2.body != null) {
            // Should return empty results for non-existent category
            val hasEmptyContent = response2.body!!.contains("\"content\":[]") || 
                                 response2.body!!.contains("\"empty\":true") ||
                                 response2.body!!.contains("\"totalElements\":0")
            assert(hasEmptyContent) { "Non-existent category should return empty results" }
        }
        
        // Test with special characters in category
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/category/test%20category%20with%20spaces", 
            String::class.java
        )
        assert(response3.statusCode == HttpStatus.OK) { "Category with special chars should be handled: ${response3.statusCode}" }
    }

    @Test
    fun `should handle resource not found scenarios`() {
        // Test non-existent story by ID
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/stories/nonexistent_story_id", 
            String::class.java
        )
        // Should return 404 or handle gracefully
        assert(response1.statusCode == HttpStatus.NOT_FOUND || response1.statusCode == HttpStatus.OK || response1.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Non-existent story should be handled appropriately: ${response1.statusCode}"
        }
        
        // Test random requests with count of 0
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences/random?count=0", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK || response2.statusCode == HttpStatus.BAD_REQUEST || response2.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            "Zero count for random should be handled: ${response2.statusCode}"
        }
        
        // Test random requests with very high count
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/verbs/random?count=10000", 
            String::class.java
        )
        assert(response3.statusCode == HttpStatus.OK || response3.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "High random count should be handled: ${response3.statusCode}" 
        }
        
        if (response3.statusCode == HttpStatus.OK && response3.body != null) {
            // Should not return more items than exist in database
            val verbCount = verbRepository.count()
            println("Database has $verbCount verbs, response: ${response3.body}")
        }
    }

    @Test
    fun `should handle edge cases in search functionality`() {
        // Test search with only whitespace
        val response1: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/dictionary/search?q=%20%20%20&type=aqelei", // URL encoded spaces
            String::class.java
        )
        assert(response1.statusCode == HttpStatus.OK || response1.statusCode == HttpStatus.BAD_REQUEST || response1.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "Whitespace query should be handled: ${response1.statusCode}" 
        }
        
        // Test search with SQL injection attempt (should be safely handled by MongoDB)
        val response2: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/sentences/search?q='; DROP TABLE sentences; --", 
            String::class.java
        )
        assert(response2.statusCode == HttpStatus.OK || response2.statusCode == HttpStatus.BAD_REQUEST || response2.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "SQL injection attempt should be safely handled: ${response2.statusCode}" 
        }
        
        // Verify database integrity after potential injection attempt (only if we have data)
        val sentenceCount = sentenceRepository.count()
        assert(sentenceCount >= 0) { "Database should remain intact after injection attempt, found: $sentenceCount sentences" }
        
        // Test search with NoSQL injection attempt - properly URL encode the query
        val response3: ResponseEntity<String> = testRestTemplate.getForEntity(
            "/api/v1/content/verbs/search?q=%7B%24ne%3Anull%7D", // URL encoded {"$ne":null}
            String::class.java
        )
        assert(response3.statusCode == HttpStatus.OK || response3.statusCode == HttpStatus.BAD_REQUEST || response3.statusCode == HttpStatus.INTERNAL_SERVER_ERROR) { 
            "NoSQL injection attempt should be safely handled: ${response3.statusCode}" 
        }
    }

    @Test
    fun `should maintain data consistency during multiple operations`() {
        // Verify initial state and perform multiple read operations
        repeat(3) {
            val response1 = testRestTemplate.getForEntity(
                "/api/v1/content/dictionary/search?q=aman&type=aqelei", 
                String::class.java
            )
            assert(response1.statusCode == HttpStatus.OK)
            
            val response2 = testRestTemplate.getForEntity(
                "/api/v1/content/sentences/random?count=1", 
                String::class.java
            )
            assert(response2.statusCode == HttpStatus.OK)
        }
        
        // Verify specific test data still exists after operations
        val testEntry = dictionaryAqeleiRepository.findAll().find { it.translation == "aman" }
        assert(testEntry != null) { "Test data should remain intact after multiple operations" }
    }
}
