package im.vector.app.features

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class VoiceRecognitionUtil(private val context: Context) {

    init {
        if (context !is AppCompatActivity) {
            throw IllegalArgumentException("Context must be an instance of AppCompatActivity")
        }
    }

    fun startSpeechRecognition(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...")
        }

        try {
            resultLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Speech recognition is not supported on this device.", Toast.LENGTH_SHORT).show()
        }
    }
}