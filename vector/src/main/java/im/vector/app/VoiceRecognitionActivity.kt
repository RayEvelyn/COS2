package im.vector.app

import im.vector.app.features.VoiceRecognitionUtil
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.speech.RecognizerIntent

class VoiceRecognitionActivity : AppCompatActivity() {

    private lateinit var voiceRecognitionUtil: VoiceRecognitionUtil
    private lateinit var voiceRecognitionStatus: TextView
    private lateinit var startVoiceRecognitionButton: Button
    private lateinit var stopVoiceRecognitionButton: Button
    private lateinit var micButton: ImageButton

    private lateinit var speechResultLauncher: ActivityResultLauncher<Intent>

    // Shortened TAG for logging
    private val TAG = "VoiceRecognitionAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_recognition)

        // Initialize UI elements
        voiceRecognitionStatus = findViewById(R.id.voice_recognition_status)
        startVoiceRecognitionButton = findViewById(R.id.start_voice_recognition)
        stopVoiceRecognitionButton = findViewById(R.id.stop_voice_recognition)
        micButton = findViewById(R.id.mic_button)

        // Initialize VoiceRecognitionUtil
        voiceRecognitionUtil = VoiceRecognitionUtil(this)

        // Register activity result launcher
        speechResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleSpeechRecognitionResult(result)
        }

        // Set button click listeners
        startVoiceRecognitionButton.setOnClickListener {
            voiceRecognitionUtil.startSpeechRecognition(speechResultLauncher)
            voiceRecognitionStatus.setText(R.string.speech_recognition_listening)
        }

        stopVoiceRecognitionButton.setOnClickListener {
            // Placeholder for stopping speech recognition if needed
            voiceRecognitionStatus.setText(R.string.speech_recognition_stopped)
        }

        micButton.setOnClickListener {
            voiceRecognitionUtil.startSpeechRecognition(speechResultLauncher)
            voiceRecognitionStatus.setText(R.string.speech_recognition_listening)
        }
    }

    private fun handleSpeechRecognitionResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val recognizedText = matches[0]
                processRecognizedText(recognizedText)
            } else {
                showToast(getString(R.string.speech_recognition_error))
            }
        } else {
            showToast(getString(R.string.speech_recognition_error))
        }
    }

    private fun processRecognizedText(spokenText: String) {
        android.util.Log.d(TAG, "Recognized text: $spokenText")

        when {
            spokenText.contains("open", ignoreCase = true) -> openApplication()
            spokenText.contains("search", ignoreCase = true) -> performSearch(spokenText)
            else -> showToast(getString(R.string.command_not_recognized))
        }
    }

    private fun openApplication() {
        showToast(getString(R.string.open_application_command))
    }

    private fun performSearch(query: String) {
        val message = getString(R.string.search_command_recognized, query)
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "VoiceRecognitionAct"
    }
}
