package com.tarifit.content.fixtures

import com.tarifit.content.domain.dictionary.DictionaryAqelei
import com.tarifit.content.domain.dictionary.DictionaryWaryaghri
import com.tarifit.content.domain.sentence.Sentence
import com.tarifit.content.domain.verb.Verb
import com.tarifit.content.domain.story.Story

object TestDataFixtures {
    
    fun createSampleAqeleiEntries(): List<DictionaryAqelei> {
        return listOf(
            DictionaryAqelei(
                id = "test_aqelei_1",
                word = "eau",
                translation = "aman",
                type = "noun",
                plural = "imawen"
            ),
            DictionaryAqelei(
                id = "test_aqelei_2", 
                word = "pain",
                translation = "aghrum",
                type = "noun",
                plural = "igherman"
            ),
            DictionaryAqelei(
                id = "test_aqelei_3",
                word = "maison",
                translation = "tighmmi",
                type = "noun",
                plural = "tighemma"
            )
        )
    }
    
    fun createSampleWaryaghriEntries(): List<DictionaryWaryaghri> {
        return listOf(
            DictionaryWaryaghri(
                id = "test_waryaghri_1",
                mot = "aman",
                definitionFr = "eau",
                type = listOf("nom"),
                pluriel = "imawen",
                tifinagh = "ⴰⵎⴰⵏ"
            ),
            DictionaryWaryaghri(
                id = "test_waryaghri_2",
                mot = "aghrum", 
                definitionFr = "pain",
                type = listOf("nom"),
                pluriel = "igherman",
                tifinagh = "ⴰⵖⵔⵓⵎ"
            ),
            DictionaryWaryaghri(
                id = "test_waryaghri_3",
                mot = "tighmmi",
                definitionFr = "maison",
                type = listOf("nom"),
                pluriel = "tighemma",
                tifinagh = "ⵜⵉⵖⵎⵎⵉ"
            )
        )
    }
    
    fun createSampleSentences(): List<Sentence> {
        return listOf(
            Sentence(
                id = "test_sentence_1",
                englishSentence = "Hello",
                rifSentence = "Azul"
            ),
            Sentence(
                id = "test_sentence_2",
                englishSentence = "Goodbye",
                rifSentence = "Ar tufat"
            ),
            Sentence(
                id = "test_sentence_3",
                englishSentence = "How are you?",
                rifSentence = "Mamek tsawaleḍ?"
            )
        )
    }
    
    fun createSampleVerbs(): List<Verb> {
        return listOf(
            Verb(
                id = "test_verb_1",
                verb = "azwel",
                translation = "run",
                conjugations = mapOf(
                    "present" to mapOf(
                        "1s" to "azweleg",
                        "2s" to "tazweleḍ",
                        "3s" to "yezwel"
                    )
                )
            ),
            Verb(
                id = "test_verb_2",
                verb = "ssiwel",
                translation = "walk",
                conjugations = mapOf(
                    "present" to mapOf(
                        "1s" to "ssiwleg",
                        "2s" to "tssiwleḍ", 
                        "3s" to "yessiwel"
                    )
                )
            ),
            Verb(
                id = "test_verb_3",
                verb = "ɣeṛ",
                translation = "read",
                conjugations = mapOf(
                    "present" to mapOf(
                        "1s" to "ɣṛeg",
                        "2s" to "tɣaṛeḍ",
                        "3s" to "yeɣṛa"
                    )
                )
            )
        )
    }
    
    fun createSampleStories(): List<Story> {
        return listOf(
            Story(
                id = "test_story_1",
                title = "Azul, nec qqaren-ayi Massin",
                titleEnglish = "Hello, they call me Massin",
                titleFrench = "Bonjour, ils m'appellent Massin",
                tarifitText = "Azul, nec qqaren-ayi Massin. D amḍan n 25 n yiseggasen...",
                englishText = "Hello, they call me Massin. I am a 25-year-old man...",
                frenchText = "Bonjour, ils m'appellent Massin. Je suis un homme de 25 ans...",
                difficultyLevel = "intermediate",
                category = "autobiographical",
                wordCount = 1378,
                estimatedReadingTime = 7,
                author = "Fuad (Amaziɣ Massin)",
                publicationDate = "2024-05-13",
                themes = listOf("identity", "language", "culture"),
                createdAt = "2024-12-11"
            ),
            Story(
                id = "test_story_2",
                title = "Tamurt n waraben",
                titleEnglish = "The land of ravens",
                titleFrench = "La terre des corbeaux",
                tarifitText = "Deg yiwen wass, yella yiwen uqcic...",
                englishText = "One day, there was a young boy...",
                frenchText = "Un jour, il y avait un jeune garçon...",
                difficultyLevel = "beginner",
                category = "folklore",
                wordCount = 856,
                estimatedReadingTime = 4,
                author = "Traditional",
                publicationDate = "2024-06-01",
                themes = listOf("nature", "adventure"),
                createdAt = "2024-12-11"
            )
        )
    }
}
