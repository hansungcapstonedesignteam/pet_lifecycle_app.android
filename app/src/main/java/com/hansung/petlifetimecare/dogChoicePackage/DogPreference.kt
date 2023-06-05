package com.hansung.petlifetimecare.dogChoicePackage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogPreference(
    var id: Int = 0,
    var breed: String = "",
    var activityLevel: String = "",
    var houseSize: String = "",
    var hairLoss: String = "",
    var training: String = "",
    var personality: String = "",
    var walkFrequency: String = "",
    var loneliness: String = "",
    var otherPets: String = "",
    var vetCosts: String = "",
    var ownerPresenceRequired: Boolean = false,
    var suitableForSmallHouses: Boolean = false,
    var yardRequired: Boolean = false
) : Parcelable
