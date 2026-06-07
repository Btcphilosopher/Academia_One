package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profiles")
data class StudentProfile(
    @PrimaryKey val id: String, // e.g., "UG-2026-8951"
    val name: String,
    val role: String, // "Undergraduate", "Postgraduate", "Faculty", "Administration"
    val email: String,
    val department: String,
    val gpa: Double,
    val completedCredits: Int,
    val totalCredits: Int,
    val profilePhotoUri: String? = null
)

@Entity(tableName = "timetable_events")
data class TimetableEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val courseCode: String,
    val type: String, // "Lecture", "Lab", "Seminar", "Exam"
    val dayOfWeek: String, // "Mon", "Tue", "Wed", "Thu", "Fri"
    val startTime: String, // "09:00"
    val endTime: String, // "10:30"
    val location: String, // e.g., "Lovelace Auditorium", "Maxwell Lab"
    val professor: String,
    val description: String,
    val isConflict: Boolean = false
)

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val courseCode: String,
    val dueDate: String, // "2026-06-15"
    val status: String, // "Pending", "Draft Saved", "Submitted", "Graded"
    val grade: String? = null, // e.g., "A", "94%"
    val draftsCount: Int = 0,
    val latestSubmissionText: String? = null,
    val complianceFeedback: String? = null, // Mock plagiarism or rubric analyzer
    val plagiarismScore: Int? = null, // percentage e.g. 4%
    val professorComments: String? = null
)

@Entity(tableName = "research_projects")
data class ResearchProject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val abstractText: String,
    val supervisor: String,
    val status: String, // "Proposal Approved", "Literature Review", "Data Collection", "Under Review"
    val citationApa: String,
    val citationMla: String,
    val citationChicago: String,
    val datasets: String, // comma or semicolon separated
    val supervisorFeedback: String? = null
)

@Entity(tableName = "library_bookings")
data class LibraryBooking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomName: String,
    val date: String, // "2026-06-08"
    val timeSlot: String, // "14:00 - 16:00"
    val purpose: String
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String,
    val senderRole: String,
    val content: String,
    val timestamp: String, // "10:15 AM"
    val isAnnouncement: Boolean = false,
    val targetRole: String? = null // e.g. "All", "Faculty", "Postgraduate"
)
