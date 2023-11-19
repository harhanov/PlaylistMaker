package com.practicum.playlistmaker.media_library.playlists.domain

import android.os.Parcel
import android.os.Parcelable
data class PlaylistModel(
    val playlistId: Long? = null,
    val playlistName: String?,
    val playlistDescription: String?,
    val playlistImagePath: String?,
    var trackIds:String? = "",
    var numberOfTracks: Int = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(playlistId)
        parcel.writeString(playlistName)
        parcel.writeString(playlistDescription)
        parcel.writeString(playlistImagePath)
        parcel.writeString(trackIds)
        parcel.writeInt(numberOfTracks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistModel> {
        override fun createFromParcel(parcel: Parcel): PlaylistModel {
            return PlaylistModel(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistModel?> {
            return arrayOfNulls(size)
        }
    }
}