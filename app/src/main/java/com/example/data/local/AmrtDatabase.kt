package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        NoteEntity::class,
        FolderEntity::class,
        FlashcardEntity::class,
        QuizAttemptEntity::class,
        StudyProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AmrtDatabase : RoomDatabase() {
    abstract fun amrtDao(): AmrtDao

    companion object {
        @Volatile
        private var INSTANCE: AmrtDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AmrtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AmrtDatabase::class.java,
                    "amrt_thinker_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.amrtDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: AmrtDao) {
            // Initial Profile
            dao.insertOrUpdateProfile(
                StudyProfileEntity(
                    id = 1,
                    studentName = "Imran Malik",
                    email = "imran.malik@amrtthinker.edu",
                    gradeLevel = "Undergraduate / Competitive Exam",
                    studyStreakDays = 6,
                    totalNotesCreated = 4,
                    totalQuizzesTaken = 3,
                    totalFlashcardsMastered = 16,
                    notificationsEnabled = true,
                    isDarkMode = false,
                    selectedLanguage = "English",
                    hasCompletedWelcome = false
                )
            )

            // Default Folders per user prompt:
            // 1. 📘 CSS Exam Preparation (Marked Under Development)
            val cssFolderId = dao.insertFolder(
                FolderEntity(
                    name = "CSS Exam Preparation",
                    iconName = "menu_book",
                    colorHex = "#2563EB",
                    isUnderDevelopment = true,
                    description = "Central Superior Services competitive examination preparation modules."
                )
            )

            // 2. 🎓 ECAT Preparation (Marked Under Development)
            val ecatFolderId = dao.insertFolder(
                FolderEntity(
                    name = "ECAT Preparation",
                    iconName = "school",
                    colorHex = "#7C3AED",
                    isUnderDevelopment = true,
                    description = "Engineering College Admission Test practice questions and formulas."
                )
            )

            // 3. 🩺 MDCAT Preparation (Marked Under Development)
            val mdcatFolderId = dao.insertFolder(
                FolderEntity(
                    name = "MDCAT Preparation",
                    iconName = "medical_services",
                    colorHex = "#059669",
                    isUnderDevelopment = true,
                    description = "Medical and Dental Colleges Admission Test biology, chemistry & physics."
                )
            )

            // 4. University & General Folders
            val csFolderId = dao.insertFolder(
                FolderEntity(
                    name = "Computer Networks",
                    iconName = "lan",
                    colorHex = "#0284C7",
                    isUnderDevelopment = false,
                    description = "OSI layer, TCP/IP, routing protocols and socket architectures."
                )
            )

            val physicsFolderId = dao.insertFolder(
                FolderEntity(
                    name = "Physics & Mechanics",
                    iconName = "bolt",
                    colorHex = "#EA580C",
                    isUnderDevelopment = false,
                    description = "Newton's laws of motion, gravitation, thermodynamics and modern physics."
                )
            )

            val mathFolderId = dao.insertFolder(
                FolderEntity(
                    name = "Mathematics",
                    iconName = "functions",
                    colorHex = "#9333EA",
                    isUnderDevelopment = false,
                    description = "Calculus, linear algebra and differential equations."
                )
            )

            // Initial Starter Notes
            val note1Id = dao.insertNote(
                NoteEntity(
                    title = "Newton's Three Laws of Motion",
                    content = """
# Newton's Laws of Motion

## 1. First Law (Law of Inertia)
An object at rest stays at rest and an object in motion stays in motion with the same speed and in the same direction unless acted upon by an unbalanced external force.
- **Formula:** $\Sigma F = 0 \implies a = 0$
- **Real-world Example:** A book lying on a desk remains stationary until someone pushes it.

## 2. Second Law (Force and Acceleration)
The acceleration of an object is directly proportional to the net force acting on it and inversely proportional to its mass.
- **Key Formula:** **F = ma**
- Unit of Force is the Newton (N = kg·m/s²).

## 3. Third Law (Action and Reaction)
For every action, there is an equal and opposite reaction.
- When object A exerts a force on object B, object B simultaneously exerts a force equal in magnitude and opposite in direction on object A.
- Example: Rocket propulsion pushing exhaust gas backwards creates forward thrust.
                    """.trimIndent(),
                    folderId = physicsFolderId,
                    folderName = "Physics & Mechanics",
                    tags = "Physics, Mechanics, Exam-Prep",
                    colorHex = "#EA580C",
                    isFavorite = true,
                    aiSummary = "Newton's laws define classical mechanics: 1) Inertia (ΣF=0), 2) F=ma (force yields acceleration), 3) Equal & opposite reaction forces."
                )
            )

            val note2Id = dao.insertNote(
                NoteEntity(
                    title = "OSI 7-Layer Architecture & TCP/IP",
                    content = """
# OSI Model Overview

The Open Systems Interconnection (OSI) reference model characterizes computing and telecommunication functions into seven abstract layers:

1. **Application Layer (Layer 7):** HTTP, HTTPS, FTP, DNS. Direct interface to end-user software.
2. **Presentation Layer (Layer 6):** Data encryption, compression, and formatting (JPEG, ASCII, TLS).
3. **Session Layer (Layer 5):** Establishes, manages, and terminates sessions between applications.
4. **Transport Layer (Layer 4):** End-to-end transport reliability. TCP (reliable, connection-oriented) vs UDP (fast, connectionless).
5. **Network Layer (Layer 3):** Routing and logical addressing (IP addresses, Routers, ICMP).
6. **Data Link Layer (Layer 2):** Physical addressing, MAC addresses, Ethernet framing, switches.
7. **Physical Layer (Layer 1):** Raw bitstream transmission via copper cables, fiber optics, or radio waves.

### Quick Mnemonic:
**P**lease **D**o **N**ot **T**hrow **S**ausage **P**izza **A**way
                    """.trimIndent(),
                    folderId = csFolderId,
                    folderName = "Computer Networks",
                    tags = "Networking, CS, Protocols",
                    colorHex = "#0284C7",
                    isFavorite = true,
                    aiSummary = "Detailed guide on the 7 layers of OSI model from Physical to Application, detailing TCP vs UDP and network packet flow."
                )
            )

            val note3Id = dao.insertNote(
                NoteEntity(
                    title = "Calculus: Differentiation & Integration Rules",
                    content = """
# Fundamental Calculus Formulas

## Differentiation Fundamentals
- Power Rule: $\frac{d}{dx} x^n = n x^{n-1}$
- Product Rule: $(uv)' = u'v + uv'$
- Quotient Rule: $(\frac{u}{v})' = \frac{u'v - uv'}{v^2}$
- Chain Rule: $\frac{dy}{dx} = \frac{dy}{du} \cdot \frac{du}{dx}$

## Standard Integrals
- $\int x^n dx = \frac{x^{n+1}}{n+1} + C \quad (n \neq -1)$
- $\int \frac{1}{x} dx = \ln|x| + C$
- $\int e^x dx = e^x + C$
- $\int \cos(x) dx = \sin(x) + C$
                    """.trimIndent(),
                    folderId = mathFolderId,
                    folderName = "Mathematics",
                    tags = "Math, Calculus, Formulas",
                    colorHex = "#9333EA",
                    isFavorite = false,
                    aiSummary = "Quick reference sheet for core calculus differentiation (power, product, quotient, chain) and fundamental indefinite integration rules."
                )
            )

            // Initial Flashcards
            dao.insertFlashcard(
                FlashcardEntity(
                    noteId = note2Id,
                    deckName = "Computer Networks",
                    frontText = "What is the primary role of Layer 4 (Transport Layer)?",
                    backText = "End-to-end communication, segmentation, flow control, and error checking using TCP or UDP.",
                    isMastered = true
                )
            )
            dao.insertFlashcard(
                FlashcardEntity(
                    noteId = note2Id,
                    deckName = "Computer Networks",
                    frontText = "What is the main difference between TCP and UDP?",
                    backText = "TCP is connection-oriented and reliable with acknowledgments; UDP is connectionless, faster, with no delivery guarantee.",
                    isMastered = true
                )
            )
            dao.insertFlashcard(
                FlashcardEntity(
                    noteId = note1Id,
                    deckName = "Physics & Mechanics",
                    frontText = "What is Newton's Second Law of Motion equation?",
                    backText = "F = ma (Net Force = mass × acceleration), measured in Newtons (kg·m/s²).",
                    isMastered = false
                )
            )
            dao.insertFlashcard(
                FlashcardEntity(
                    noteId = note1Id,
                    deckName = "Physics & Mechanics",
                    frontText = "State Newton's Third Law with a physical example.",
                    backText = "For every action, there is an equal and opposite reaction. Example: Rocket thrust pushing gases downward accelerates the rocket upward.",
                    isMastered = true
                )
            )

            // Initial Quiz Attempt
            dao.insertQuizAttempt(
                QuizAttemptEntity(
                    quizTitle = "Computer Networks Fundamentals",
                    noteId = note2Id,
                    totalQuestions = 5,
                    correctAnswers = 4,
                    scorePercentage = 80,
                    weakTopics = "Layer 2 MAC framing vs Layer 3 IP routing",
                    aiFeedback = "Strong grasp of the OSI model! Review switch MAC learning algorithms to achieve a perfect 100%."
                )
            )
        }
    }
}
