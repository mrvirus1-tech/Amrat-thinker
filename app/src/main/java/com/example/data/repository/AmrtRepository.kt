package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.local.AmrtDao
import com.example.data.local.FlashcardEntity
import com.example.data.local.FolderEntity
import com.example.data.local.NoteEntity
import com.example.data.local.QuizAttemptEntity
import com.example.data.local.StudyProfileEntity
import com.example.data.remote.GeminiClient
import kotlinx.coroutines.flow.Flow

class AmrtRepository(private val dao: AmrtDao) {

    // --- Notes Flow ---
    val allActiveNotes: Flow<List<NoteEntity>> = dao.getAllActiveNotes()
    val favoriteNotes: Flow<List<NoteEntity>> = dao.getFavoriteNotes()
    val archivedNotes: Flow<List<NoteEntity>> = dao.getArchivedNotes()
    val notesCount: Flow<Int> = dao.getNotesCount()

    suspend fun getNoteById(id: Long): NoteEntity? = dao.getNoteById(id)

    fun getNotesByFolder(folderId: Long): Flow<List<NoteEntity>> = dao.getNotesByFolder(folderId)

    suspend fun insertOrUpdateNote(note: NoteEntity): Long = dao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) = dao.updateNote(note)

    suspend fun deleteNote(id: Long) = dao.deleteNoteById(id)

    suspend fun toggleFavorite(note: NoteEntity) {
        dao.updateNote(note.copy(isFavorite = !note.isFavorite, updatedAt = System.currentTimeMillis()))
    }

    suspend fun duplicateNote(note: NoteEntity) {
        val copy = note.copy(
            id = 0,
            title = "${note.title} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertNote(copy)
    }

    suspend fun toggleArchive(note: NoteEntity) {
        dao.updateNote(note.copy(isArchived = !note.isArchived, updatedAt = System.currentTimeMillis()))
    }

    // --- Folders Flow ---
    val allFolders: Flow<List<FolderEntity>> = dao.getAllFolders()

    suspend fun getFolderById(id: Long): FolderEntity? = dao.getFolderById(id)

    suspend fun insertFolder(name: String, iconName: String, colorHex: String, description: String = ""): Long {
        return dao.insertFolder(
            FolderEntity(
                name = name,
                iconName = iconName,
                colorHex = colorHex,
                isUnderDevelopment = false,
                description = description
            )
        )
    }

    suspend fun deleteFolder(id: Long) = dao.deleteFolderById(id)

    // --- Flashcards Flow ---
    val allFlashcards: Flow<List<FlashcardEntity>> = dao.getAllFlashcards()

    fun getFlashcardsByNote(noteId: Long): Flow<List<FlashcardEntity>> = dao.getFlashcardsByNote(noteId)

    suspend fun insertFlashcard(flashcard: FlashcardEntity) = dao.insertFlashcard(flashcard)

    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>) = dao.insertFlashcards(flashcards)

    suspend fun toggleFlashcardMastered(flashcard: FlashcardEntity) {
        dao.updateFlashcard(
            flashcard.copy(
                isMastered = !flashcard.isMastered,
                reviewCount = flashcard.reviewCount + 1,
                lastReviewedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteFlashcard(id: Long) = dao.deleteFlashcard(id)

    // --- Quiz Attempts Flow ---
    val allQuizAttempts: Flow<List<QuizAttemptEntity>> = dao.getAllQuizAttempts()

    suspend fun recordQuizAttempt(
        title: String,
        noteId: Long?,
        total: Int,
        correct: Int,
        weakTopics: String,
        feedback: String
    ): Long {
        val percentage = if (total > 0) (correct * 100) / total else 0
        return dao.insertQuizAttempt(
            QuizAttemptEntity(
                quizTitle = title,
                noteId = noteId,
                totalQuestions = total,
                correctAnswers = correct,
                scorePercentage = percentage,
                weakTopics = weakTopics,
                aiFeedback = feedback
            )
        )
    }

    // --- Profile Flow ---
    val profile: Flow<StudyProfileEntity?> = dao.getProfile()

    suspend fun updateProfile(profile: StudyProfileEntity) = dao.insertOrUpdateProfile(profile)

    // --- AI Gemini Operations ---
    suspend fun askGemini(prompt: String, systemPrompt: String = "You are Amrt Thinker study assistant."): Result<String> {
        return GeminiClient.generateContent(prompt = prompt, systemInstruction = systemPrompt)
    }

    suspend fun transformNoteWithAi(content: String, mode: String): Result<String> {
        val prompt = when (mode) {
            "summarize_short" -> "Provide a concise 3-bullet summary of the following note content:\n\n$content"
            "summarize_detailed" -> "Provide a comprehensive, well-structured academic summary of the following note content:\n\n$content"
            "summarize_keypoints" -> "Extract the most critical key points and definitions from the following notes:\n\n$content"
            "simplify" -> "Explain the concepts in the following note content in ultra simple terms, like I am a complete beginner, using everyday analogies:\n\n$content"
            "exam_notes" -> "Convert the following notes into a high-yield exam revision sheet with essential formulas, definitions, and expected exam questions:\n\n$content"
            "generate_mcqs" -> "Create 4 high-quality Multiple Choice Questions (with 4 options A, B, C, D, the correct answer, and an explanation) based on this content:\n\n$content"
            "generate_flashcards" -> "Generate 4 flashcard pairs (Front question and Back answer) formatted clearly based on this content:\n\n$content"
            "explain_difficult" -> "Identify and break down the most difficult, abstract, or tricky concepts in this note content step-by-step:\n\n$content"
            "find_important" -> "Highlight and list the top 5 most important testable points in this material:\n\n$content"
            else -> "Analyze and provide study insights for:\n\n$content"
        }
        return GeminiClient.generateContent(
            prompt = prompt,
            systemInstruction = "You are Amrt Thinker, an expert AI academic tutor. Output clean markdown formatted for student note-taking."
        )
    }

    suspend fun scanImageToNotes(bitmap: Bitmap): Result<String> {
        val prompt = "Transcribe, extract, and structure all educational notes, formulas, diagrams, and text from this image into a clean, well-formatted markdown study note."
        return GeminiClient.generateContent(
            prompt = prompt,
            systemInstruction = "You are Amrt Thinker OCR & Note Transformation specialist. Structure the extracted text into headings, bullet points, and formulas.",
            imageBitmap = bitmap
        )
    }

    suspend fun processVoiceText(rawSpeech: String, language: String): Result<String> {
        val prompt = """
A student recorded a voice note ($language):
"$rawSpeech"

Please:
1. Fix any grammar or transcription inconsistencies.
2. Structure it into a beautifully organized study note with a clear title, bullet points, key takeaways, and relevant examples.
        """.trimIndent()

        return GeminiClient.generateContent(
            prompt = prompt,
            systemInstruction = "You are Amrt Thinker voice-to-notes organizer. Return a well-structured markdown note with # Title, ## Sections, and bullet points."
        )
    }
}
