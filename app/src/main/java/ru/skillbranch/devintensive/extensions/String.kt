package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String {
    val cropped = this.trimEnd().take(count)
    return if (cropped.length == count) "${cropped.trimEnd()}..." else cropped
}

fun String.stripHtml(): String {
    var text = Regex("</?[^>]*>").replace(this, "")
    text = Regex("(\\s|&nbsp;)+").replace(text, " ")
    text = Regex("(&[\\s\\d]+;)+").replace(text, "")
    return text
}
