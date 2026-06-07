package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
interface AcademiaDao {
    @Query("SELECT * FROM student_profiles LIMIT 1")
    fun getStudentProfile(): Flow<StudentProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentProfile(profile: StudentProfile)

    @Update
    suspend fun updateStudentProfile(profile: StudentProfile)

    @Query("SELECT * FROM timetable_events ORDER BY id ASC")
    fun getAllTimetableEvents(): Flow<List<TimetableEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableEvent(event: TimetableEvent)

    @Update
    suspend fun updateTimetableEvent(event: TimetableEvent)

    @Delete
    suspend fun deleteTimetableEvent(event: TimetableEvent)

    @Query("DELETE FROM timetable_events")
    suspend fun clearTimetableEvents()

    @Query("SELECT * FROM assignments ORDER BY dueDate ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    @Query("SELECT * FROM research_projects")
    fun getAllResearchProjects(): Flow<List<ResearchProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResearchProject(project: ResearchProject)

    @Update
    suspend fun updateResearchProject(project: ResearchProject)

    @Query("SELECT * FROM library_bookings")
    fun getAllLibraryBookings(): Flow<List<LibraryBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibraryBooking(booking: LibraryBooking)

    @Delete
    suspend fun deleteLibraryBooking(booking: LibraryBooking)

    @Query("SELECT * FROM messages ORDER BY id DESC")
    fun getAllMessages(): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
}

@Database(
    entities = [
        StudentProfile::class,
        TimetableEvent::class,
        Assignment::class,
        ResearchProject::class,
        LibraryBooking::class,
        Message::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun academiaDao(): AcademiaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "academia_one_database"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.academiaDao())
                }
            }
        }

        suspend fun populateDatabase(dao: AcademiaDao) {
            // Seed student profile
            dao.insertStudentProfile(
                StudentProfile(
                    id = "UG-2026-8951",
                    name = "Alexander Mercer",
                    role = "Undergraduate",
                    email = "a.mercer@academia.edu",
                    department = "Department of Computer Science & Physics",
                    gpa = 3.92,
                    completedCredits = 120,
                    totalCredits = 180
                )
            )

            // Seed Timetable Events
            dao.insertTimetableEvent(
                TimetableEvent(
                    title = "Intro to Quantum Mechanics",
                    courseCode = "PHYS-250",
                    type = "Lecture",
                    dayOfWeek = "Mon",
                    startTime = "10:00",
                    endTime = "11:30",
                    location = "Lyman Labs - Sanders Hall",
                    professor = "Prof. Sarah Jenkins",
                    description = "Foundational quantum models, wave equations, and operators."
                )
            )
            dao.insertTimetableEvent(
                TimetableEvent(
                    title = "Machine Learning Systems",
                    courseCode = "CS-181",
                    type = "Lecture",
                    dayOfWeek = "Tue",
                    startTime = "13:00",
                    endTime = "14:30",
                    location = "Maxwell Auditorium",
                    professor = "Prof. Ryan Adams",
                    description = "Deep network structures, optimization heuristics, and validation."
                )
            )
            dao.insertTimetableEvent(
                TimetableEvent(
                    title = "Advanced Software Engineering",
                    courseCode = "CS-261",
                    type = "Lecture",
                    dayOfWeek = "Wed",
                    startTime = "09:00",
                    endTime = "10:30",
                    location = "Lovelace Lecture Hall B",
                    professor = "Prof. Diana Sterling",
                    description = "Structural design, microservice orchestration, and consensus systems."
                )
            )
            dao.insertTimetableEvent(
                TimetableEvent(
                    title = "Quantum Computation Lab",
                    courseCode = "PHYS-250L",
                    type = "Lab",
                    dayOfWeek = "Thu",
                    startTime = "14:00",
                    endTime = "16:00",
                    location = "Science Yard Room 301",
                    professor = "Dr. Emily Vance",
                    description = "Qiskit simulations and real-time state teleportation tests."
                )
            )
            dao.insertTimetableEvent(
                TimetableEvent(
                    title = "Distributed Systems Seminar",
                    courseCode = "CS-224",
                    type = "Seminar",
                    dayOfWeek = "Fri",
                    startTime = "15:00",
                    endTime = "16:30",
                    location = "Lovelace Wing Room 403",
                    professor = "Prof. Leslie Lamport",
                    description = "Reviewing Paxos vs Raft consensus papers."
                )
            )

            // Seed assignments
            dao.insertAssignment(
                Assignment(
                    title = "Problem Set 2: Backprop & Optimization",
                    courseCode = "CS-181",
                    dueDate = "2026-06-15",
                    status = "Pending",
                    plagiarismScore = null,
                    professorComments = null
                )
            )
            dao.insertAssignment(
                Assignment(
                    title = "Consensus Protocol Draft Thesis",
                    courseCode = "CS-261",
                    dueDate = "2026-06-12",
                    status = "Draft Saved",
                    draftsCount = 2,
                    latestSubmissionText = "This paper introduces a serverless consensus protocol modeled after Lamport timestamps...",
                    complianceFeedback = "Meets draft formatting rubrics. Plagiarism check pending.",
                    plagiarismScore = 3,
                    professorComments = "Strong start. Ensure you detail partition tolerance in Chapter 3."
                )
            )
            dao.insertAssignment(
                Assignment(
                    title = "Quantum State Decay Simulation",
                    courseCode = "PHYS-250",
                    dueDate = "2026-06-02",
                    status = "Graded",
                    grade = "96%",
                    draftsCount = 1,
                    latestSubmissionText = "Simulating Bloch sphere projections with active state decay operators...",
                    complianceFeedback = "Plagiarism score within clean limits (0%). Rubric standard fully met.",
                    plagiarismScore = 0,
                    professorComments = "Brilliant mathematical formulation! Code correctness was perfect."
                )
            )

            // Seed Research Projects
            dao.insertResearchProject(
                ResearchProject(
                    title = "Zero-Emission High-Performance Campus Computing Framework",
                    abstractText = "This research models server thermal dissipation models and dynamic scheduling strategies to reduce carbon footprints in high-density multi-tenant institutional computing clusters.",
                    supervisor = "Prof. Marcus Roberts",
                    status = "Proposal Approved",
                    citationApa = "Mercer, A. (2026). Zero-Emission High-Performance Campus Computing Framework. Journal of Academic Green Systems, 12(3), 145-162.",
                    citationMla = "Mercer, Alexander. \"Zero-Emission High-Performance Campus Computing Framework.\" Journal of Academic Green Systems, vol. 12, no. 3, 2026, pp. 145-162.",
                    citationChicago = "Mercer, Alexander. \"Zero-Emission High-Performance Campus Computing Framework.\" Journal of Academic Green Systems 12, no. 3 (2026): 145-162.",
                    datasets = "https://datasets.academia.edu/green-cluster-data; https://github.com/academia/greencomputemodule",
                    supervisorFeedback = "The theoretical modeling of thermal gradients is robust. Let's move to real physical metrics in Lyman Labs next."
                )
            )

            // Seed library booking
            dao.insertLibraryBooking(
                LibraryBooking(
                    roomName = "Widener Library - Study Room 402",
                    date = "2026-06-08",
                    timeSlot = "14:00 - 16:00",
                    purpose = "Collaborative CS-181 Group Review"
                )
            )

            // Seed messages / announcements
            dao.insertMessage(
                Message(
                    sender = "Office of Academic Registry",
                    senderRole = "Administration",
                    content = "Undergraduate and Postgraduate enrollment for Summer '26 electives is now open. The deadline to declare courses is June 15th.",
                    timestamp = "10 mins ago",
                    isAnnouncement = true,
                    targetRole = "All"
                )
            )
            dao.insertMessage(
                Message(
                    sender = "Prof. Ryan Adams",
                    senderRole = "Faculty",
                    content = "The final grading scheme for Problem Set 2 has been updated in the LMS tab under Rubrics. Please check the requirements before submitting.",
                    timestamp = "Today, 8:15 AM",
                    isAnnouncement = false,
                    targetRole = "All"
                )
            )
            dao.insertMessage(
                Message(
                    sender = "Office of International Support",
                    senderRole = "Administration",
                    content = "Important notice regarding summer visa extensions for international students: workshop scheduled in Student Union Annex tomorrow at 2 PM.",
                    timestamp = "Yesterday",
                    isAnnouncement = true,
                    targetRole = "All"
                )
            )
        }
    }
}
