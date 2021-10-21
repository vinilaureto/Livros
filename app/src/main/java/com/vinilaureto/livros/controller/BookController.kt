package com.vinilaureto.livros.controller

import com.vinilaureto.livros.MainActivity
import com.vinilaureto.livros.entities.Book
import com.vinilaureto.livros.entities.BookDAO
import com.vinilaureto.livros.entities.BookDB

class BookController(mainActivity: MainActivity) {
    private val bookDAO: BookDAO = BookDB(mainActivity)

    fun newBook(book: Book) = bookDAO.create(book)
    fun findByTitle(title: String) = bookDAO.findByTitle(title)
    fun findAllBooks() = bookDAO.findAll()
    fun updateBook(book: Book) = bookDAO.update(book)
    fun deleteBookByTitle(title: String) = bookDAO.remove(title)
}