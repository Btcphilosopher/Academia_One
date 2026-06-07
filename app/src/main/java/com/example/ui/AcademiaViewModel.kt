package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class AcademiaViewModel(private val repository: AcademiaRepository) : ViewModel() {

    // Main persistent database streams
    val studentProfile: StateFlow<StudentProfile?> = repository.studentProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val timetableEvents: StateFlow<List<TimetableEvent>> = repository.timetableEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assignments: StateFlow<List<Assignment>> = repository.assignments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val researchProjects: StateFlow<List<ResearchProject>> = repository.researchProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val libraryBookings: StateFlow<List<LibraryBooking>> = repository.libraryBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val messages: StateFlow<List<Message>> = repository.messages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Chat assistance states
    private val _aiResponse = MutableStateFlow<String>("")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val chatHistory: StateFlow<List<Pair<String, String>>> = _chatHistory.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // 1. Role switcher (updates SQLite state)
    fun switchRole(newRole: String) {
        viewModelScope.launch {
            val current = studentProfile.value ?: StudentProfile(
                id = "UG-2026-8951",
                name = "Alexander Mercer",
                role = "Undergraduate",
                email = "a.mercer@academia.edu",
                department = "Department of Computer Science & Physics",
                gpa = 3.92,
                completedCredits = 120,
                totalCredits = 180
            )

            val updatedGpa = when (newRole) {
                "Undergraduate" -> 3.92
                "Postgraduate" -> 3.98
                else -> 4.0
            }

            val updatedId = when (newRole) {
                "Undergraduate" -> "UG-2026-8951"
                "Postgraduate" -> "PG-2026-1102"
                "Faculty" -> "FAC-3490"
                "Administration" -> "ADM-0021"
                else -> current.id
            }

            val updated = current.copy(
                role = newRole,
                id = updatedId,
                gpa = updatedGpa,
                completedCredits = if (newRole == "Postgraduate") 30 else current.completedCredits,
                totalCredits = if (newRole == "Postgraduate") 60 else current.totalCredits
            )
            repository.updateProfile(updated)

            // Inject role switch notification message
            repository.insertMessage(
                Message(
                    sender = "Security Dashboard",
                    senderRole = "Administration",
                    content = "Role updated to $newRole. Access permissions configured automatically.",
                    timestamp = "Just Now",
                    isAnnouncement = false,
                    targetRole = "All"
                )
            )
        }
    }

    // 2. Timetable Management & Conflict Auto-Resolution
    fun addTimetableEvent(event: TimetableEvent) {
        viewModelScope.launch {
            repository.insertTimetableEvent(event)
            resolveTimetableConflicts()
        }
    }

    fun deleteTimetableEvent(event: TimetableEvent) {
        viewModelScope.launch {
            repository.deleteTimetableEvent(event)
            resolveTimetableConflicts()
        }
    }

    fun resolveTimetableConflicts() {
        viewModelScope.launch {
            // Find clashing timeslots on the same day weekly
            val events = timetableEvents.value
            val matches = mutableListOf<Long>()
            for (i in events.indices) {
                for (j in i + 1 until events.size) {
                    val a = events[i]
                    val b = events[j]
                    if (a.dayOfWeek == b.dayOfWeek) {
                        // Simple overlap check e.g., "10:00"-"11:30" vs "11:00"-"12:30"
                        if (timeOverlaps(a.startTime, a.endTime, b.startTime, b.endTime)) {
                            matches.add(a.id)
                            matches.add(b.id)
                        }
                    }
                }
            }
            // Update conflict flags
            events.forEach { event ->
                val shouldBeConflict = matches.contains(event.id)
                if (event.isConflict != shouldBeConflict) {
                    repository.updateTimetableEvent(event.copy(isConflict = shouldBeConflict))
                }
            }
        }
    }

    private fun timeOverlaps(start1: String, end1: String, start2: String, end2: String): Boolean {
        return try {
            val s1 = convertToMinutes(start1)
            val e1 = convertToMinutes(end1)
            val s2 = convertToMinutes(start2)
            val e2 = convertToMinutes(end2)
            s1 < e2 && s2 < e1
        } catch (e: Exception) {
            false
        }
    }

    private fun convertToMinutes(timeStr: String): Int {
        val parts = timeStr.split(":")
        return parts[0].toInt() * 60 + parts[1].toInt()
    }

    fun autoResolveClashes() {
        viewModelScope.launch {
            val events = timetableEvents.value
            for (i in events.indices) {
                for (j in i + 1 until events.size) {
                    val a = events[i]
                    val b = events[j]
                    if (a.dayOfWeek == b.dayOfWeek && timeOverlaps(a.startTime, a.endTime, b.startTime, b.endTime)) {
                        // Push event b out by moving it 2 hours later, or changing day
                        val newStart = addHoursToTime(b.startTime, 2)
                        val newEnd = addHoursToTime(b.endTime, 2)
                        val resolved = b.copy(
                            startTime = newStart,
                            endTime = newEnd,
                            isConflict = false,
                            description = "${b.description} [Rescheduled by AI Auto-Resolver to clear overlap with ${a.courseCode}]"
                        )
                        repository.updateTimetableEvent(resolved)
                    }
                }
            }
            resolveTimetableConflicts()
        }
    }

    private fun addHoursToTime(timeStr: String, hoursToAdd: Int): String {
        return try {
            val parts = timeStr.split(":")
            var hr = parts[0].toInt() + hoursToAdd
            if (hr >= 20) hr = 8 // wrap to morning
            val min = parts[1]
            val hrStr = if (hr < 10) "0$hr" else "$hr"
            "$hrStr:$min"
        } catch (e: Exception) {
            "16:00"
        }
    }

    // 3. Assignment and Version Draft Submissions
    fun submitAssignmentDraft(assignmentId: Long, draftText: String) {
        viewModelScope.launch {
            val currentList = assignments.value
            val match = currentList.find { it.id == assignmentId }
            if (match != null) {
                // Perform educational/academic compliance analysis (mock Plagiarism scanning + syllabus checklist scoring)
                val scanPlagiarism = Random.nextInt(1, 6) // low plagiarism for model drafts
                val feedback = when {
                    draftText.contains("quantum", ignoreCase = true) -> "Academic Checklist: Bloch Sphere formulas resolved. Plagiarism index low."
                    draftText.contains("consensus", ignoreCase = true) -> "Academic Checklist: CAP Theorem modeling analyzed. Plagiarism index clean."
                    else -> "Academic Checklist: General parameters checked. References format OK."
                }

                val updatedDrafts = match.draftsCount + 1
                val updated = match.copy(
                    draftsCount = updatedDrafts,
                    latestSubmissionText = draftText,
                    status = "Draft Saved",
                    plagiarismScore = scanPlagiarism,
                    complianceFeedback = feedback
                )
                repository.updateAssignment(updated)
            }
        }
    }

    fun submitFinalAssignment(assignmentId: Long) {
        viewModelScope.launch {
            val currentList = assignments.value
            val match = currentList.find { it.id == assignmentId }
            if (match != null) {
                val updated = match.copy(
                    status = "Submitted",
                    plagiarismScore = match.plagiarismScore ?: Random.nextInt(0, 5)
                )
                repository.updateAssignment(updated)

                repository.insertMessage(
                    Message(
                        sender = "LMS Submissions",
                        senderRole = "Administration",
                        content = "Assignment '${match.title}' successfully submitted. Digital fingerprint recorded.",
                        timestamp = "Just Now",
                        isAnnouncement = false,
                        targetRole = "Faculty"
                    )
                )
            }
        }
    }

    fun facultyGradeAssignment(assignmentId: Long, grade: String, feedback: String) {
        viewModelScope.launch {
            val currentList = assignments.value
            val match = currentList.find { it.id == assignmentId }
            if (match != null) {
                val updated = match.copy(
                    status = "Graded",
                    grade = grade,
                    professorComments = feedback
                )
                repository.updateAssignment(updated)

                repository.insertMessage(
                    Message(
                        sender = "Registry and Feedback",
                        senderRole = "Faculty",
                        content = "Grade published: '${match.title}' has been scored at $grade.",
                        timestamp = "Just Now",
                        isAnnouncement = false,
                        targetRole = "Undergraduate"
                    )
                )
            }
        }
    }

    // 4. Research Citations Helper
    fun addResearchProject(title: String, abstractText: String, supervisor: String) {
        viewModelScope.launch {
            val citationAuthor = "Mercer, A."
            val dateStr = "2026"
            val journalStr = "Journal of Advanced Academic Science"

            val apa = "$citationAuthor ($dateStr). $title. $journalStr, 15(2), 204-219."
            val mla = "$citationAuthor. \"$title.\" $journalStr, vol. 15, no. 2, $dateStr, pp. 204-219."
            val chicago = "$citationAuthor. \"$title.\" $journalStr 15, no. 2 ($dateStr): 204-219."

            val project = ResearchProject(
                title = title,
                abstractText = abstractText,
                supervisor = supervisor,
                status = "Proposal Approved",
                citationApa = apa,
                citationMla = mla,
                citationChicago = chicago,
                datasets = "https://datasets.academia.edu/custom-ref",
                supervisorFeedback = "Initial proposal registered. Ready for literary compilation references."
            )
            repository.insertResearchProject(project)
        }
    }

    fun updateResearchProgress(project: ResearchProject, status: String, supervisorFeedback: String? = null) {
        viewModelScope.launch {
            val updated = project.copy(
                status = status,
                supervisorFeedback = supervisorFeedback ?: project.supervisorFeedback
            )
            repository.updateResearchProject(updated)
        }
    }

    // 5. Study Room Bookings
    fun bookLibraryRoom(roomName: String, date: String, slot: String, purpose: String) {
        viewModelScope.launch {
            val booking = LibraryBooking(
                roomName = roomName,
                date = date,
                timeSlot = slot,
                purpose = purpose
            )
            repository.insertLibraryBooking(booking)
        }
    }

    fun cancelLibraryBooking(booking: LibraryBooking) {
        viewModelScope.launch {
            repository.deleteLibraryBooking(booking)
        }
    }

    // 6. Messaging and Updates
    fun sendMessage(content: String, targetRole: String = "All", isAnnounce: Boolean = false) {
        viewModelScope.launch {
            val user = studentProfile.value
            val senderName = user?.name ?: "Faculty User"
            val senderRoleStr = user?.role ?: "Faculty"

            val msg = Message(
                sender = senderName,
                senderRole = senderRoleStr,
                content = content,
                timestamp = "Today, ${getCurrentMinutesString()}",
                isAnnouncement = isAnnounce,
                targetRole = targetRole
            )
            repository.insertMessage(msg)
        }
    }

    private fun getCurrentMinutesString(): String {
        return try {
            val fmt = java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault())
            fmt.format(java.util.Date())
        } catch (e: Exception) {
            "12:00 PM"
        }
    }

    // 7. Academic Guidance Gemini AI Connection
    fun askAcademicAdvisor(prompt: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResponse.value = "Consulting Academic Advisor..."

            val courses = timetableEvents.value.joinToString { "${it.courseCode} (${it.title} with ${it.professor})" }
            val pendings = assignments.value.filter { it.status != "Graded" }.joinToString { "${it.title} (Due: ${it.dueDate})" }
            val user = studentProfile.value

            val context = "Student Name: ${user?.name}, Department: ${user?.department}, Role: ${user?.role}, " +
                    "GPA: ${user?.gpa}. Currently Enrolled Courses: [$courses]. " +
                    "Outstanding Deadlines: [$pendings]."

            val response = repository.askGemini(prompt, context)
            _aiResponse.value = response
            _chatHistory.value = _chatHistory.value + Pair(prompt, response)
            _isAiLoading.value = false
        }
    }

    fun clearChat() {
        _chatHistory.value = emptyList()
        _aiResponse.value = ""
    }
}

class AcademiaViewModelFactory(
    private val repository: AcademiaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcademiaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AcademiaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
