package net.theluckycoder.filechooser

internal class Option(val name: String, val path: String, val isFolder: Boolean) : Comparable<Option> {

    override fun compareTo(other: Option): Int {
        return name.toLowerCase().compareTo(name.toLowerCase())
    }
}
