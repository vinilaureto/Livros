package com.vinilaureto.livros.entities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.sql.SQLException

class BookDB (context: Context): BookDAO {
    companion object {
        private val BD_BOOKS = "books"
        private val TABLE_BOOKS = "books"
        private val COL_TITLE = "title"
        private val COL_ISBN = "isbn"
        private val COL_AUTHOR = "author"
        private val COL_PUBLISHER = "publisher"
        private val COL_EDITION = "edition"
        private val COL_PAGES = "pages"

        private val CREATE_BOOK_TABLE_STM = "CREATE TABLE IF NOT EXISTS ${TABLE_BOOKS} (" +
                "${COL_TITLE} TEXT NOT NULL PRIMARY KEY, " +
                "${COL_ISBN} TEXT NOT NULL, " +
                "${COL_AUTHOR} TEXT NOT NULL, " +
                "${COL_PUBLISHER} TEXT NOT NULL, " +
                "${COL_EDITION} INTEGER NOT NULL, " +
                "${COL_PAGES} INTEGER NOT NULL );"
    }

    private val bookBD: SQLiteDatabase

    init {
        bookBD = context.openOrCreateDatabase(BD_BOOKS, Context.MODE_PRIVATE, null)
        try {
            bookBD.execSQL(CREATE_BOOK_TABLE_STM)
        } catch (error: SQLException) {
            Log.e("Books", error.toString())
        }
    }

    private fun databaseBookAdapter(book: Book): ContentValues? {
        return ContentValues().also {
            with(it) {
                put(COL_TITLE, book.title)
                put(COL_ISBN, book.isbn)
                put(COL_AUTHOR, book.firstAuthor)
                put(COL_PUBLISHER, book.publisher)
                put(COL_EDITION, book.edition)
                put(COL_PAGES, book.pages)
            }
        }
    }

    private fun bookFromDatabaseAdapter(bookCursor: Cursor): Book {
        return if (bookCursor.moveToFirst()) {
            with(bookCursor) {
                Book(getString(
                    getColumnIndexOrThrow(COL_TITLE)),
                    getString(getColumnIndexOrThrow(COL_ISBN)),
                    getString(getColumnIndexOrThrow(COL_AUTHOR)),
                    getString(getColumnIndexOrThrow(COL_PUBLISHER)),
                    getInt(getColumnIndexOrThrow(COL_EDITION)),
                    getInt(getColumnIndexOrThrow(COL_PAGES))
                )
            }
        } else {
            return Book("", "", "", "", 0, 0)
        }
    }

    override fun create(book: Book): Long {
        return bookBD.insert(TABLE_BOOKS, null, databaseBookAdapter((book)))
    }

    override fun findByTitle(title: String): Book {
        val bookQuery = bookBD.query(true, TABLE_BOOKS, null, "${COL_TITLE} = ?", arrayOf(title), null, null, null, null)
        return bookFromDatabaseAdapter(bookQuery)
    }

    override fun findAll(): MutableList<Book> {
        val bookList: MutableList<Book> = mutableListOf()
        val booksQuery = bookBD.query(true, TABLE_BOOKS, null, null, null, null, null, null, null)

        while (booksQuery.moveToNext()) {
            with(booksQuery) {
                bookList.add(Book(getString(getColumnIndexOrThrow(COL_TITLE)),
                    getString(getColumnIndexOrThrow(COL_ISBN)),
                    getString(getColumnIndexOrThrow(COL_AUTHOR)),
                    getString(getColumnIndexOrThrow(COL_PUBLISHER)),
                    getInt(getColumnIndexOrThrow(COL_EDITION)),
                    getInt(getColumnIndexOrThrow(COL_PAGES))
                ))
            }
        }
        return bookList
    }

    override fun update(book: Book): Int {
        val convertedBook = databaseBookAdapter(book)
        return bookBD.update(TABLE_BOOKS, convertedBook, "${COL_TITLE} = ?", arrayOf(book.title))
    }

    override fun remove(title: String): Int {
        return bookBD.delete(TABLE_BOOKS, "${COL_TITLE} = ?", arrayOf(title))
    }
}