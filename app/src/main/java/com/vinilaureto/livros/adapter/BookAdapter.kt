package com.vinilaureto.livros.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vinilaureto.livros.R
import com.vinilaureto.livros.databinding.LayoutBookBinding
import com.vinilaureto.livros.entities.Book

class BookAdapter(
    val localContext: Context,
    val layout: Int,
    val bookList: MutableList<Book>
    ): ArrayAdapter<Book>(localContext, layout, bookList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val bookLayoutView: View
        if (convertView != null) {
            // célula reciclada
            bookLayoutView = convertView

        } else {
            // inflar uma célula nova
            val layoutBookBinding = LayoutBookBinding.inflate(
                localContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            )
            with(layoutBookBinding) {
                root.tag = BookLayoutHolder(titleTv, authorTv, publisherTv)
                bookLayoutView = layoutBookBinding.root
            }
        }

        // alterar os dados da celular que foi resultado
        val book = bookList[position]

        val bookLayoutHolder = bookLayoutView.tag as BookLayoutHolder
        with(bookLayoutHolder) {
            titleTv.text = book.title
            authorTv.text = book.firstAuthor
            publisherTv.text = book.publisher
        }

        return bookLayoutView
    }

    private data class BookLayoutHolder(
        val titleTv: TextView,
        val authorTv: TextView,
        val publisherTv: TextView
    )
}