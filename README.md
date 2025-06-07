# Tarifit Content Service Backend

Content management microservice for the Tarifit Translator application built with Spring Boot and Kotlin.

## Description
Manages dictionary entries, sentences, verbs, and translation content for the Tarifit Translator platform.

## Technology Stack
- **Framework**: Spring Boot 3.x
- **Language**: Kotlin
- **Database**: MongoDB
- **Search**: Full-text search capabilities
- **Build Tool**: Maven

## Features
- Dictionary search and retrieval
- Sentence management
- Verb conjugation lookup
- Content pagination
- Search functionality

## API Endpoints
```
GET /api/v1/content/dictionary/search?q={query} - Search dictionary
GET /api/v1/content/dictionary/random?count={n} - Random words
GET /api/v1/content/sentences?page={n}          - Get sentences
GET /api/v1/content/verbs?page={n}              - Get verbs
GET /api/v1/content/verbs/search?q={query}      - Search verbs
```

## Configuration
- Port: 8082
- MongoDB Connection: `tarifit_content` database
- Collections: dictionary_aqelɛi, sentences, verbs

## Repository
https://github.com/tarifit/tarifit-content-service-back
