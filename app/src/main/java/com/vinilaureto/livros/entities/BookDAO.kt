package com.vinilaureto.livros.entities

interface  BookDAO {
    fun create(book: Book): Long
    fun findByTitle(title: String): Book
    fun findAll(): MutableList<Book>
    fun update(book: Book): Int
    fun remove(title: String): Int
}