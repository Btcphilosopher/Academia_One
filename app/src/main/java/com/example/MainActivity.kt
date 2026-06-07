package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.GeminiRepository
import com.example.data.AcademiaRepository
import com.example.ui.AcademiaPlatform
import com.example.ui.AcademiaViewModel
import com.example.ui.AcademiaViewModelFactory
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Initialize Room, client repos, and setup structural ViewModels
                val rootScope = CoroutineScope(Dispatchers.IO)
                val database = AppDatabase.getDatabase(applicationContext, rootScope)
                val geminiRepo = GeminiRepository()
                val repository = AcademiaRepository(database.academiaDao(), geminiRepo)
                val factory = AcademiaViewModelFactory(repository)
                val vm: AcademiaViewModel = viewModel(factory = factory)

                // Render the complete unified Student OS system
                AcademiaPlatform(viewModel = vm)
            }
        }
    }
}
