package net.theluckycoder.materialchooser

import android.os.Parcel
import android.os.Parcelable

internal class ChooserParams(
    val rootPath: String,
    val startPath: String?,
    val fileExtensions: List<String>?,
    val showHiddenFiles: Boolean,
    val useNightTheme: Boolean,
    val isFileChooser: Boolean
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rootPath)
        parcel.writeString(startPath)
        parcel.writeStringList(fileExtensions)
        parcel.writeByte(if (showHiddenFiles) 1 else 0)
        parcel.writeByte(if (useNightTheme) 1 else 0)
        parcel.writeByte(if (isFileChooser) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ChooserParams> {
        override fun createFromParcel(parcel: Parcel): ChooserParams {
            return ChooserParams(parcel)
        }

        override fun newArray(size: Int): Array<ChooserParams?> {
            return arrayOfNulls(size)
        }
    }
}