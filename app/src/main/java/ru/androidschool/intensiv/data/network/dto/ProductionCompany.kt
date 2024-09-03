package ru.androidschool.intensiv.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("original_country")
    val originCountry: String?
)
