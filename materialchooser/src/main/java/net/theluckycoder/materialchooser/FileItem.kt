package net.theluckycoder.materialchooser

internal class FileItem(val name: String,
                        val path: String,
                        val isFolder: Boolean,
                        val isParent: Boolean = false)
    : Comparable<FileItem> {

    override fun compareTo(other: FileItem): Int = name.toLowerCase().compareTo(other.name.toLowerCase())
}
