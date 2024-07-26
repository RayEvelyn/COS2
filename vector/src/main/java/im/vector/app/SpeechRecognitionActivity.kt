package im.vector.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import im.vector.app.features.VoiceRecognitionUtil
import java.util.*

class SpeechRecognitionActivity : AppCompatActivity() {

    private lateinit var speechRecognitionPrompt: TextView
    private lateinit var voiceRecognitionUtil: VoiceRecognitionUtil
    private lateinit var startButton: Button
    private val speechResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleSpeechRecognitionResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_recognition) // Set the content view to your layout

        // Initialize the UI elements from the layout
        speechRecognitionPrompt = findViewById(R.id.speech_recognition_prompt)
        startButton = findViewById(R.id.startButton)
        voiceRecognitionUtil = VoiceRecognitionUtil(this)

        // Set button click listener
        startButton.setOnClickListener {
            voiceRecognitionUtil.startSpeechRecognition(speechResultLauncher)
        }
    }

    private fun handleSpeechRecognitionResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val recognizedText = matches[0]
                speechRecognitionPrompt.text = "Recognized: $recognizedText"
            } else {
                speechRecognitionPrompt.text = "Speech recognition failed."
            }
        } else {
            speechRecognitionPrompt.text = "Speech recognition failed."
        }
    }
}
