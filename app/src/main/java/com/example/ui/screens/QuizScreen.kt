package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.NoteEntity
import com.example.data.local.QuizAttemptEntity
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.VioletAccent
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.ActiveQuizState
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    activeQuiz: ActiveQuizState?,
    notes: List<NoteEntity>,
    quizAttempts: List<QuizAttemptEntity>,
    isAiLoading: Boolean,
    prefilledQuizDoc: Pair<String, String>? = null,
    onClearPrefilledDoc: () -> Unit = {},
    onOpenDocumentPicker: () -> Unit = {},
    onGenerateQuiz: (sourceTitle: String, content: String, difficulty: String, questionCount: Int, durationMinutes: Int) -> Unit,
    onSelectAnswer: (questionIndex: Int, optionIndex: Int) -> Unit,
    onToggleFlagQuestion: (questionIndex: Int) -> Unit = {},
    onJumpToQuestion: (questionIndex: Int) -> Unit = {},
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onFinishQuiz: () -> Unit,
    onResetQuiz: () -> Unit,
    onCloseQuiz: () -> Unit
) {
    if (activeQuiz != null) {
        if (activeQuiz.isCompleted) {
            QuizResultView(
                activeQuiz = activeQuiz,
                onTryAgain = onResetQuiz,
                onGenerateAnother = onCloseQuiz,
                onClose = onCloseQuiz
            )
        } else {
            QuizRunnerView(
                activeQuiz = activeQuiz,
                onSelectAnswer = onSelectAnswer,
                onToggleFlag = onToggleFlagQuestion,
                onJumpToQuestion = onJumpToQuestion,
                onNext = onNextQuestion,
                onPrevious = onPreviousQuestion,
                onFinish = onFinishQuiz,
                onClose = onCloseQuiz
            )
        }
    } else {
        QuizSetupView(
            notes = notes,
            quizAttempts = quizAttempts,
            isAiLoading = isAiLoading,
            prefilledQuizDoc = prefilledQuizDoc,
            onClearPrefilledDoc = onClearPrefilledDoc,
            onOpenDocumentPicker = onOpenDocumentPicker,
            onGenerateQuiz = onGenerateQuiz
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizSetupView(
    notes: List<NoteEntity>,
    quizAttempts: List<QuizAttemptEntity>,
    isAiLoading: Boolean,
    prefilledQuizDoc: Pair<String, String>? = null,
    onClearPrefilledDoc: () -> Unit,
    onOpenDocumentPicker: () -> Unit,
    onGenerateQuiz: (String, String, String, Int, Int) -> Unit
) {
    val context = LocalContext.current
    var uploadedFileName by remember(prefilledQuizDoc) { mutableStateOf(prefilledQuizDoc?.first ?: "") }
    var uploadedFileContent by remember(prefilledQuizDoc) { mutableStateOf(prefilledQuizDoc?.second ?: "") }
    var selectedNote by remember { mutableStateOf<NoteEntity?>(notes.firstOrNull()) }
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    // User Criteria Requirements:
    // Minimum MCQs: 20, Maximum MCQs: 1024
    var questionCount by remember { mutableIntStateOf(20) }
    var questionCountInput by remember { mutableStateOf("20") }

    // Minimum Time: 10 minutes, Maximum Time: 120 minutes (2 hours)
    var durationMinutes by remember { mutableIntStateOf(30) }
    var durationInput by remember { mutableStateOf("30") }

    // In-screen File Picker launcher for PDF, PPT, Word, TXT
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                var fName = "Document_${System.currentTimeMillis()}"
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            fName = cursor.getString(nameIndex) ?: fName
                        }
                    }
                }
                val stream = context.contentResolver.openInputStream(uri)
                val bytes = stream?.use { it.readBytes() } ?: ByteArray(0)
                val rawStr = String(bytes, Charsets.UTF_8)

                val cleanText = if (rawStr.any { it.code == 0 }) {
                    val sb = StringBuilder()
                    var token = StringBuilder()
                    for (b in bytes) {
                        val c = b.toInt().toChar()
                        if (c in ' '..'~' || c == '\n' || c == '\t') {
                            token.append(c)
                        } else {
                            if (token.length >= 4) {
                                sb.append(token.toString().trim()).append(" ")
                            }
                            token.clear()
                        }
                        if (sb.length > 25000) break
                    }
                    if (sb.length > 50) sb.toString() else "Uploaded file: $fName (Ready for MCQ generation)"
                } else {
                    rawStr.take(20000)
                }

                uploadedFileName = fName
                uploadedFileContent = cleanText
                selectedNote = null
                Toast.makeText(context, "Loaded $fName for Quiz", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quiz_setup_view"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp)
    ) {
        item {
            // Futuristic Header Banner
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    PrimaryIndigo.copy(alpha = 0.85f),
                                    Color(0xFF4338CA),
                                    CyanAccent.copy(alpha = 0.4f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.35f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(SuccessGreen)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Online Gemini 3.5 AI",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "AI Exam & MCQ Generator",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Upload any PDF, PPT, Word or Notes to create customized practice exams (20 to 1024 MCQs with 10 to 120 min timer).",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: Study Material & File Upload Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "1. Choose Study Material",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Upload a document (PDF / PPT / DOC) or select one of your notes.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // File Upload Primary Action
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (uploadedFileName.isNotBlank()) PrimaryIndigo.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (uploadedFileName.isNotBlank()) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryIndigo) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                fileLauncher.launch(
                                    arrayOf(
                                        "application/pdf",
                                        "application/vnd.ms-powerpoint",
                                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                                        "application/msword",
                                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                        "text/*",
                                        "*/*"
                                    )
                                )
                            }
                            .testTag("upload_file_for_quiz_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val icon = when {
                                uploadedFileName.endsWith(".pdf", ignoreCase = true) -> Icons.Default.PictureAsPdf
                                uploadedFileName.endsWith(".ppt", ignoreCase = true) || uploadedFileName.endsWith(".pptx", ignoreCase = true) -> Icons.Default.Slideshow
                                else -> Icons.Default.FileUpload
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = "Upload",
                                tint = if (uploadedFileName.isNotBlank()) PrimaryIndigo else CyanAccent,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (uploadedFileName.isNotBlank()) uploadedFileName else "Upload PDF, PPT, Word or Notes",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (uploadedFileName.isNotBlank()) PrimaryIndigo else MaterialTheme.colorScheme.onSurface
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (uploadedFileName.isNotBlank()) "Tap to change file • AI ready" else "Tap to browse local device files",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            if (uploadedFileName.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        uploadedFileName = ""
                                        uploadedFileContent = ""
                                        onClearPrefilledDoc()
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove file",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Or Select from Notes:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (notes.isEmpty()) {
                        Text(
                            text = "No saved notes yet. Upload a file above or create a note.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    } else {
                        notes.take(4).forEach { note ->
                            val isSelected = selectedNote?.id == note.id && uploadedFileName.isBlank()
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrimaryIndigo.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, PrimaryIndigo) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        selectedNote = note
                                        uploadedFileName = ""
                                        uploadedFileContent = ""
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = note.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${note.folderName} • ${note.content.length / 5} words",
                                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = PrimaryIndigo,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 2: Number of MCQs Criteria (Min 20, Max 1024)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "2. Number of Questions (MCQs)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Criteria: Min 20, Max 1024 MCQs",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PrimaryIndigo.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "$questionCount MCQs",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = PrimaryIndigo),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = questionCount.toFloat(),
                        onValueChange = {
                            val rounded = it.toInt()
                            questionCount = rounded.coerceIn(20, 1024)
                            questionCountInput = questionCount.toString()
                        },
                        valueRange = 20f..1024f,
                        colors = SliderDefaults.colors(
                            thumbColor = PrimaryIndigo,
                            activeTrackColor = PrimaryIndigo
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_mcq_count_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Preset Quick Chips (20, 50, 100, 250, 500, 1024)
                    Text(
                        text = "Quick Presets:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val presets = listOf(20, 50, 100, 250, 500, 1024)
                        presets.forEach { count ->
                            val isSel = questionCount == count
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) PrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable {
                                    questionCount = count
                                    questionCountInput = count.toString()
                                }
                            ) {
                                Text(
                                    text = if (count == 1024) "1024 (Max)" else if (count == 20) "20 (Min)" else "$count",
                                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Custom input text field
                    OutlinedTextField(
                        value = questionCountInput,
                        onValueChange = { str ->
                            questionCountInput = str
                            str.toIntOrNull()?.let {
                                if (it in 20..1024) {
                                    questionCount = it
                                }
                            }
                        },
                        label = { Text("Custom MCQ Count (20 - 1024)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 3: Exam Duration Criteria (Min 10 mins, Max 120 mins / 2 Hours)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "3. Exam Duration",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Criteria: Min 10 min, Max 2 hours (120 min)",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF06B6D4).copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = CyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (durationMinutes >= 60) "${durationMinutes / 60}h ${durationMinutes % 60}m" else "$durationMinutes mins",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = CyanAccent)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = durationMinutes.toFloat(),
                        onValueChange = {
                            val rounded = it.toInt()
                            durationMinutes = rounded.coerceIn(10, 120)
                            durationInput = durationMinutes.toString()
                        },
                        valueRange = 10f..120f,
                        colors = SliderDefaults.colors(
                            thumbColor = CyanAccent,
                            activeTrackColor = CyanAccent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_duration_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Preset Duration Chips (10, 15, 30, 45, 60, 90, 120)
                    Text(
                        text = "Duration Presets:",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val timePresets = listOf(10, 15, 30, 45, 60, 90, 120)
                        timePresets.forEach { mins ->
                            val isSel = durationMinutes == mins
                            val label = when (mins) {
                                10 -> "10m (Min)"
                                60 -> "1 Hour"
                                120 -> "2h (Max)"
                                else -> "${mins}m"
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) CyanAccent else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable {
                                    durationMinutes = mins
                                    durationInput = mins.toString()
                                }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = durationInput,
                        onValueChange = { str ->
                            durationInput = str
                            str.toIntOrNull()?.let {
                                if (it in 10..120) {
                                    durationMinutes = it
                                }
                            }
                        },
                        label = { Text("Custom Duration in Minutes (10 - 120)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 4: Difficulty Selector
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "4. Academic Difficulty",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Easy", "Medium", "Hard", "Master").forEach { diff ->
                            val isSelected = selectedDifficulty == diff
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDifficulty = diff }
                            ) {
                                Text(
                                    text = diff,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Launch Button
            Button(
                onClick = {
                    val finalCount = questionCount.coerceIn(20, 1024)
                    val finalDuration = durationMinutes.coerceIn(10, 120)

                    if (uploadedFileName.isNotBlank() && uploadedFileContent.isNotBlank()) {
                        val cleanTitle = uploadedFileName.substringBeforeLast(".")
                        onGenerateQuiz(cleanTitle, uploadedFileContent, selectedDifficulty, finalCount, finalDuration)
                    } else if (selectedNote != null) {
                        val n = selectedNote!!
                        onGenerateQuiz(n.title, n.content, selectedDifficulty, finalCount, finalDuration)
                    } else {
                        Toast.makeText(context, "Please upload a file or select a note first.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isAiLoading,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("generate_quiz_button")
            ) {
                if (isAiLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Generating $questionCount MCQs...",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Start AI Exam ($questionCount MCQs • $durationMinutes min)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Past Quiz Attempts
            if (quizAttempts.isNotEmpty()) {
                Text(
                    text = "Previous Exam History",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))

                quizAttempts.take(3).forEach { attempt ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = attempt.quizTitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "${attempt.totalQuestions} Questions • Score: ${attempt.correctAnswers}/${attempt.totalQuestions}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            val percentage = attempt.scorePercentage
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (percentage >= 70) SuccessGreen.copy(alpha = 0.15f) else WarningAmber.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "$percentage%",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (percentage >= 70) SuccessGreen else WarningAmber
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizRunnerView(
    activeQuiz: ActiveQuizState,
    onSelectAnswer: (Int, Int) -> Unit,
    onToggleFlag: (Int) -> Unit,
    onJumpToQuestion: (Int) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onFinish: () -> Unit,
    onClose: () -> Unit
) {
    val currentQuestion = activeQuiz.questions.getOrNull(activeQuiz.currentIndex)
    val totalQuestions = activeQuiz.questions.size
    val currentSelected = activeQuiz.selectedAnswers[activeQuiz.currentIndex]
    val isFlagged = activeQuiz.flaggedQuestions.contains(activeQuiz.currentIndex)

    // Real-time Countdown Timer (durationMinutes -> seconds)
    val totalSeconds = remember(activeQuiz.durationMinutes) { activeQuiz.durationMinutes * 60 }
    var secondsRemaining by remember(activeQuiz.durationMinutes) { mutableIntStateOf(totalSeconds) }
    var showQuestionGridSheet by remember { mutableStateOf(false) }

    LaunchedEffect(activeQuiz.isCompleted) {
        while (secondsRemaining > 0 && !activeQuiz.isCompleted) {
            delay(1000)
            secondsRemaining -= 1
        }
        if (secondsRemaining <= 0 && !activeQuiz.isCompleted) {
            onFinish()
        }
    }

    val hours = secondsRemaining / 3600
    val minutes = (secondsRemaining % 3600) / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }

    val isUrgent = secondsRemaining < 300 // under 5 minutes
    val isCritical = secondsRemaining < 120 // under 2 minutes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quiz_runner_view")
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Top Bar: Timer Badge, Question Counter, Grid View, and Exit
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }

            // Real-time Countdown Timer Badge
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = when {
                    isCritical -> ErrorRed.copy(alpha = 0.2f)
                    isUrgent -> WarningAmber.copy(alpha = 0.2f)
                    else -> Color(0xFF0F172A)
                },
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when {
                        isCritical -> ErrorRed
                        isUrgent -> WarningAmber
                        else -> CyanAccent.copy(alpha = 0.5f)
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isUrgent) Icons.Default.HourglassBottom else Icons.Outlined.Timer,
                        contentDescription = "Timer",
                        tint = when {
                            isCritical -> ErrorRed
                            isUrgent -> WarningAmber
                            else -> CyanAccent
                        },
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = when {
                                isCritical -> ErrorRed
                                isUrgent -> WarningAmber
                                else -> CyanAccent
                            }
                        )
                    )
                }
            }

            Row {
                // Question Grid Navigator Trigger
                IconButton(onClick = { showQuestionGridSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Question Grid",
                        tint = PrimaryIndigo
                    )
                }

                // Flag Question for Review
                IconButton(onClick = { onToggleFlag(activeQuiz.currentIndex) }) {
                    Icon(
                        imageVector = if (isFlagged) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Flag Question",
                        tint = if (isFlagged) WarningAmber else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Indicator Bar
        val progress = (activeQuiz.currentIndex + 1).toFloat() / totalQuestions.coerceAtLeast(1)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = PrimaryIndigo,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question ${activeQuiz.currentIndex + 1} of $totalQuestions",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryIndigo)
            )
            Text(
                text = "${activeQuiz.selectedAnswers.size} Answered • ${activeQuiz.flaggedQuestions.size} Flagged",
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (currentQuestion != null) {
            // Question Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    item {
                        if (isFlagged) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = WarningAmber.copy(alpha = 0.15f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = WarningAmber,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Flagged for Review",
                                        style = MaterialTheme.typography.labelSmall.copy(color = WarningAmber, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    items(currentQuestion.options.size) { optIndex ->
                        val optionText = currentQuestion.options[optIndex]
                        val isSelected = currentSelected == optIndex
                        val optionLetter = ('A' + optIndex).toString()

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) PrimaryIndigo.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryIndigo) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clickable { onSelectAnswer(activeQuiz.currentIndex, optIndex) }
                                .testTag("quiz_option_$optIndex")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = optionLetter,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Navigation Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPrevious,
                enabled = activeQuiz.currentIndex > 0,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous")
            }

            if (activeQuiz.currentIndex == totalQuestions - 1) {
                Button(
                    onClick = onFinish,
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("finish_quiz_button")
                ) {
                    Text("Submit Exam 🎯", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                }
            } else {
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("next_quiz_button")
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }

    // Question Grid Bottom Sheet (jump to any question)
    if (showQuestionGridSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQuestionGridSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question Navigator (${totalQuestions} Total)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = { showQuestionGridSheet = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(PrimaryIndigo))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Answered", style = MaterialTheme.typography.labelSmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(WarningAmber))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Flagged", style = MaterialTheme.typography.labelSmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Unanswered", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 44.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(totalQuestions) { index ->
                        val isAns = activeQuiz.selectedAnswers.containsKey(index)
                        val isFlag = activeQuiz.flaggedQuestions.contains(index)
                        val isCur = activeQuiz.currentIndex == index

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isCur -> PrimaryIndigo
                                isFlag -> WarningAmber
                                isAns -> SuccessGreen.copy(alpha = 0.85f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            border = if (isCur) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null,
                            modifier = Modifier
                                .size(44.dp)
                                .clickable {
                                    onJumpToQuestion(index)
                                    showQuestionGridSheet = false
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    color = if (isCur || isFlag || isAns) Color.White else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun QuizResultView(
    activeQuiz: ActiveQuizState,
    onTryAgain: () -> Unit,
    onGenerateAnother: () -> Unit,
    onClose: () -> Unit
) {
    val total = activeQuiz.questions.size
    val correct = activeQuiz.score
    val percentage = if (total > 0) (correct * 100) / total else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quiz_result_view"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Score Summary Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Exam Completed!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeQuiz.title,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Circular Score Badge
                    Surface(
                        shape = CircleShape,
                        color = when {
                            percentage >= 80 -> SuccessGreen.copy(alpha = 0.15f)
                            percentage >= 60 -> PrimaryIndigo.copy(alpha = 0.15f)
                            else -> WarningAmber.copy(alpha = 0.15f)
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            4.dp,
                            when {
                                percentage >= 80 -> SuccessGreen
                                percentage >= 60 -> PrimaryIndigo
                                else -> WarningAmber
                            }
                        ),
                        modifier = Modifier.size(120.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$percentage%",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = when {
                                            percentage >= 80 -> SuccessGreen
                                            percentage >= 60 -> PrimaryIndigo
                                            else -> WarningAmber
                                        }
                                    )
                                )
                                Text(
                                    text = "$correct / $total",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = activeQuiz.aiFeedback,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Dual Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onTryAgain,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Retake")
                }

                Button(
                    onClick = onGenerateAnother,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("New Quiz", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Detailed Question Review
            Text(
                text = "Detailed Question Review",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(activeQuiz.questions.size) { index ->
            val q = activeQuiz.questions[index]
            val userPick = activeQuiz.selectedAnswers[index]
            val isCorrect = userPick == q.correctIndex

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isCorrect) SuccessGreen.copy(alpha = 0.5f) else ErrorRed.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${index + 1}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCorrect) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (isCorrect) "Correct ✓" else "Incorrect ✗",
                                color = if (isCorrect) SuccessGreen else ErrorRed,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val userOptionText = if (userPick != null && userPick in q.options.indices) q.options[userPick] else "No answer selected"
                    val correctOptionText = if (q.correctIndex in q.options.indices) q.options[q.correctIndex] else ""

                    if (!isCorrect) {
                        Text(
                            text = "Your answer: $userOptionText",
                            style = MaterialTheme.typography.bodySmall.copy(color = ErrorRed)
                        )
                    }

                    Text(
                        text = "Correct answer: $correctOptionText",
                        style = MaterialTheme.typography.bodySmall.copy(color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                    )

                    if (q.explanation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explanation: ${q.explanation}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }
        }
    }
}
