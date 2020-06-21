package kz.education.stephomework5

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*


class MainActivity : AppCompatActivity() {

    val PERMISSION_CODE = 1001;
    var buttonRecord : Button? = null;
    var textViewRecordResult: TextView? = null;
    var mSpeechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    var mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also { intent ->
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "kz.education.stephomework5")
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        initializeListeners()
    }

    fun initializeViews(){
        buttonRecord = findViewById(R.id.activity_main_edit_text_record)
        textViewRecordResult = findViewById(R.id.activity_main_text_view_record_result)
    }

    fun initializeListeners(){
        setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

            override fun onBeginningOfSpeech() {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {
                println(p0)
            }

            override fun onResults(results: Bundle?) {
                val matches: ArrayList<String>? = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null) textViewRecordResult?.setText(matches[0])
            }
        })
        buttonRecord?.setOnTouchListener(object : View.OnTouchListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_UP -> {
                            mSpeechRecognizer.stopListening();
                        }
                        MotionEvent.ACTION_DOWN -> {
                                if (initiateCheckPermissionRecordAudio()) {
                                    textViewRecordResult?.setText("")
                                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                                } else {
                                    initiateRequestPermissionsRecordAudio()
                                }
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initiateRequestPermissionsRecordAudio(){
        requestPermissions(Array(1){android.Manifest.permission.RECORD_AUDIO},PERMISSION_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initiateCheckPermissionRecordAudio() :Boolean{
        return  checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE->{
                if(grantResults.isNotEmpty()){
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                }
            }
        }
    }

    fun setRecognitionListener(recognitionListener: RecognitionListener) {
        mSpeechRecognizer.setRecognitionListener(recognitionListener)
    }
}