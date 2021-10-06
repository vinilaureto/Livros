package com.vinilaureto.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vinilaureto.livros.databinding.ActivityBookBinding
import com.vinilaureto.livros.entities.Book

class BookActivity : AppCompatActivity() {
    private val activityBookBinding: ActivityBookBinding by lazy {
        ActivityBookBinding.inflate(layoutInflater)
    }
    private var position = -1
    private lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityBookBinding.root)

        // Verifica se já tem um livro para edição
        val book = intent.getParcelableExtra<Book>(MainActivity.EXTRA_BOOK)
        position = intent.getIntExtra(MainActivity.EXTRA_POSITION, -1)
        if (book != null ) {
            activityBookBinding.titleEt.setText(book.title)
            activityBookBinding.isbnEt.setText(book.isbn)
            activityBookBinding.authorEt.setText(book.firstAuthor)
            activityBookBinding.publisherEt.setText(book.publisher)
            activityBookBinding.editionEt.setText(book.edition.toString())
            activityBookBinding.pagesEt.setText(book.pages.toString())
            if (position == -1) {
                activityBookBinding.titleEt.isEnabled = false
                activityBookBinding.isbnEt.isEnabled = false
                activityBookBinding.authorEt.isEnabled = false
                activityBookBinding.publisherEt.isEnabled = false
                activityBookBinding.editionEt.isEnabled = false
                activityBookBinding.pagesEt.isEnabled = false
                activityBookBinding.saveBt.visibility = View.GONE
            }
        }
    }

    fun saveClick(view: View) {
        val book = Book(
            activityBookBinding.titleEt.text.toString(),
            activityBookBinding.isbnEt.text.toString(),
            activityBookBinding.authorEt.text.toString(),
            activityBookBinding.publisherEt.text.toString(),
            activityBookBinding.editionEt.text.toString().toInt(),
            activityBookBinding.pagesEt.text.toString().toInt()
        )

        val resultIntent = Intent()
        resultIntent.putExtra(MainActivity.Extras.EXTRA_BOOK, book)

        // se for edição, devolver a posição tambem
        if (position != -1) {
            resultIntent.putExtra(MainActivity.Extras.EXTRA_POSITION, position)
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}