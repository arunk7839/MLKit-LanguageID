package com.c1ctech.mlkit_languageid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier

class MainActivity : AppCompatActivity() {
    private lateinit var edtInput: EditText
    private lateinit var tvLanguage: TextView
    private lateinit var tvInput: TextView
    private lateinit var languageIdentifier: LanguageIdentifier

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        languageIdentifier = LanguageIdentification.getClient()
        lifecycle.addObserver(languageIdentifier)
        edtInput = findViewById(R.id.edt_input)

        tvInput = findViewById(R.id.tv_input_text)
        tvLanguage = findViewById(R.id.tv_output_language)

        findViewById<Button>(R.id.btn_identify_language).setOnClickListener { _ ->
            val input = edtInput.text.toString()

            if (!input.isEmpty()) {
                identifyLanguage(input)
            }
        }

        findViewById<Button>(R.id.btn_possible_language).setOnClickListener { _ ->
            val input = edtInput.text.toString()

            if (!input.isEmpty()) {
                identifyPossibleLanguages(input)
            }
        }

    }

    // Identify a language.
    private fun identifyLanguage(inputText: String) {
        tvLanguage.text = "Waiting…"
        languageIdentifier
            .identifyLanguage(inputText)
            .addOnSuccessListener { identifiedLanguage ->
                tvInput.text = "Input Text: " + inputText
                tvLanguage.text = "Identified language(s): " + identifiedLanguage
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Language identification error", e)
                tvInput.text = "Input Text: " + inputText
                tvLanguage.text = ""
                Toast.makeText(
                    this,
                    "Failed to identify language" +
                            "\nError: " +
                            e.getLocalizedMessage() +
                            "\nCause: " +
                            e.cause,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
    }


    // Identify all possible languages.
    private fun identifyPossibleLanguages(inputText: String) {
        tvLanguage.text = "Waiting…"
        languageIdentifier
            .identifyPossibleLanguages(inputText)
            .addOnSuccessListener { identifiedLanguages ->
                tvInput.text = "Input Text: " + inputText

                var output = ""
                for (identifiedLanguage in identifiedLanguages) {
                    output += identifiedLanguage.languageTag + " (" + identifiedLanguage.confidence + "), "
                }
                tvLanguage.text =
                    "Identified language (s):" + output.substring(0, output.length - 2)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Language identification error", e)
                tvInput.text = "Input Text: " + inputText
                tvLanguage.text = ""
                Toast.makeText(
                    this,
                    "Failed to identify language" +
                            "\nError: " +
                            e.getLocalizedMessage() +
                            "\nCause: " +
                            e.cause,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
    }

}
