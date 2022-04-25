package com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS

@Suppress("UNUSED_VARIABLE")
object DetectText {

    @Throws(Exception::class)
    fun detectText(context: Context, uri: Uri) {
        val img = InputImage.fromFilePath(context, uri)

        val recognizer = TextRecognition.getClient(DEFAULT_OPTIONS)
        val result = recognizer.process(img).addOnSuccessListener {
            val text = StringBuilder()
            it.textBlocks.forEach { textBlock ->
                val blockText = textBlock.text
                val cornerPoint = textBlock.cornerPoints
                val frame = textBlock.boundingBox
                textBlock.lines.forEach { line ->
                    val lineText = line.text
                    val lineCornerPoint = line.cornerPoints
                    val lineRect = line.boundingBox
                    line.elements.forEach { element ->
                        val textElement = element.text
                        text.append(textElement)
                    }
                    text.append(blockText)
                }
            }
            Toast.makeText(context, text.toString(), Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show()
        }
    }
}