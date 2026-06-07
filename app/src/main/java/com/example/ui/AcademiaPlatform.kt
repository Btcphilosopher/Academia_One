package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.ui.theme.Crimson
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.SlateGrey
import com.example.ui.theme.LightBackground
import com.example.ui.theme.WarmSand
import com.example.ui.theme.BorderColor
import com.example.ui.theme.IndigoAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademiaPlatform(
    viewModel: AcademiaViewModel,
    modifier: Modifier = Modifier
) {
    val studentProfile by viewModel.studentProfile.collectAsState()
    val timetableEvents by viewModel.timetableEvents.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val researchProjects by viewModel.researchProjects.collectAsState()
    val libraryBookings by viewModel.libraryBookings.collectAsState()
    val messages by viewModel.messages.collectAsState()

    var activeTab by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Determine Responsive Window Size Class
    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp >= 600

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (!isTablet) {
                BottomNavigationBar(
                    selectedTab = activeTab,
                    onTabSelected = { activeTab = it }
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tablet Left Sidebar Rail
            if (isTablet) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    header = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DeepNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = "Logo", tint = GoldAccent, modifier = Modifier.size(28.dp))
                        }
                    }
                ) {
                    NavigationRailItem(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        icon = { Icon(Icons.Default.Dashboard, "Home") },
                        label = { Text("Home", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        icon = { Icon(Icons.Default.CalendarToday, "Schedule") },
                        label = { Text("Schedule", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        icon = { Icon(Icons.Default.MenuBook, "LMS") },
                        label = { Text("LMS", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 3,
                        onClick = { activeTab = 3 },
                        icon = { Icon(Icons.Default.AssignmentTurnedIn, "Submissions") },
                        label = { Text("Submissions", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 4,
                        onClick = { activeTab = 4 },
                        icon = { Icon(Icons.Default.Science, "Research") },
                        label = { Text("Research", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 5,
                        onClick = { activeTab = 5 },
                        icon = { Icon(Icons.Default.MeetingRoom, "Campus Hub") },
                        label = { Text("Campus Hub", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 6,
                        onClick = { activeTab = 6 },
                        icon = { Icon(Icons.Default.Navigation, "Nav Map") },
                        label = { Text("Map", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 7,
                        onClick = { activeTab = 7 },
                        icon = { Icon(Icons.Default.Email, "Board") },
                        label = { Text("Board", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                    NavigationRailItem(
                        selected = activeTab == 8,
                        onClick = { activeTab = 8 },
                        icon = { Icon(Icons.Default.Chat, "Advisor") },
                        label = { Text("Advisor", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }

            // Main Tab Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (activeTab) {
                    0 -> HomeDashboardScreen(
                        viewModel = viewModel,
                        profile = studentProfile,
                        events = timetableEvents,
                        assignments = assignments,
                        messages = messages,
                        onNavigateToTab = { activeTab = it }
                    )
                    1 -> TimetableScreen(
                        viewModel = viewModel,
                        events = timetableEvents,
                        profile = studentProfile
                    )
                    2 -> LmsScreen(
                        profile = studentProfile,
                        events = timetableEvents
                    )
                    3 -> SubmissionScreen(
                        viewModel = viewModel,
                        assignments = assignments,
                        profile = studentProfile
                    )
                    4 -> ResearchScreen(
                        viewModel = viewModel,
                        projects = researchProjects,
                        profile = studentProfile
                    )
                    5 -> CampusHubScreen(
                        viewModel = viewModel,
                        bookings = libraryBookings,
                        profile = studentProfile
                    )
                    6 -> CampusNavigationScreen()
                    7 -> CommunicationScreen(
                        viewModel = viewModel,
                        messages = messages,
                        profile = studentProfile
                    )
                    8 -> AdvisorChatScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// Bottom Navigation for mobile screens
@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Dashboard, "Home") },
            label = { Text("Home", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.CalendarToday, "Schedule") },
            label = { Text("Calendar", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.MenuBook, "LMS") },
            label = { Text("LMS", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.AssignmentTurnedIn, "Drafts") },
            label = { Text("Submissions", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = selectedTab == 8,
            onClick = { onTabSelected(8) },
            icon = { Icon(Icons.Default.Chat, "AI") },
            label = { Text("AI Advisor", fontSize = 10.sp) }
        )
    }
}

// ----------------------------------------------------
// TABS IMPLEMENTATION
// ----------------------------------------------------

// TAB 0: HOME DASHBOARD
@Composable
fun HomeDashboardScreen(
    viewModel: AcademiaViewModel,
    profile: StudentProfile?,
    events: List<TimetableEvent>,
    assignments: List<Assignment>,
    messages: List<Message>,
    onNavigateToTab: (Int) -> Unit
) {
    var expandedRoles by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dynamic top banner mirroring the exact HTML design
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "MICHAELMAS TERM",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        letterSpacing = 2.sp,
                        modifier = Modifier.alpha(0.7f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Academia One",
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = DeepNavy
                    )
                }
                // Exquisite radial gradient avatar matching HTML
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFF94A3B8), Color(0xFF475569))
                            )
                        )
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }

        // App Identity and Role Switcher Block
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("id_identity_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "STUDENT IDENTITY SYSTEM",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.6f),
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = profile?.name ?: "Alexander Mercer",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = profile?.department ?: "Department of Computer Science",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Adaptive Interactive Role Badge Dropdown
                        Box {
                            Button(
                                onClick = { expandedRoles = !expandedRoles },
                                colors = ButtonDefaults.buttonColors(containerColor = Crimson),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = profile?.role ?: "Undergraduate",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Switch Role",
                                        tint = Color.White,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = expandedRoles,
                                onDismissRequest = { expandedRoles = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Undergraduate") },
                                    onClick = {
                                        viewModel.switchRole("Undergraduate")
                                        expandedRoles = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Postgraduate") },
                                    onClick = {
                                        viewModel.switchRole("Postgraduate")
                                        expandedRoles = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Faculty") },
                                    onClick = {
                                        viewModel.switchRole("Faculty")
                                        expandedRoles = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Administration") },
                                    onClick = {
                                        viewModel.switchRole("Administration")
                                        expandedRoles = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Student ID", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text(profile?.id ?: "UG-2026-8951", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        }
                        Column {
                            Text("Cumulative GPA", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("${profile?.gpa ?: 3.92} / 4.0", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 14.sp)
                        }
                        Column {
                            Text("Academic Progress", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Text("${profile?.completedCredits ?: 120} / ${profile?.totalCredits ?: 180} Credits", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Campus Alerts announcement bar
        item {
            val alerts = messages.filter { it.isAnnouncement }
            if (alerts.isNotEmpty()) {
                val activeAlert = alerts.first()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Crimson.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, Crimson.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Crimson, CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Warning, "Alert", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CAMPUS NOTIFICATION", color = Crimson, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                            Text(activeAlert.content, fontSize = 12.sp, color = DeepNavy, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                        TextButton(onClick = { onNavigateToTab(7) }) {
                            Text("Read All", color = Crimson, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Today's Timetable Row overview
        item {
            val todayEvents = events.take(2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Schedule",
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy
                )
                TextButton(onClick = { onNavigateToTab(1) }) {
                    Text("View All", color = Crimson, fontWeight = FontWeight.Bold)
                }
            }
            if (todayEvents.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, BorderColor)
                ) {
                    Text(
                        "No lectures scheduled for today. Enjoy your academic study block!",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 13.sp,
                        color = SlateGrey,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    todayEvents.forEach { event ->
                        val conflictColor = if (event.isConflict) Crimson else DeepNavy
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, if (event.isConflict) Crimson.copy(alpha = 0.5f) else BorderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(conflictColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                        .padding(10.dp)
                                        .width(54.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(event.startTime, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = conflictColor)
                                        Text("to", fontSize = 8.sp, color = SlateGrey)
                                        Text(event.endTime, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = conflictColor)
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(event.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                                        if (event.isConflict) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Badge(containerColor = Crimson) { Text("CLASH", color = Color.White, fontSize = 8.sp) }
                                        }
                                    }
                                    Text("${event.courseCode} • ${event.professor}", fontSize = 12.sp, color = SlateGrey)
                                    Row(
                                        modifier = Modifier.padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Room, "Location", tint = GoldAccent, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(event.location, fontSize = 11.sp, color = SlateGrey)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Deadlines summary & Grades panel (Gorgeously rendered Editorial stats grid)
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Pending Assignments (Warm Sand Card with thin border)
                val pending = assignments.filter { it.status == "Pending" || it.status == "Draft Saved" }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = WarmSand),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(16.dp).heightIn(min = 140.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccessTime, "Deadlines", tint = GoldAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DEADLINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            if (pending.isEmpty()) {
                                Text("Clear skies! No pending work.", fontSize = 12.sp, color = SlateGrey)
                            } else {
                                pending.take(1).forEach { ass ->
                                    Text(ass.title, fontFamily = FontFamily.Serif, fontSize = 16.sp, color = DeepNavy, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("Due in ${ass.dueDate}", fontSize = 11.sp, color = SlateGrey, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        TextButton(onClick = { onNavigateToTab(3) }, contentPadding = PaddingValues(0.dp)) {
                            Text("Submissions →", fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Recent Grades Summary (Charcoal Slate-900 Premium Card)
                val graded = assignments.filter { it.status == "Graded" }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = DeepNavy),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp).heightIn(min = 140.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TrendingUp, "Grades", tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GPA PROGRESS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.6f),
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            if (graded.isEmpty()) {
                                Text("3.92 GPA", fontFamily = FontFamily.Serif, fontSize = 20.sp, color = Color.White)
                                Text("Top 5% of Cohort", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            } else {
                                graded.take(1).forEach { ass ->
                                    Text(ass.grade ?: "A+", fontFamily = FontFamily.Serif, fontSize = 24.sp, color = Color.White)
                                    Text(ass.title, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                        TextButton(onClick = { onNavigateToTab(5) }, contentPadding = PaddingValues(0.dp)) {
                            Text("View Transcript →", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick AI Advisor Access Card (The beautiful Prestige Suggestion Banner)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTab(8) },
                colors = CardDefaults.cardColors(containerColor = Crimson),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "AI ASSISTANT",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .background(Color.White, CircleShape)
                        )
                        Text(
                            "GUIDANCE",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "\"You have a gap between 11:00 and 13:00 today. Would you like to check out available quiet study cabins or outline a thesis structure?\"",
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Consult Advisor →",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// TAB 1: TIMETABLE & CLASH SOLVER
@Composable
fun TimetableScreen(
    viewModel: AcademiaViewModel,
    events: List<TimetableEvent>,
    profile: StudentProfile? = null
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("Mon") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Prestige Scheduling Engine",
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy
                )
                Text("Conflict Monitoring System", fontSize = 11.sp, color = SlateGrey)
            }

            // Quick solver button when clashes are detected
            val hasConflict = events.any { it.isConflict }
            if (hasConflict) {
                Button(
                    onClick = { viewModel.autoResolveClashes() },
                    colors = ButtonDefaults.buttonColors(containerColor = Crimson),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, "Resolve", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Auto-Resolve Clashes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else if (profile?.role == "Faculty" || profile?.role == "Administration") {
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Class")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Schedule Class")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mon - Fri Days Picker row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
            days.forEach { day ->
                val isSelected = selectedDay == day
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) DeepNavy else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) DeepNavy else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedDay = day }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else DeepNavy,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clashing alert banner
        val clashesOnDay = events.filter { it.dayOfWeek == selectedDay && it.isConflict }
        if (clashesOnDay.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Crimson.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, Crimson.copy(alpha = 0.3f)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, "Clash Detected", tint = Crimson, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CONFLICT DETECTED", color = Crimson, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Multiple lectures overlap on $selectedDay. Faculty rules require manual or auto rescheduling.", fontSize = 11.sp, color = DeepNavy)
                    }
                }
            }
        }

        // Daily Classes list
        val dayEvents = events.filter { it.dayOfWeek == selectedDay }
        if (dayEvents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CalendarToday, "No Class", tint = SlateGrey.copy(alpha = 0.3f), modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No events scheduled for $selectedDay.", fontSize = 14.sp, color = SlateGrey)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(dayEvents) { event ->
                    val colorAccent = if (event.isConflict) Crimson else DeepNavy
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, if (event.isConflict) Crimson.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(70.dp)
                                    .padding(end = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(event.startTime, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorAccent)
                                Icon(Icons.Default.ArrowDownward, "to", tint = SlateGrey, modifier = Modifier.size(12.dp))
                                Text(event.endTime, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colorAccent)
                            }

                            Spacer(modifier = Modifier.width(4.dp))
                            VerticalDivider(color = colorAccent.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(event.title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = DeepNavy)
                                    if (profile?.role == "Faculty" || profile?.role == "Administration") {
                                        IconButton(
                                            onClick = { viewModel.deleteTimetableEvent(event) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, "Cancel Class", tint = Crimson, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                                Text("${event.courseCode} — ${event.type}", style = MaterialTheme.typography.bodyMedium, color = SlateGrey)
                                Text("Lecturer: ${event.professor}", fontSize = 11.sp, color = SlateGrey)

                                Box(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .background(LightBackground, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Room, "Location", tint = GoldAccent, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(event.location, fontSize = 11.sp, color = DeepNavy)
                                    }
                                }

                                Text(
                                    text = event.description,
                                    fontSize = 11.sp,
                                    color = SlateGrey,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Schedule Custom Dialog
    if (showAddDialog) {
        var addTitle by remember { mutableStateOf("") }
        var addCourse by remember { mutableStateOf("") }
        var addProf by remember { mutableStateOf("") }
        var addLoc by remember { mutableStateOf("") }
        var addStart by remember { mutableStateOf("11:00") }
        var addEnd by remember { mutableStateOf("12:30") }
        var addDesc by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showAddDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Schedule Elite Academy Lecture", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                    TextField(value = addTitle, onValueChange = { addTitle = it }, label = { Text("Lecture Name") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = addCourse, onValueChange = { addCourse = it }, label = { Text("Course Code (e.g. CS-181)") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = addProf, onValueChange = { addProf = it }, label = { Text("Professor") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = addLoc, onValueChange = { addLoc = it }, label = { Text("Hall / Room") }, modifier = Modifier.fillMaxWidth())

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextField(value = addStart, onValueChange = { addStart = it }, label = { Text("Start Time") }, modifier = Modifier.weight(1f))
                        TextField(value = addEnd, onValueChange = { addEnd = it }, label = { Text("End Time") }, modifier = Modifier.weight(1f))
                    }

                    TextField(value = addDesc, onValueChange = { addDesc = it }, label = { Text("Topic Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 2)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showAddDialog = false }) { Text("Dismiss") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (addTitle.isNotEmpty()) {
                                    viewModel.addTimetableEvent(
                                        TimetableEvent(
                                            title = addTitle,
                                            courseCode = addCourse,
                                            type = "Lecture",
                                            dayOfWeek = selectedDay,
                                            startTime = addStart,
                                            endTime = addEnd,
                                            location = addLoc,
                                            professor = addProf,
                                            description = addDesc
                                        )
                                    )
                                    showAddDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                        ) {
                            Text("Schedule")
                        }
                    }
                }
            }
        }
    }
}

// TAB 2: LEARNING MANAGEMENT SYSTEM (LMS)
@Composable
fun LmsScreen(
    profile: StudentProfile?,
    events: List<TimetableEvent>
) {
    var selectedCourse by remember { mutableStateOf<String?>(null) }
    var isStreamingLecture by remember { mutableStateOf(false) }
    var streamProgress by remember { mutableFloatStateOf(0.24f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedCourse == null) {
            // Courses listing
            Text(
                text = "Learning Management Server",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = DeepNavy
            )
            Text("Modular syllabus, interactive modules, and multimedia lectures.", fontSize = 11.sp, color = SlateGrey)

            Spacer(modifier = Modifier.height(16.dp))

            // Display standard enrolled codes
            val uniqueCourses = events.map { it.courseCode }.distinct()
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uniqueCourses) { code ->
                    val ev = events.find { it.courseCode == code }
                    val courseTitle = ev?.title ?: "Academic Component"
                    val instructor = ev?.professor ?: "Advisory Board"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCourse = code },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(DeepNavy.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(code, fontWeight = FontWeight.ExtraBold, color = DeepNavy, fontSize = 12.sp)
                                }
                                Icon(Icons.Default.ArrowForwardIos, "Enter Module", tint = SlateGrey, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(courseTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                            Text("Syllabus Lead: $instructor", fontSize = 12.sp, color = SlateGrey)

                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Weekly Chapters: 12 Modules", fontSize = 11.sp, color = SlateGrey)
                                Text("Completion: 4 / 12 Modules", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Crimson)
                            }
                        }
                    }
                }
            }
        } else {
            // Lecture Material Inside Course View
            val code = selectedCourse!!
            val ev = events.find { it.courseCode == code }
            val courseTitle = ev?.title ?: "Academic Component"

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { selectedCourse = null }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(code, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    Text(courseTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LMS options tabs: Recordings, Readings, Quizzes
            var currentSubTab by remember { mutableStateOf(0) }
            ScrollableTabRow(
                selectedTabIndex = currentSubTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent
            ) {
                Tab(selected = currentSubTab == 0, onClick = { currentSubTab = 0 }) { Text("Lectures", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold) }
                Tab(selected = currentSubTab == 1, onClick = { currentSubTab = 1 }) { Text("Reading List", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold) }
                Tab(selected = currentSubTab == 2, onClick = { currentSubTab = 2 }) { Text("Assessments", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (currentSubTab) {
                0 -> {
                    // Multimedia Recordings Player Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DeepNavy),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("HIGH-DEFINITION BROADCAST", color = GoldAccent, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                    Text("Week 4: Advanced Core Implementations", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Crimson, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("CLOUDHUB", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Interactive streaming player timeline
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(Color.Black, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isStreamingLecture) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(color = GoldAccent, modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Streaming Live Audio and Code Slides...", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircleFilled,
                                        contentDescription = "Play Lecture",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clickable { isStreamingLecture = true }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Slider(
                                value = streamProgress,
                                onValueChange = { streamProgress = it },
                                colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("18:45", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                                Text("50:00", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                            }
                        }
                    }
                }
                1 -> {
                    // Syllabus Reads
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            Text("Assigned Research Materials", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                        }
                        item {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("1. Foundations of Computational Physics (Syllabus Core)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                                    Text("Read Chapters 4 and 5 for homework assignments.", fontSize = 11.sp, color = SlateGrey)
                                }
                            }
                        }
                        item {
                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("2. Distributed Log Replication consensus protocols (Raft, Paxos)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                                    Text("Recommended reference material for postgraduate research tracks.", fontSize = 11.sp, color = SlateGrey)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Micro Assessments / Quizzes
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Concept Checklist: Module 4 Quiz", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                            Text("Duration: 15 minutes | Status: Pending Board Review", fontSize = 12.sp, color = SlateGrey)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Commence Concept Quiz")
                            }
                        }
                    }
                }
            }
        }
    }
}

// TAB 3: ASSIGNMENT SUBMISSION DESK
@Composable
fun SubmissionScreen(
    viewModel: AcademiaViewModel,
    assignments: List<Assignment>,
    profile: StudentProfile?
) {
    var selectedAssId by remember { mutableStateOf<Long?>(null) }
    var draftInputText by remember { mutableStateOf("") }

    // Faculty grading fields
    var gradingScore by remember { mutableStateOf("A+") }
    var gradingFeedback by remember { mutableStateOf("") }

    val role = profile?.role ?: "Undergraduate"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedAssId == null) {
            Text(
                text = "Assignment & Submissions Desk",
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = DeepNavy
            )
            Text("Syllabus requirements, draft versioning control, plag scans, and rubrics.", fontSize = 11.sp, color = SlateGrey)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(assignments) { ass ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedAssId = ass.id
                                draftInputText = ass.latestSubmissionText ?: ""
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ass.courseCode, fontWeight = FontWeight.ExtraBold, color = Crimson, fontSize = 12.sp)

                                val statusColor = when (ass.status) {
                                    "Graded" -> Color(0xFF2E7D32)
                                    "Submitted" -> DeepNavy
                                    "Draft Saved" -> GoldAccent
                                    else -> SlateGrey
                                }
                                Box(
                                    modifier = Modifier
                                        .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(ass.status, fontWeight = FontWeight.Bold, color = statusColor, fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(ass.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                            Text("Due Date: ${ass.dueDate}", fontSize = 12.sp, color = SlateGrey)

                            if (ass.grade != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Score Awarded: ${ass.grade}", fontWeight = FontWeight.ExtraBold, color = Crimson, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // Detailed submission portal
            val ass = assignments.find { it.id == selectedAssId }!!

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { selectedAssId = null }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(ass.courseCode, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                    Text("Submissions Console", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(ass.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                            Text("Syllabus Deadline: ${ass.dueDate}", fontSize = 12.sp, color = SlateGrey)
                            Text("Latest Draft Versions Saved: ${ass.draftsCount} uploads", fontSize = 12.sp, color = SlateGrey)
                        }
                    }
                }

                // AI integrity check and plagiarism scoring overview
                if (ass.plagiarismScore != null || ass.complianceFeedback != null) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("INTEGRITY COMPLIANCE REPORT", color = Crimson, fontWeight = FontWeight.ExtraBold, fontSize = 10.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                if (ass.plagiarismScore != null) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.VerifiedUser, "Plag Check", tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Plagiarism Index: ${ass.plagiarismScore}% (Academic standard met)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                    }
                                }
                                Text(ass.complianceFeedback ?: "", fontSize = 11.sp, color = SlateGrey)
                            }
                        }
                    }
                }

                // If graded, show feedback
                if (ass.professorComments != null) {
                    item {
                        Card(colors = CardDefaults.cardColors(containerColor = Crimson.copy(alpha = 0.04f))) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("FACULTY SCORE & CRITIQUE", fontWeight = FontWeight.ExtraBold, color = Crimson, fontSize = 11.sp)
                                Text("Grade: ${ass.grade}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                                Text(ass.professorComments ?: "", fontSize = 12.sp, color = SlateGrey)
                            }
                        }
                    }
                }

                // Undergrad/Postgrad submission desk console
                if (role == "Undergraduate" || role == "Postgraduate") {
                    item {
                        Column {
                            Text("Draft Workspace Editor", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = draftInputText,
                                onValueChange = { draftInputText = it },
                                placeholder = { Text("Paste academic thesis text or problem set write-up here...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                maxLines = 10
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.submitAssignmentDraft(ass.id, draftInputText) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SlateGrey),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Analyze Compliance")
                                }

                                Button(
                                    onClick = {
                                        viewModel.submitAssignmentDraft(ass.id, draftInputText)
                                        viewModel.submitFinalAssignment(ass.id)
                                        selectedAssId = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Submit Final Draft")
                                }
                            }
                        }
                    }
                }

                // Faculty grading console
                if (role == "Faculty") {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Post Grade & Feedback Room", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Crimson)
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = gradingScore,
                                    onValueChange = { gradingScore = it },
                                    label = { Text("Score Assigned (e.g. A+, 95/100)") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = gradingFeedback,
                                    onValueChange = { gradingFeedback = it },
                                    label = { Text("Comments") },
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 3
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.facultyGradeAssignment(ass.id, gradingScore, gradingFeedback)
                                        selectedAssId = null
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Commit Final Feedback")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// TAB 4: RESEARCH & CITATIONS DESK
@Composable
fun ResearchScreen(
    viewModel: AcademiaViewModel,
    projects: List<ResearchProject>,
    profile: StudentProfile?
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Research & Publications Desk",
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy
                )
                Text("APA, Chicago, and MLA citation engines, Supervisor transcripts", fontSize = 11.sp, color = SlateGrey)
            }

            if (profile?.role == "Postgraduate" || profile?.role == "Faculty") {
                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, "Register Research")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Project", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (projects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Science, "No Project", tint = SlateGrey.copy(alpha = 0.3f), modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No registered research papers in the database.", fontSize = 14.sp, color = SlateGrey)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(projects) { project ->
                    var activeCitationStyle by remember { mutableStateOf("APA") }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ACADEMIC CAPSTONE MODULE", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(project.title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DeepNavy)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Crimson.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(project.status, fontWeight = FontWeight.Bold, color = Crimson, fontSize = 10.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(project.abstractText, fontSize = 12.sp, color = SlateGrey)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Syllabus Supervisor: ${project.supervisor}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)

                            if (project.supervisorFeedback != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(DeepNavy.copy(alpha = 0.05f), RoundedCornerShape(6.dp))
                                        .padding(10.dp)
                                ) {
                                    Text("Supervisor Feedback: '${project.supervisorFeedback}'", fontSize = 11.sp, color = SlateGrey)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive citation generation options
                            Text("Automatic Citation Generator", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DeepNavy)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("APA", "MLA", "Chicago").forEach { format ->
                                    val active = activeCitationStyle == format
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = if (active) DeepNavy else LightBackground,
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                            .clickable { activeCitationStyle = format }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(format, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = if (active) Color.White else DeepNavy)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            val displayedCitation = when (activeCitationStyle) {
                                "MLA" -> project.citationMla
                                "Chicago" -> project.citationChicago
                                else -> project.citationApa
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(LightBackground, RoundedCornerShape(4.dp))
                                    .padding(10.dp)
                            ) {
                                Text(displayedCitation, fontSize = 11.sp, color = DeepNavy)
                            }
                        }
                    }
                }
            }
        }
    }

    // New Project Dialog
    if (showCreateDialog) {
        var crTitle by remember { mutableStateOf("") }
        var crAbstract by remember { mutableStateOf("") }
        var crSupervisor by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showCreateDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Register Prestige Research Capstone", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                    TextField(value = crTitle, onValueChange = { crTitle = it }, label = { Text("Research Title") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = crAbstract, onValueChange = { crAbstract = it }, label = { Text("Abstract Outline") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = crSupervisor, onValueChange = { crSupervisor = it }, label = { Text("Lead Supervisor") }, modifier = Modifier.fillMaxWidth())

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showCreateDialog = false }) { Text("Dismiss") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (crTitle.isNotEmpty()) {
                                    viewModel.addResearchProject(crTitle, crAbstract, crSupervisor)
                                    showCreateDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                        ) {
                            Text("Create")
                        }
                    }
                }
            }
        }
    }
}

// TAB 5: CAMPUS SERVICES & ACADEMIC TRANSCRIPTS
@Composable
fun CampusHubScreen(
    viewModel: AcademiaViewModel,
    bookings: List<LibraryBooking>,
    profile: StudentProfile?
) {
    var showBookDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Campus Services & Registry",
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Italic,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = DeepNavy
        )
        Text("Library bookings, secure dynamic transcripts, mental health, and student ID.", fontSize = 11.sp, color = SlateGrey)

        Spacer(modifier = Modifier.height(16.dp))

        var currentAreaTab by remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = currentAreaTab) {
            Tab(selected = currentAreaTab == 0, onClick = { currentAreaTab = 0 }) { Text("ID & Registry", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold) }
            Tab(selected = currentAreaTab == 1, onClick = { currentAreaTab = 1 }) { Text("Library Book", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold) }
            Tab(selected = currentAreaTab == 2, onClick = { currentAreaTab = 2 }) { Text("Support Hub", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (currentAreaTab) {
            0 -> {
                // Identity Transcript and QR code verification representation
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Dynamic Student ID QR Code Simulation (Canvas representation)
                                Text("OFFICIAL DIGITAL IDENTIFICATION", color = Crimson, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .size(140.dp)
                                        .background(Color.White)
                                        .border(BorderStroke(2.dp, DeepNavy), RoundedCornerShape(8.dp))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Custom draw beautiful mosaic code
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val totalDots = 10
                                        val dotWidth = size.width / totalDots
                                        val dotHeight = size.height / totalDots
                                        for (r in 0 until totalDots) {
                                            for (c in 0 until totalDots) {
                                                // Random but constant design based on indices
                                                if ((r * c + r) % 3 == 0 || (r + c) % 5 == 0) {
                                                    drawRect(
                                                        color = DeepNavy,
                                                        topLeft = Offset(c * dotWidth + 2f, r * dotHeight + 2f),
                                                        size = Size(dotWidth - 4f, dotHeight - 4f)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(profile?.name ?: "Alexander Mercer", fontWeight = FontWeight.Bold, color = DeepNavy)
                                Text(profile?.id ?: "UG-2026-8951", fontSize = 12.sp, color = SlateGrey)
                            }
                        }
                    }

                    item {
                        // Transcript registry summary
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Verified Registrar Transcript", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepNavy)
                                Text("Cumulative GP: ${profile?.gpa ?: 3.92} / 4.0", fontSize = 12.sp, color = SlateGrey)
                                Text("Total Term Credits Completed: ${profile?.completedCredits ?: 120} Credits", fontSize = 12.sp, color = SlateGrey)

                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Status: Enrollment Compliant", color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Button(
                                        onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                        modifier = Modifier.height(32.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp)
                                    ) {
                                        Text("Export verification PDF", fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Class and Library Study Room reserving
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Library study spaces", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                    Button(
                        onClick = { showBookDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                    ) {
                        Text("Reserve Room", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (bookings.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No upcoming library bookings found.", color = SlateGrey)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(bookings) { b ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(b.roomName, fontWeight = FontWeight.Bold, color = DeepNavy)
                                        Text("Date: ${b.date} | Hours: ${b.timeSlot}", fontSize = 12.sp, color = SlateGrey)
                                        Text("Syllabus Purpose: '${b.purpose}'", fontSize = 11.sp, color = SlateGrey)
                                    }
                                    IconButton(onClick = { viewModel.cancelLibraryBooking(b) }) {
                                        Icon(Icons.Default.Cancel, "Cancel Book", tint = Crimson)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Integrated Student Support Systems
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Prestige Student Support Services Hub", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Crimson)
                        Spacer(modifier = Modifier.height(8.dp))
                        listOf(
                            Pair("Prestige Career Advisory Board", "Schedule resumes or placement portfolio interviews."),
                            Pair("Academic Mental Health Guidance", "Access secure, top-tier private university medical counsel."),
                            Pair("Dynamic International Visas Admin", "Ensure FERPA compliance and active study Visa paperwork.")
                        ).forEach { item ->
                            Text("• ${item.first}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                            Text("   ${item.second}", fontSize = 11.sp, color = SlateGrey, modifier = Modifier.padding(bottom = 6.dp))
                        }
                    }
                }
            }
        }
    }

    // Room Dialog
    if (showBookDialog) {
        var roomChoice by remember { mutableStateOf("Widener Library Study 402") }
        var dateChoice by remember { mutableStateOf("2026-06-08") }
        var slotChoice by remember { mutableStateOf("14:00 - 16:00") }
        var purposeText by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showBookDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Reserve Academia Study Area", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                    TextField(value = roomChoice, onValueChange = { roomChoice = it }, label = { Text("Selected Room") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = dateChoice, onValueChange = { dateChoice = it }, label = { Text("Reservation Date") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = slotChoice, onValueChange = { slotChoice = it }, label = { Text("Time Slot") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = purposeText, onValueChange = { purposeText = it }, label = { Text("Purpose Details") }, modifier = Modifier.fillMaxWidth())

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showBookDialog = false }) { Text("Dismiss") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (purposeText.isNotEmpty()) {
                                    viewModel.bookLibraryRoom(roomChoice, dateChoice, slotChoice, purposeText)
                                    showBookDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)
                        ) {
                            Text("Confirm Booking")
                        }
                    }
                }
            }
        }
    }
}

// TAB 6: CAMPUS NAVIGATION SYSTEM (MAP CANVAS)
@Composable
fun CampusNavigationScreen() {
    var searchBuildingInput by remember { mutableStateOf("") }
    var highlightedBuilding by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Campus Navigation Engine",
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Italic,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = DeepNavy
        )
        Text("Interactive vector layouts, active hall vacancy indexes, and paths.", fontSize = 11.sp, color = SlateGrey)

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        TextField(
            value = searchBuildingInput,
            onValueChange = {
                searchBuildingInput = it
                highlightedBuilding = if (it.isNotEmpty()) it else null
            },
            placeholder = { Text("Search building name... (e.g., Library, Science Labs)") },
            leadingIcon = { Icon(Icons.Default.Search, "Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large graphics canvas map representational card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw abstract aesthetic campus blueprint layout lines
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A)),
                            center = Offset(w / 2, h / 2)
                        )
                    )

                    // Coordinate system lines (Blueprint graph paper design)
                    val cols = 8
                    val rows = 12
                    for (c in 0..cols) {
                        val cx = c * (w / cols)
                        drawLine(Color.White.copy(0.06f), Offset(cx, 0f), Offset(cx, h))
                    }
                    for (r in 0..rows) {
                        val ry = r * (h / rows)
                        drawLine(Color.White.copy(0.06f), Offset(0f, ry), Offset(w, ry))
                    }

                    // Draw abstract campus pathways/sidewalks
                    drawLine(Color(0xFF334155), Offset(100f, 100f), Offset(w - 100f, h - 200f), strokeWidth = 8f)
                    drawLine(Color(0xFF334155), Offset(w - 200f, 150f), Offset(150f, h - 100f), strokeWidth = 8f)

                    // Draw Buildings as styled blueprint boxes
                    // 1. Widener Library
                    val libHighlight = if (highlightedBuilding?.contains("lib", ignoreCase = true) == true) Crimson else GoldAccent
                    drawRect(
                        color = Color(0xFF1E1E2F),
                        topLeft = Offset(100f, 150f),
                        size = Size(200f, 150f)
                    )
                    drawRect(
                        color = libHighlight,
                        topLeft = Offset(100f, 150f),
                        size = Size(200f, 150f),
                        style = Stroke(width = 4f)
                    )

                    // 2. Science Labs & Lyman building
                    val sciHighlight = if (highlightedBuilding?.contains("science", ignoreCase = true) == true || highlightedBuilding?.contains("lab", ignoreCase = true) == true) Crimson else GoldAccent
                    drawRect(
                        color = Color(0xFF1E1E2F),
                        topLeft = Offset(w - 300f, 200f),
                        size = Size(220f, 180f)
                    )
                    drawRect(
                        color = sciHighlight,
                        topLeft = Offset(w - 300f, 200f),
                        size = Size(220f, 180f),
                        style = Stroke(width = 4f)
                    )

                    // 3. Lovelace Hall (Computing)
                    val compHighlight = if (highlightedBuilding?.contains("love", ignoreCase = true) == true || highlightedBuilding?.contains("comp", ignoreCase = true) == true) Crimson else GoldAccent
                    drawRect(
                        color = Color(0xFF1E1E2F),
                        topLeft = Offset(150f, h - 350f),
                        size = Size(240f, 160f)
                    )
                    drawRect(
                        color = compHighlight,
                        topLeft = Offset(150f, h - 350f),
                        size = Size(240f, 160f),
                        style = Stroke(width = 4f)
                    )

                    // 4. Student Union
                    val unionHighlight = if (highlightedBuilding?.contains("union", ignoreCase = true) == true) Crimson else GoldAccent
                    drawRect(
                        color = Color(0xFF1E1E2F),
                        topLeft = Offset(w - 320f, h - 300f),
                        size = Size(220f, 160f)
                    )
                    drawRect(
                        color = unionHighlight,
                        topLeft = Offset(w - 320f, h - 300f),
                        size = Size(220f, 160f),
                        style = Stroke(width = 4f)
                    )

                    // If highlighted screen, draw navigation target line (Entrance to Building Lovelace)
                    drawLine(
                        color = Crimson,
                        start = Offset(w / 2, h - 40f),
                        end = Offset(270f, h - 270f),
                        strokeWidth = 6f
                    )
                }

                // Layout titles inside Box to overlap the Canvas
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Widener Library\n(Room 402 - Active)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.offset(110.dp, 60.dp))
                    Text("Quantum Science Building\n(Lyman Labs - Occupied)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.align(Alignment.TopEnd).padding(top = 90.dp, end = 50.dp))
                    Text("Lovelace Computing Hall\n(Lecture Rooms - Safe)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 140.dp, start = 60.dp))
                    Text("Student Union & Atrium\n(Atrium - High Occupancy)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 120.dp, end = 40.dp))

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .background(DeepNavy.copy(alpha = 0.85f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Interactive Campus Core (Entrance Nav Line Active)", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// TAB 7: COMMUNICATION BOARD / INBOX
@Composable
fun CommunicationScreen(
    viewModel: AcademiaViewModel,
    messages: List<Message>,
    profile: StudentProfile?
) {
    var filterAnnouncementsOnly by remember { mutableStateOf(false) }
    var chatMessageInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Academy Communications Board",
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Italic,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = DeepNavy
        )
        Text("Department announcements, course reminders, advisor channels.", fontSize = 11.sp, color = SlateGrey)

        Spacer(modifier = Modifier.height(14.dp))

        // Board Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { filterAnnouncementsOnly = false },
                    colors = ButtonDefaults.buttonColors(containerColor = if (!filterAnnouncementsOnly) DeepNavy else LightBackground),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("All Entries", color = if (!filterAnnouncementsOnly) Color.White else DeepNavy, fontSize = 11.sp)
                }

                Button(
                    onClick = { filterAnnouncementsOnly = true },
                    colors = ButtonDefaults.buttonColors(containerColor = if (filterAnnouncementsOnly) DeepNavy else LightBackground),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Announcements", color = if (filterAnnouncementsOnly) Color.White else DeepNavy, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Message Feed
        val displayedMessages = if (filterAnnouncementsOnly) {
            messages.filter { it.isAnnouncement }
        } else {
            messages
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(displayedMessages) { msg ->
                val cardColor = if (msg.isAnnouncement) Crimson.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                val cardBorder = if (msg.isAnnouncement) Crimson.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    border = BorderStroke(1.dp, cardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(DeepNavy, CircleShape)
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(msg.sender.take(1), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(msg.sender, fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = if (msg.senderRole == "Faculty") GoldAccent else SlateGrey) {
                                    Text(msg.senderRole, color = Color.White, fontSize = 8.sp)
                                }
                            }
                            Text(msg.timestamp, fontSize = 10.sp, color = SlateGrey)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(msg.content, style = MaterialTheme.typography.bodyMedium, color = DeepNavy)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dynamic Messenger compose field
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = chatMessageInput,
                onValueChange = { chatMessageInput = it },
                placeholder = { Text("Send board communication...") },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (chatMessageInput.isNotEmpty()) {
                        viewModel.sendMessage(
                            content = chatMessageInput,
                            targetRole = "All",
                            isAnnounce = profile?.role == "Faculty" || profile?.role == "Administration"
                        )
                        chatMessageInput = ""
                    }
                },
                modifier = Modifier
                    .background(DeepNavy, CircleShape)
                    .size(48.dp)
            ) {
                Icon(Icons.Default.Send, "Send Message", tint = Color.White)
            }
        }
    }
}

// TAB 8: AI ADVISOR CHAT SCREEN (GEMINI ASSISTANT)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdvisorChatScreen(
    viewModel: AcademiaViewModel
) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var chatInputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Scroll to bottom on updates
    LaunchedEffect(chatHistory.size, isAiLoading) {
        if (chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(chatHistory.size * 2 - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Chat Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Gemini AI Academic Advisor",
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy
                )
                Text("Prestige university counseling, study planner, essay structuring", fontSize = 11.sp, color = SlateGrey)
            }
            if (chatHistory.isNotEmpty()) {
                IconButton(onClick = { viewModel.clearChat() }) {
                    Icon(Icons.Default.Refresh, "Clear Conversation", tint = Crimson)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sample Prompts row for quick start
        if (chatHistory.isEmpty()) {
            Text("Suggested Q&A Queries:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
            Spacer(modifier = Modifier.height(6.dp))
            val samples = listOf(
                "Optimize my schedule",
                "Explain Quantum decay simply",
                "Draft CS-181 neural network thesis guide"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                samples.forEach { samplePrompt ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(LightBackground, RoundedCornerShape(8.dp))
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(8.dp))
                            .clickable {
                                chatInputText = samplePrompt
                                viewModel.askAcademicAdvisor(samplePrompt)
                                chatInputText = ""
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(samplePrompt, fontSize = 10.sp, color = DeepNavy, maxLines = 2, textAlign = TextAlign.Center)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Conversational Scrollable Pane
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatHistory) { entry ->
                // User Prompt bubble right aligned
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .background(DeepNavy, RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp))
                            .padding(12.dp)
                            .widthIn(max = 280.dp)
                    ) {
                        Text(entry.first, color = Color.White, fontSize = 13.sp)
                    }
                }

                // AI Response bubble left aligned
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp))
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp))
                            .padding(12.dp)
                            .widthIn(max = 280.dp)
                    ) {
                        Text(entry.second, color = DeepNavy, fontSize = 13.sp)
                    }
                }
            }

            if (isAiLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = DeepNavy)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Advisor compiling analysis...", fontSize = 12.sp, color = SlateGrey)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Input Area
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = chatInputText,
                onValueChange = { chatInputText = it },
                placeholder = { Text("Ask your academic advisor...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_chat_input")
            )

            IconButton(
                onClick = {
                    if (chatInputText.isNotEmpty()) {
                        viewModel.askAcademicAdvisor(chatInputText)
                        chatInputText = ""
                    }
                },
                modifier = Modifier
                    .background(DeepNavy, CircleShape)
                    .size(48.dp)
                    .testTag("ai_send_button")
            ) {
                Icon(Icons.Default.Send, "Send query", tint = Color.White)
            }
        }
    }
}
