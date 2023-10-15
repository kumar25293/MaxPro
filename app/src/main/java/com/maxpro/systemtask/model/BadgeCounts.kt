package com.maxpro.systemtask.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BadgeCounts (
    @SerializedName("bronze" ) var bronze : Int? = null,
    @SerializedName("silver" ) var silver : Int? = null,
    @SerializedName("gold"   ) var gold   : Int? = null)
{
}