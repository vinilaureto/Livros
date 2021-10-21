package com.vinilaureto.livros

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.vinilaureto.livros.adapter.BookAdapter
import com.vinilaureto.livros.controller.BookController
import com.vinilaureto.livros.databinding.ActivityMainBinding
import com.vinilaureto.livros.entities.Book

class MainActivity : AppCompatActivity() {
    companion object Extras {
        const val EXTRA_BOOK = "EXTRA_BOOK"
        const val EXTRA_POSITION = "EXTRA_POSITION"
    }
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var bookActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editBookActivityResultLauncher: ActivityResultLauncher<Intent>

    private val bookController: BookController by lazy {
        BookController(this)
    }

    // DataSource
    private var bookList: MutableList<Book> = mutableListOf();

    private fun prepareBookList() {
        bookList = bookController.findAllBooks()
    }

    // Adapter
//    private val booksAdapter: ArrayAdapter<String> by lazy {
//        ArrayAdapter(this, android.R.layout.simple_list_item_1, bookList.run {
//            val booksStringList = mutableListOf<String>()
//            this.forEach { book -> booksStringList.add(book.toString()) }
//            booksStringList
//        })
//    }

    private val booksAdapter: BookAdapter by lazy {
        BookAdapter(this, R.layout.layout_book, bookList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        prepareBookList()
        activityMainBinding.booksLv.adapter = booksAdapter

        registerForContextMenu(activityMainBinding.booksLv)

        bookActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val book = result.data?.getParcelableExtra<Book>(EXTRA_BOOK)
                if (book != null) {
                    bookList.add(book)
                    booksAdapter.notifyDataSetChanged()
                    bookController.newBook(book)
                    //booksAdapter.add(book)
                }
            }
        }

        editBookActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val position = result.data?.getIntExtra(EXTRA_POSITION, -1)
                result.data?.getParcelableExtra<Book>(EXTRA_BOOK)?.apply {
                    if (position != null && position != -1) {
                        bookList[position] = this
                        booksAdapter.notifyDataSetChanged()
                        bookController.updateBook(this)
                    }
                }
            }
        }

        activityMainBinding.booksLv.setOnItemClickListener{ _, _, position, _ ->
            val book = bookList[position]
            val consultBookIntent = Intent(this, BookActivity::class.java)
            consultBookIntent.putExtra(EXTRA_BOOK, book)
            startActivity(consultBookIntent)
        }

        activityMainBinding.addBookFab.setOnClickListener {
            bookActivityResultLauncher.launch(Intent(this, BookActivity::class.java))
        }
    }



//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.addBookMi) {
//            bookActivityResultLauncher.launch(Intent(this, BookActivity::class.java))
//        }
//        return false
//    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val book = bookList[position]

        return when(item.itemId) {
            R.id.editItemMi -> {
                val book = bookList[position]
                val editBookIntent = Intent(this, BookActivity::class.java)
                editBookIntent.putExtra(EXTRA_BOOK, book)
                editBookIntent.putExtra(EXTRA_POSITION, position)
                //startActivity(editBookIntent)
                editBookActivityResultLauncher.launch(editBookIntent)

                true
            }
            R.id.removeItemMi -> {
                with(AlertDialog.Builder(this)) {
                    setMessage("Apagar ${book.title}?")
                    setPositiveButton("Sim") { _, _ ->
                        bookList.removeAt(position)
                        booksAdapter.notifyDataSetChanged()
                        bookController.deleteBookByTitle(book.title)
                        Snackbar.make(activityMainBinding.root, "Removido com sucesso", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityMainBinding.root, "Operação cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()
                true
            }
            else -> { false }
        }


    }
}