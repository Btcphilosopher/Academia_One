package com.example.data

import kotlinx.coroutines.flow.Flow

class AcademiaRepository(
    private val dao: AcademiaDao,
    private val geminiRepo: GeminiRepository
) {
    val studentProfile: Flow<StudentProfile?> = dao.getStudentProfile()
    val timetableEvents: Flow<List<TimetableEvent>> = dao.getAllTimetableEvents()
    val assignments: Flow<List<Assignment>> = dao.getAllAssignments()
    val researchProjects: Flow<List<ResearchProject>> = dao.getAllResearchProjects()
    val libraryBookings: Flow<List<LibraryBooking>> = dao.getAllLibraryBookings()
    val messages: Flow<List<Message>> = dao.getAllMessages()

    suspend fun updateProfile(profile: StudentProfile) {
        dao.insertStudentProfile(profile)
    }

    suspend fun insertTimetableEvent(event: TimetableEvent) {
        dao.insertTimetableEvent(event)
    }

    suspend fun updateTimetableEvent(event: TimetableEvent) {
        dao.updateTimetableEvent(event)
    }

    suspend fun deleteTimetableEvent(event: TimetableEvent) {
        dao.deleteTimetableEvent(event)
    }

    suspend fun insertAssignment(assignment: Assignment) {
        dao.insertAssignment(assignment)
    }

    suspend fun updateAssignment(assignment: Assignment) {
        dao.updateAssignment(assignment)
    }

    suspend fun deleteAssignment(assignment: Assignment) {
        dao.deleteAssignment(assignment)
    }

    suspend fun insertResearchProject(project: ResearchProject) {
        dao.insertResearchProject(project)
    }

    suspend fun updateResearchProject(project: ResearchProject) {
        dao.updateResearchProject(project)
    }

    suspend fun insertLibraryBooking(booking: LibraryBooking) {
        dao.insertLibraryBooking(booking)
    }

    suspend fun deleteLibraryBooking(booking: LibraryBooking) {
        dao.deleteLibraryBooking(booking)
    }

    suspend fun insertMessage(message: Message) {
        dao.insertMessage(message)
    }

    suspend fun askGemini(prompt: String, studentContext: String): String {
        return geminiRepo.getAcademicFeedback(prompt, studentContext)
    }
}
