package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VioletAccent
import com.example.ui.dialogs.AiAssistantDialog
import com.example.ui.dialogs.DocumentImportDialog
import com.example.ui.dialogs.ImageScannerDialog
import com.example.ui.dialogs.VoiceNoteDialog
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.FoldersScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NoteDetailScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.VideoGuideScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.AmrtThinkerTheme
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.viewmodel.AmrtViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AmrtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val showWelcome by viewModel.showWelcomeScreen.collectAsState()

            AmrtThinkerTheme(darkTheme = isDarkMode) {
                if (showWelcome) {
                    WelcomeScreen(
                        onGetStarted = { viewModel.dismissWelcomeScreen() }
                    )
                } else {
                    AmrtThinkerMainApp(viewModel = viewModel)
                }
            }
        }
    }
}

sealed class ScreenTab(val route: String, val title: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    object Home : ScreenTab("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Notes : ScreenTab("notes", "Notes", Icons.Filled.Description, Icons.Outlined.Description)
    object Quiz : ScreenTab("quiz", "Quiz", Icons.Filled.Psychology, Icons.Outlined.Psychology)
    object Folders : ScreenTab("folders", "Folders", Icons.Filled.Folder, Icons.Outlined.Folder)
    object Profile : ScreenTab("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun AmrtThinkerMainApp(viewModel: AmrtViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedNote by viewModel.selectedNote.collectAsState()
    val allNotes by viewModel.allNotes.collectAsState()
    val favoriteNotes by viewModel.favoriteNotes.collectAsState()
    val folders by viewModel.folders.collectAsState()
    val flashcards by viewModel.flashcards.collectAsState()
    val quizAttempts by viewModel.quizAttempts.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val showVideoGuide by viewModel.showVideoGuide.collectAsState()

    val showVoiceDialog by viewModel.showVoiceNoteDialog.collectAsState()
    val showImageDialog by viewModel.showImageScannerDialog.collectAsState()
    val showDocDialog by viewModel.showDocumentImportDialog.collectAsState()
    val showAiDialog by viewModel.showAiAssistantDialog.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val aiResult by viewModel.aiResult.collectAsState()
    val aiMessages by viewModel.aiAssistantMessages.collectAsState()
    val activeQuiz by viewModel.activeQuiz.collectAsState()
    val prefilledQuizDoc by viewModel.prefilledQuizDoc.collectAsState()
    val selectedFolder by viewModel.selectedFolder.collectAsState()

    val navTabs = listOf(
        ScreenTab.Home,
        ScreenTab.Notes,
        ScreenTab.Quiz,
        ScreenTab.Folders,
        ScreenTab.Profile
    )

    // If user opened Video Guide
    if (showVideoGuide) {
        VideoGuideScreen(
            onClose = { viewModel.setShowVideoGuide(false) },
            onOpenQuiz = {
                viewModel.setShowVideoGuide(false)
                viewModel.setTab("quiz")
            },
            onOpenVoiceNotes = {
                viewModel.setShowVideoGuide(false)
                viewModel.setShowVoiceNoteDialog(true)
            },
            onOpenImageScanner = {
                viewModel.setShowVideoGuide(false)
                viewModel.setShowImageScannerDialog(true)
            },
            onOpenDocumentImport = {
                viewModel.setShowVideoGuide(false)
                viewModel.setShowDocumentImportDialog(true)
            },
            onOpenAiAssistant = {
                viewModel.setShowVideoGuide(false)
                viewModel.setShowAiAssistantDialog(true)
            },
            onCreateNote = {
                viewModel.setShowVideoGuide(false)
                val newNote = viewModel.createNewNote()
                viewModel.selectNote(newNote)
            },
            onOpenFolders = {
                viewModel.setShowVideoGuide(false)
                viewModel.setTab("folders")
            }
        )
    } else if (selectedNote != null) {
        NoteDetailScreen(
            note = selectedNote!!,
            folders = folders,
            isAiLoading = isAiLoading,
            aiResult = aiResult,
            onBack = { viewModel.selectNote(null) },
            onContentChanged = { t, c, tags, color -> viewModel.onNoteContentChanged(t, c, tags, color) },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onDuplicate = { viewModel.duplicateNote(it) },
            onDelete = { viewModel.deleteNote(it) },
            onMoveToFolder = { note, folder -> viewModel.moveNoteToFolder(note, folder) },
            onTransformWithAi = { mode -> viewModel.transformCurrentNote(mode) },
            onAppendAiResult = { viewModel.appendAiResultToCurrentNote() },
            onClearAiResult = { viewModel.clearAiResult() }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    tonalElevation = 10.dp,
                    border = BorderStroke(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(
                                CyanAccent.copy(alpha = 0.4f),
                                PrimaryIndigo.copy(alpha = 0.35f),
                                VioletAccent.copy(alpha = 0.4f)
                            )
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("bottom_nav_bar")
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                        windowInsets = WindowInsets(0, 0, 0, 0)
                    ) {
                        navTabs.forEach { tab ->
                            val isSelected = currentTab == tab.route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = CyanAccent,
                                    selectedTextColor = CyanAccent,
                                    indicatorColor = CyanAccent.copy(alpha = 0.18f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_tab_${tab.route}")
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                // Floating Amrt AI Companion Button (available across tabs unless running quiz)
                if (activeQuiz == null && currentTab != "profile") {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 76.dp)
                            .testTag("floating_ai_assistant_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient glowing backdrop aura
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(CyanAccent.copy(alpha = 0.4f), Color.Transparent)
                                    )
                                )
                        )
                        FloatingActionButton(
                            onClick = { viewModel.setShowAiAssistantDialog(true) },
                            containerColor = Color(0xFF0F172A),
                            contentColor = CyanAccent,
                            shape = CircleShape,
                            modifier = Modifier.border(1.5.dp, CyanAccent, CircleShape),
                            elevation = FloatingActionButtonDefaults.elevation(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Ask Amrt AI",
                                    tint = CyanAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Amrt AI",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { targetTab ->
                    when (targetTab) {
                        "home" -> HomeScreen(
                            notes = allNotes,
                            favorites = favoriteNotes,
                            profile = profile,
                            onOpenNote = { viewModel.selectNote(it) },
                            onCreateNote = {
                                val newNote = viewModel.createNewNote()
                                viewModel.selectNote(newNote)
                            },
                            onOpenVoiceNotes = { viewModel.setShowVoiceNoteDialog(true) },
                            onOpenImageScanner = { viewModel.setShowImageScannerDialog(true) },
                            onOpenDocumentImport = { viewModel.setShowDocumentImportDialog(true) },
                            onOpenAiAssistant = { viewModel.setShowAiAssistantDialog(true) },
                            onOpenQuizTab = { viewModel.setTab("quiz") },
                            onOpenFoldersTab = { viewModel.setTab("folders") },
                            onAskAiPrompt = { prompt ->
                                viewModel.setShowAiAssistantDialog(true)
                                viewModel.sendAiAssistantMessage(prompt)
                            },
                            onOpenVideoGuide = { viewModel.setShowVideoGuide(true) }
                        )

                        "notes" -> NotesScreen(
                            notes = allNotes,
                            onSelectNote = { viewModel.selectNote(it) },
                            onCreateNote = {
                                val newNote = viewModel.createNewNote()
                                viewModel.selectNote(newNote)
                            },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onDeleteNote = { viewModel.deleteNote(it) },
                            onDuplicateNote = { viewModel.duplicateNote(it) }
                        )

                        "quiz" -> QuizScreen(
                            activeQuiz = activeQuiz,
                            notes = allNotes,
                            quizAttempts = quizAttempts,
                            isAiLoading = isAiLoading,
                            prefilledQuizDoc = prefilledQuizDoc,
                            onClearPrefilledDoc = { viewModel.clearPrefilledQuizDoc() },
                            onOpenDocumentPicker = { viewModel.setShowDocumentImportDialog(true) },
                            onGenerateQuiz = { title, content, diff, count, duration ->
                                viewModel.generateQuiz(title, content, diff, count, duration)
                            },
                            onSelectAnswer = { qIndex, optIndex ->
                                viewModel.selectQuizAnswer(qIndex, optIndex)
                            },
                            onToggleFlagQuestion = { qIndex ->
                                viewModel.toggleFlagQuestion(qIndex)
                            },
                            onJumpToQuestion = { qIndex ->
                                viewModel.jumpToQuizQuestion(qIndex)
                            },
                            onNextQuestion = { viewModel.nextQuizQuestion() },
                            onPreviousQuestion = { viewModel.previousQuizQuestion() },
                            onFinishQuiz = { viewModel.finishQuiz() },
                            onResetQuiz = { viewModel.resetQuiz() },
                            onCloseQuiz = { viewModel.closeQuiz() }
                        )

                        "folders" -> FoldersScreen(
                            folders = folders,
                            notes = allNotes,
                            selectedFolder = selectedFolder,
                            onSelectFolder = { viewModel.selectFolder(it) },
                            onOpenNote = { viewModel.selectNote(it) },
                            onCreateFolder = { name, icon, color, desc ->
                                viewModel.createFolder(name, icon, color, desc)
                            },
                            onCreateNoteInFolder = { folder ->
                                val note = viewModel.createNewNote(folderId = folder.id, folderName = folder.name)
                                viewModel.selectNote(note)
                            },
                            onBackToHome = {
                                viewModel.selectFolder(null)
                                viewModel.setTab("home")
                            }
                        )

                        "profile" -> ProfileScreen(
                            profile = profile,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            onReplayWelcome = { viewModel.replayWelcomeScreen() },
                            onUpdateNotifications = {
                                val cur = profile ?: return@ProfileScreen
                                // saved in VM
                            },
                            onSelectLanguage = {
                                // saved in VM
                            },
                            onSaveApiKey = { key ->
                                viewModel.saveCustomApiKey(key)
                            },
                            onTestConnection = { key, callback ->
                                viewModel.testGeminiConnection(key, callback)
                            },
                            onOpenVideoGuide = { viewModel.setShowVideoGuide(true) }
                        )
                    }
                }
            }
        }
    }

    // Modal Sheets and Dialogs
    if (showVoiceDialog) {
        VoiceNoteDialog(
            isAiLoading = isAiLoading,
            onDismiss = { viewModel.setShowVoiceNoteDialog(false) },
            onProcessVoice = { text, lang -> viewModel.processVoiceRecording(text, lang) }
        )
    }

    if (showImageDialog) {
        ImageScannerDialog(
            isAiLoading = isAiLoading,
            onDismiss = { viewModel.setShowImageScannerDialog(false) },
            onProcessImage = { bitmap -> viewModel.processScannedImage(bitmap) }
        )
    }

    if (showDocDialog) {
        DocumentImportDialog(
            onDismiss = { viewModel.setShowDocumentImportDialog(false) },
            onImportDocument = { title, content -> viewModel.importDocumentText(title, content) },
            onGenerateQuizFromFile = { title, content ->
                viewModel.startQuizFromFile(title, content, questionCount = 20, durationMinutes = 30)
            }
        )
    }

    if (showAiDialog) {
        AiAssistantDialog(
            messages = aiMessages,
            isAiLoading = isAiLoading,
            onDismiss = { viewModel.setShowAiAssistantDialog(false) },
            onSendMessage = { prompt -> viewModel.sendAiAssistantMessage(prompt) }
        )
    }
}
