package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.VioletAccent
import com.example.ui.theme.WarningAmber
import kotlinx.coroutines.delay

data class VideoChapter(
    val id: Int,
    val title: String,
    val romanUrduTitle: String,
    val startSecond: Int,
    val durationSeconds: Int,
    val icon: ImageVector,
    val accentColor: Color,
    val romanUrduSubtitle: String,
    val englishSubtitle: String,
    val quickActionLabel: String,
    val sceneKey: String
)

@Composable
fun VideoGuideScreen(
    onClose: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenVoiceNotes: () -> Unit,
    onOpenImageScanner: () -> Unit,
    onOpenDocumentImport: () -> Unit,
    onOpenAiAssistant: () -> Unit,
    onCreateNote: () -> Unit,
    onOpenFolders: () -> Unit
) {
    BackHandler { onClose() }

    val chapters = remember {
        listOf(
            VideoChapter(
                id = 1,
                title = "Cyber Neural Hub & Home",
                romanUrduTitle = "1. Home Screen & Streaks",
                startSecond = 0,
                durationSeconds = 45,
                icon = Icons.Default.AutoAwesome,
                accentColor = CyanAccent,
                romanUrduSubtitle = "Salam! Smart Thinker me aapka khushamdeed. Yahan aap apna daily study streak, live Gemini 3.5 AI engine aur quick shortcuts dekh sakte hain.",
                englishSubtitle = "Welcome to Smart Thinker! Track your daily study streak, live Gemini 3.5 Flash engine, and quick launch shortcuts.",
                quickActionLabel = "Go to Home Screen",
                sceneKey = "home_scene"
            ),
            VideoChapter(
                id = 2,
                title = "Smart Notes & AI Summary",
                romanUrduTitle = "2. Smart Notes & Summarizer",
                startSecond = 45,
                durationSeconds = 45,
                icon = Icons.Default.Description,
                accentColor = PrimaryIndigo,
                romanUrduSubtitle = "Kisi bhi note ko likhein ya paste karein, phir 'AI Summarize' dabayein. Gemini aapke note ka mukammal bullet khulasa bana dega.",
                englishSubtitle = "Type or paste your study material, then tap 'AI Summarize'. Gemini generates structured bullet points and takeaways.",
                quickActionLabel = "Create New Note",
                sceneKey = "notes_scene"
            ),
            VideoChapter(
                id = 3,
                title = "Voice Notes & Urdu Audio",
                romanUrduTitle = "3. Bol Kar Note Banayein",
                startSecond = 90,
                durationSeconds = 45,
                icon = Icons.Default.Mic,
                accentColor = WarningAmber,
                romanUrduSubtitle = "Mic icon dabayein aur Urdu ya English me bolna shuru karein. Amrt Thinker aapki awaz ko saf suthre text aur points me convert karta hai.",
                englishSubtitle = "Tap the microphone and speak in Urdu or English. Amrt Thinker transcribes and structures your voice notes accurately.",
                quickActionLabel = "Open Voice Notes",
                sceneKey = "voice_scene"
            ),
            VideoChapter(
                id = 4,
                title = "Book & Document OCR Scanner",
                romanUrduTitle = "4. Kitaab & Notes Scanner",
                startSecond = 135,
                durationSeconds = 45,
                icon = Icons.Default.PhotoCamera,
                accentColor = VioletAccent,
                romanUrduSubtitle = "Apni kitaab, handouts ya past paper ki photo khenchein. OCR scanner foran text read karke study note bana deta hai.",
                englishSubtitle = "Snap a photo of textbook pages, handwritten notes, or past papers. The OCR scanner extracts text automatically.",
                quickActionLabel = "Scan Document",
                sceneKey = "camera_scene"
            ),
            VideoChapter(
                id = 5,
                title = "AI Exam Cockpit (20-1024 MCQs)",
                romanUrduTitle = "5. AI Exam (20 se 1024 MCQs)",
                startSecond = 180,
                durationSeconds = 75,
                icon = Icons.Default.Psychology,
                accentColor = SuccessGreen,
                romanUrduSubtitle = "Kisi bhi topic par 20 se 1024 tak MCQs banayein! Countdown timer, question review flag aur har answer ki mukammal wazahat milti hai.",
                englishSubtitle = "Generate 20 up to 1024 custom MCQs on any topic with countdown timer, review flags, and detailed explanations.",
                quickActionLabel = "Start AI Exam",
                sceneKey = "quiz_scene"
            ),
            VideoChapter(
                id = 6,
                title = "Amrt AI 24/7 Study Tutor",
                romanUrduTitle = "6. Amrt AI 24/7 Tutor",
                startSecond = 255,
                durationSeconds = 45,
                icon = Icons.Default.AutoAwesome,
                accentColor = CyanAccent,
                romanUrduSubtitle = "Screen par floating Amrt AI button ko dabayein aur kisi bhi sawal ka jawab poochein, mushkil concepts ko asaan zaban me samjhein.",
                englishSubtitle = "Tap the floating Amrt AI companion anytime to ask tough questions, understand difficult concepts, and get instant explanations.",
                quickActionLabel = "Chat with Amrt AI",
                sceneKey = "ai_scene"
            )
        )
    }

    val totalVideoSeconds = 300 // 5 minutes (05:00)
    var currentSecond by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var subtitleLanguageUrdu by remember { mutableStateOf(true) }
    var expandedPlayer by remember { mutableStateOf(false) }

    // Coroutine ticker for simulated video playback
    LaunchedEffect(isPlaying, playbackSpeed) {
        if (isPlaying) {
            while (true) {
                val delayTime = (1000L / playbackSpeed).toLong()
                delay(delayTime)
                currentSecond = if (currentSecond >= totalVideoSeconds) {
                    0 // loop or restart
                } else {
                    currentSecond + 1
                }
            }
        }
    }

    // Determine current active chapter
    val currentChapter = chapters.lastOrNull { currentSecond >= it.startSecond } ?: chapters.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .testTag("video_guide_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Header App Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "COMPLETE VIDEO GUIDE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = CyanAccent
                                )
                            )
                        }
                        Text(
                            text = "App Use Karne Ka Tareeqa 🎬",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                // Subtitle language toggle chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { subtitleLanguageUrdu = !subtitleLanguageUrdu }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Subtitles,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (subtitleLanguageUrdu) "Urdu" else "English",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        // 🎥 INTERACTIVE VIDEO PLAYER COMPONENT
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(
                                currentChapter.accentColor.copy(alpha = 0.8f),
                                PrimaryIndigo.copy(alpha = 0.6f),
                                VioletAccent.copy(alpha = 0.8f)
                            )
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_player_card")
                ) {
                    Column {
                        // Video Screen Canvas (16:9)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9.5f)
                                .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                                .background(Color.Black)
                        ) {
                            // Video Background Image
                            Image(
                                painter = painterResource(id = R.drawable.img_video_guide_thumb),
                                contentDescription = "Video Tutorial Poster",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Scrim Overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0x88000000),
                                                Color(0x33000000),
                                                Color(0xEE090D16)
                                            )
                                        )
                                    )
                            )

                            // Simulated Active Animated Scene Overlay
                            AnimatedContent(
                                targetState = currentChapter.sceneKey,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "scene_transition",
                                modifier = Modifier.fillMaxSize()
                            ) { scene ->
                                VideoSceneSimulation(
                                    sceneKey = scene,
                                    chapter = currentChapter,
                                    isPlaying = isPlaying
                                )
                            }

                            // Top Badges on Video
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xCC000000),
                                    border = BorderStroke(1.dp, currentChapter.accentColor.copy(alpha = 0.6f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (isPlaying) SuccessGreen else WarningAmber)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isPlaying) "PLAYING HD" else "PAUSED",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 9.sp,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xCC000000)
                                ) {
                                    Text(
                                        text = "${formatTime(currentSecond)} / ${formatTime(totalVideoSeconds)}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Subtitle Bar (Bottom of Video Canvas)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xD9050811),
                                    border = BorderStroke(1.dp, Color(0xFF334155)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.GraphicEq,
                                            contentDescription = "Voiceover",
                                            tint = currentChapter.accentColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (subtitleLanguageUrdu) currentChapter.romanUrduSubtitle else currentChapter.englishSubtitle,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        // Video Scrubbing Bar / Progress Slider
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F172A))
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                        ) {
                            Slider(
                                value = currentSecond.toFloat(),
                                onValueChange = { currentSecond = it.toInt() },
                                valueRange = 0f..totalVideoSeconds.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = currentChapter.accentColor,
                                    activeTrackColor = currentChapter.accentColor,
                                    inactiveTrackColor = Color(0xFF334155)
                                ),
                                modifier = Modifier.height(26.dp)
                            )

                            // Controls Row (Rewind, Play/Pause, Fast Forward, Replay, Speed)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp, bottom = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Jump back 10s
                                IconButton(
                                    onClick = { currentSecond = (currentSecond - 10).coerceAtLeast(0) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FastRewind,
                                        contentDescription = "Back 10s",
                                        tint = Color.White
                                    )
                                }

                                // Play / Pause Big Glow Button
                                Surface(
                                    shape = CircleShape,
                                    color = currentChapter.accentColor,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clickable { isPlaying = !isPlaying }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = if (isPlaying) "Pause" else "Play",
                                            tint = Color(0xFF0F172A),
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                // Jump forward 10s
                                IconButton(
                                    onClick = { currentSecond = (currentSecond + 10).coerceAtMost(totalVideoSeconds) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FastForward,
                                        contentDescription = "Forward 10s",
                                        tint = Color.White
                                    )
                                }

                                // Replay from start
                                IconButton(
                                    onClick = { currentSecond = 0; isPlaying = true },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Replay,
                                        contentDescription = "Restart",
                                        tint = Color(0xFF94A3B8)
                                    )
                                }

                                // Speed selector chip
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF1E293B),
                                    modifier = Modifier.clickable {
                                        playbackSpeed = when (playbackSpeed) {
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            1.5f -> 2.0f
                                            else -> 1.0f
                                        }
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Speed,
                                            contentDescription = null,
                                            tint = CyanAccent,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "${playbackSpeed}x",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ⚡ ACTION SHORTCUT: TRY THIS CURRENT FEATURE NOW
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0F172A),
                border = BorderStroke(1.dp, currentChapter.accentColor.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = currentChapter.accentColor.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = currentChapter.icon,
                                    contentDescription = null,
                                    tint = currentChapter.accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Current Topic: ${currentChapter.title}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = currentChapter.accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Abhi yeh feature try karein!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Button(
                        onClick = {
                            when (currentChapter.id) {
                                1 -> onClose()
                                2 -> onCreateNote()
                                3 -> onOpenVoiceNotes()
                                4 -> onOpenImageScanner()
                                5 -> onOpenQuiz()
                                6 -> onOpenAiAssistant()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = currentChapter.accentColor,
                            contentColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Try Now 🚀",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // 📑 VIDEO CHAPTERS TIMESTAMPS SELECTOR
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Video Chapters (Tap to Jump)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(chapters) { chapter ->
                        val isSelected = currentChapter.id == chapter.id
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) chapter.accentColor.copy(alpha = 0.2f) else Color(0xFF1E293B),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) chapter.accentColor else Color(0xFF334155)
                            ),
                            modifier = Modifier
                                .clickable {
                                    currentSecond = chapter.startSecond
                                    isPlaying = true
                                }
                                .testTag("chapter_chip_${chapter.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = chapter.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) chapter.accentColor else Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = formatTime(chapter.startSecond),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = chapter.accentColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                    Text(
                                        text = chapter.romanUrduTitle,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 📘 COMPLETE WRITTEN STEP-BY-STEP GUIDE (URDU & ROMAN URDU MANUAL)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = CyanAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Complete Step-by-Step Guide / مکمل طریقہ کار",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                // Step 1: Notes & Summarizer
                TutorialGuideCard(
                    stepNumber = "1",
                    title = "Smart AI Notes & 1-Tap Summary",
                    subtitleUrdu = "Notes likhein aur AI se mukammal khulasa banwayein",
                    description = "1. Home screen se '+ Create Note' button dabayein.\n2. Apna title aur lecture notes likhein ya paste karein.\n3. Bottom me 'Transform with AI' ya 'AI Summarize' dabayein.\n4. Chand seconds me bullet points aur key takeaways tayar ho kar note me add ho jayenge.",
                    icon = Icons.Default.Description,
                    accentColor = PrimaryIndigo,
                    buttonLabel = "Create Note Now",
                    onAction = onCreateNote
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Step 2: Voice Notes in Urdu
                TutorialGuideCard(
                    stepNumber = "2",
                    title = "Voice Notes (Bol Kar Note Banayein)",
                    subtitleUrdu = "Urdu ya English me bol kar note tayar karein",
                    description = "1. Home screen par 'Voice Note' (Mic icon) par click karein.\n2. Audio recording start karein aur Urdu ya English me bolna shuru karein.\n3. 'Process with AI' dabayein. AI aapki speech ko automatically text me tabdeel karke clean note bana dega.",
                    icon = Icons.Default.Mic,
                    accentColor = WarningAmber,
                    buttonLabel = "Record Voice Note",
                    onAction = onOpenVoiceNotes
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Step 3: Book Scanner OCR
                TutorialGuideCard(
                    stepNumber = "3",
                    title = "Book & Paper Scanner (OCR)",
                    subtitleUrdu = "Kitaab ki tasweer khench kar text nikaalein",
                    description = "1. 'Scan / OCR' button dabayein.\n2. Camera se kitaab ke page ki photo khenchein ya gallery se document select karein.\n3. Gemini AI photo me maujood har word ko scan karke editable study note bana deta hai.",
                    icon = Icons.Default.PhotoCamera,
                    accentColor = VioletAccent,
                    buttonLabel = "Open OCR Scanner",
                    onAction = onOpenImageScanner
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Step 4: AI Exam Generator
                TutorialGuideCard(
                    stepNumber = "4",
                    title = "AI Exam Generator (20 - 1024 MCQs)",
                    subtitleUrdu = "Real exam tayar karein aur test de kar practice karein",
                    description = "1. Bottom bar se 'Quiz' tab me jayein ya 'Start AI Exam' dabayein.\n2. Topic ka naam likhein ya apna document select karein.\n3. Questions count choose karein: 20, 50, 100, 250, 500 ya 1024 MCQs!\n4. Time duration set karein (10 min se 2 hours).\n5. Test ke dauran questions ko flag karein aur aakhir me score aur explanation dekhein.",
                    icon = Icons.Default.Psychology,
                    accentColor = SuccessGreen,
                    buttonLabel = "Launch AI Exam",
                    onAction = onOpenQuiz
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Step 5: Amrt AI Chat
                TutorialGuideCard(
                    stepNumber = "5",
                    title = "24/7 Amrt AI Personal Tutor",
                    subtitleUrdu = "Har screen par floating AI button se madad lein",
                    description = "1. Screen ke bottom right par floating 'Amrt AI' button dabayein.\n2. Koi bhi sawaal type karein ya poochhein: 'Explain Quantum Physics in simple terms' ya 'Yeh math step kaise solve hota hai?'.\n3. Instant step-by-step guidance hasil karein.",
                    icon = Icons.Default.AutoAwesome,
                    accentColor = CyanAccent,
                    buttonLabel = "Ask Amrt AI",
                    onAction = onOpenAiAssistant
                )
            }
        }
    }
}

@Composable
fun TutorialGuideCard(
    stepNumber: String,
    title: String,
    subtitleUrdu: String,
    description: String,
    icon: ImageVector,
    accentColor: Color,
    buttonLabel: String,
    onAction: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        border = BorderStroke(1.dp, Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.2f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = stepNumber,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = accentColor
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = subtitleUrdu,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF161F30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFCBD5E1),
                        lineHeight = 20.sp,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor.copy(alpha = 0.2f),
                        contentColor = accentColor
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = buttonLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoSceneSimulation(
    sceneKey: String,
    chapter: VideoChapter,
    isPlaying: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scene_anim")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (sceneKey) {
            "home_scene" -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CyanAccent.copy(alpha = 0.2f),
                        border = BorderStroke(2.dp, CyanAccent.copy(alpha = pulseAlpha)),
                        modifier = Modifier.size(70.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Amrt Thinker AI Neural Hub",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Gemini 3.5 Flash • 5 Days Study Streak",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            "notes_scene" -> {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xCC0F172A),
                    border = BorderStroke(1.dp, PrimaryIndigo.copy(alpha = pulseAlpha)),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📝 Lecture Notes: Cell Biology",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryIndigo
                            ) {
                                Text(
                                    text = "AI Summarized",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• Mitochondria is the powerhouse of the cell\n• Ribosomes synthesize proteins\n• Cell membrane regulates transport",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        )
                    }
                }
            }

            "voice_scene" -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        color = WarningAmber.copy(alpha = 0.25f),
                        border = BorderStroke(2.dp, WarningAmber.copy(alpha = pulseAlpha)),
                        modifier = Modifier.size(68.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Listening... “Urdu & English Speech”",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = WarningAmber
                        )
                    )
                    Text(
                        text = "Converting audio to structured notes...",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                    )
                }
            }

            "camera_scene" -> {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xCC0F172A),
                    border = BorderStroke(1.5.dp, VioletAccent.copy(alpha = pulseAlpha)),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = VioletAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "OCR Book Scanner Active",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Scanning textbook pages... Text extracted with 99.8% accuracy into notes!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            "quiz_scene" -> {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xCC0F172A),
                    border = BorderStroke(1.5.dp, SuccessGreen.copy(alpha = pulseAlpha)),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Exam: 1024 MCQs Mode",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            )
                            Text(
                                text = "⏱️ 29:54 left",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = WarningAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Q1: What is the primary function of DNA?",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SuccessGreen.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Text(
                                text = "✓ A) Stores genetic information",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            "ai_scene" -> {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xCC0F172A),
                    border = BorderStroke(1.5.dp, CyanAccent.copy(alpha = pulseAlpha)),
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "You: “Explain Newton's Third Law in Urdu”",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Amrt AI: Har action ka ek barabar aur mukhalif reaction hota hai. For example jab rocket gas chorta hai to wo agay barhta hai.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyanAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
