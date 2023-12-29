package com.practicum.playlistmaker.media_library.data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long? = null,
    val playlistName: String?,
    val playlistDescription: String?,
    val playlistImagePath: String?,
    var trackIds: String? = null,
    var numberOfTracks: Int = 0,
) : Parcelable {
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

    companion object CREATOR : Parcelable.Creator<PlaylistEntity> {
        override fun createFromParcel(parcel: Parcel): PlaylistEntity {
            return PlaylistEntity(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistEntity?> {
            return arrayOfNulls(size)
        }
    }

}