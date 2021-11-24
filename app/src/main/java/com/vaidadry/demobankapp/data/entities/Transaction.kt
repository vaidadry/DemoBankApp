package com.vaidadry.demobankapp.data.entities

import com.vaidadry.demobankapp.enums.TransactionType
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money

open class Transaction(
    @PrimaryKey
    var id: String = "",
    var counterPartyName: String = "",
    var counterPartyAccount: String = "",
    var type: String = "",
    var amount: String = "",
    var description: String = "",
    var date: Date? = null
) : RealmObject() {

    fun getMoney(): Money = Money.of(EUR, amount.toBigDecimal())

    fun isCredit() = type == TransactionType.CREDIT.value
}