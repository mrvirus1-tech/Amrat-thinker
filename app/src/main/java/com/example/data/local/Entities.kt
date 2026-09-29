package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val folderId: Long? = null,
    val folderName: String = "General",
    val tags: String = "", // Comma-separated tags
    val colorHex: String = "#4F46E5",
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val imageUri: String? = null,
    val voiceRecordingPath: String? = null,
    val aiSummary: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconName: String = "folder",
    val colorHex: String = "#06B6D4",
    val isUnderDevelopment: Boolean = false,
    val noteCount: Int = 0,
    val description: String = ""
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val noteId: Long? = null,
    val deckName: String = "General",
    val frontText: String,
    val backText: String,
    val isMastered: Boolean = false,
    val reviewCount: Int = 0,
    val lastReviewedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quizTitle: String,
    val noteId: Long? = null,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val scorePercentage: Int,
    val weakTopics: String = "",
    val aiFeedback: String = "",
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_profile")
data class StudyProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val studentName: String = "Imran Malik",
    val email: String = "imran.malik@amrtthinker.edu",
    val gradeLevel: String = "University / Pre-Exam",
    val studyStreakDays: Int = 5,
    val totalNotesCreated: Int = 12,
    val totalQuizzesTaken: Int = 8,
    val totalFlashcardsMastered: Int = 24,
    val notificationsEnabled: Boolean = true,
    val isDarkMode: Boolean = false,
    val selectedLanguage: String = "English",
    val hasCompletedWelcome: Boolean = false
)
