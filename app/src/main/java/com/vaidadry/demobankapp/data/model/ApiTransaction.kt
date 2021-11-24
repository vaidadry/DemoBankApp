package com.vaidadry.demobankapp.data.model

data class ApiTransaction(
    val id: String,
    val counterPartyName: String,
    val counterPartyAccount: String,
    val type: String,
    val amount: String,
    val description: String,
    val date: String
)