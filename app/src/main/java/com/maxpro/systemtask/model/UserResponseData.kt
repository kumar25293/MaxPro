package com.maxpro.systemtask.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponseData(

    @SerializedName("items"           ) var data          : ArrayList<UserDetails> = arrayListOf(),
    @SerializedName("has_more"        ) var hasMore        : Boolean?         = null,
    @SerializedName("quota_max"       ) var quotaMax       : Int?             = null,
    @SerializedName("quota_remaining" ) var quotaRemaining : Int?             = null
): Parcelable {
    constructor(parcel: Parcel) : this(

//        parcel.(UserDetails::class.java.classLoader) as ArrayList<UserDetails>,
//        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(hasMore)
        parcel.writeValue(quotaMax)
        parcel.writeValue(quotaRemaining)
        parcel.writeList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserResponseData> {
        override fun createFromParcel(parcel: Parcel): UserResponseData {
            return UserResponseData(parcel)
        }

        override fun newArray(size: Int): Array<UserResponseData?> {
            return arrayOfNulls(size)
        }
    }
}
