package ru.pazderin.bindata.classes

import com.google.gson.annotations.SerializedName


data class Wrapper (

    var number  : Number?  = Number(),
    var scheme  : String?  = null,
    var type    : String?  = null,
    var brand   : String?  = null,
    var prepaid : Boolean? = null,
    var country : Country? = Country(),
    var bank    : Bank?    = Bank()

)