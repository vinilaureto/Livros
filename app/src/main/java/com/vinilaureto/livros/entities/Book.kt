package com.vinilaureto.livros.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book (
    val title: String,
    val isbn: String,
    val firstAuthor: String,
    val publisher: String,
    val edition: Int,
    val pages: Int
) : Parcelable