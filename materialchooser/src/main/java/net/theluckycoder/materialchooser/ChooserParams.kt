package net.theluckycoder.materialchooser

import android.os.Parcel
import android.os.Parcelable

internal class ChooserParams(
    val rootPath: String,
    val startPath: String?,
    val fileExtensions: List<String>?,
    val showHiddenFiles: Boolean,
    val nightMode: Int,
    val isFileChooser: Boolean
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeString(rootPath)
            writeString(startPath)
            writeStringList(fileExtensions)
            writeByte(if (showHiddenFiles) 1 else 0)
            writeInt(nightMode)
            writeByte(if (isFileChooser) 1 else 0)
        }
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
