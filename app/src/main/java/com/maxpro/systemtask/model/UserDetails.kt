package com.maxpro.systemtask.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserDetails (
    @SerializedName("badge_counts"              ) var badgeCounts             : BadgeCounts? = BadgeCounts(),
    @SerializedName("account_id"                ) var accountId               : Int?         = null,
    @SerializedName("is_employee"               ) var isEmployee              : Boolean?     = null,
    @SerializedName("last_modified_date"        ) var lastModifiedDate        : Int?         = null,
    @SerializedName("last_access_date"          ) var lastAccessDate          : Int?         = null,
    @SerializedName("reputation_change_year"    ) var reputationChangeYear    : Int?         = null,
    @SerializedName("reputation_change_quarter" ) var reputationChangeQuarter : Int?         = null,
    @SerializedName("reputation_change_month"   ) var reputationChangeMonth   : Int?         = null,
    @SerializedName("reputation_change_week"    ) var reputationChangeWeek    : Int?         = null,
    @SerializedName("reputation_change_day"     ) var reputationChangeDay     : Int?         = null,
    @SerializedName("reputation"                ) var reputation              : Int?         = null,
    @SerializedName("creation_date"             ) var creationDate            : Int?         = null,
    @SerializedName("user_type"                 ) var userType                : String?      = null,
    @SerializedName("user_id"                   ) var userId                  : Int?         = null,
    @SerializedName("accept_rate"               ) var acceptRate              : Int?         = null,
    @SerializedName("location"                  ) var location                : String?      = null,
    @SerializedName("website_url"               ) var websiteUrl              : String?      = null,
    @SerializedName("link"                      ) var link                    : String?      = null,
    @SerializedName("profile_image"             ) var profileImage            : String?      = null,
    @SerializedName("display_name"              ) var displayName             : String?      = null):Parcelable
{
    constructor(parcel: Parcel) : this(
//        TODO("badgeCounts"),
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readString(),
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeList(badgeCounts)
        parcel.writeValue(accountId)
        parcel.writeValue(isEmployee)
        parcel.writeValue(lastModifiedDate)
        parcel.writeValue(lastAccessDate)
        parcel.writeValue(reputationChangeYear)
        parcel.writeValue(reputationChangeQuarter)
        parcel.writeValue(reputationChangeMonth)
        parcel.writeValue(reputationChangeWeek)
        parcel.writeValue(reputationChangeDay)
        parcel.writeValue(reputation)
        parcel.writeValue(creationDate)
        parcel.writeString(userType)
        parcel.writeValue(userId)
        parcel.writeValue(acceptRate)
        parcel.writeString(location)
        parcel.writeString(websiteUrl)
        parcel.writeString(link)
        parcel.writeString(profileImage)
        parcel.writeString(displayName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetails> {
        override fun createFromParcel(parcel: Parcel): UserDetails {
            return UserDetails(parcel)
        }

        override fun newArray(size: Int): Array<UserDetails?> {
            return arrayOfNulls(size)
        }
    }

}