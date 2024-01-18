package com.practicum.playlistmaker.player.domain

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.search.data.model.Track

data class TrackModel(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavourite: Boolean = false,
    var orderAdded: Long,
) : Parcelable {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTime)
        parcel.writeString(artworkUrl100)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
        parcel.writeBoolean(isFavourite)
        parcel.writeLong(orderAdded)
    }

    companion object CREATOR : Parcelable.Creator<TrackModel> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): TrackModel? {
            return try {
                TrackModel(
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readBoolean(),
                    parcel.readLong(),
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun newArray(size: Int): Array<TrackModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getCoverArtwork(): String {
        return artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: ""
    }

    fun trackModelToTrack(): Track {
        return Track(
            trackId = trackId,
            trackName = trackName ?: "",
            artistName = artistName ?: "",
            trackTime = trackTime ?: "",
            artworkUrl100 = artworkUrl100 ?: "",
            collectionName = collectionName ?: "",
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName ?: "",
            country = country ?: "",
            previewUrl = previewUrl ?: "",
            isFavourite = isFavourite,
            orderAdded = orderAdded
        )
    }

}
