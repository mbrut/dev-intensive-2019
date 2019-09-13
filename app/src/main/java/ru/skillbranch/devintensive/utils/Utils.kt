package ru.skillbranch.devintensive.utils


object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.trim()?.split(" ")?.filter { it.isNotEmpty() }
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return Pair(firstName, lastName)
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val simbolMap = mapOf(
            "а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d", "е" to "e", "ё" to "e",
            "ж" to "zh", "з" to "z", "и" to "i", "й" to "i", "к" to "k", "л" to "l", "м" to "m",
            "н" to "n", "о" to "o", "п" to "p", "р" to "r", "с" to "s", "т" to "t", "у" to "u",
            "ф" to "f", "х" to "h", "ц" to "c", "ч" to "ch", "ш" to "sh", "щ" to "sh'", "ъ" to "",
            "ы" to "i", "ь" to "", "э" to "e", "ю" to "yu", "я" to "ya"
        )
        var words = payload.split(" ")
        words = words.map {word -> word.map chars@{
            return@chars if (it.isUpperCase()) {
                simbolMap.getOrElse("${it.toLowerCase()}"){ "$it" }.capitalize()
            } else {
                simbolMap.getOrElse("$it"){ "$it" }
            }
        }.joinToString("")}
        return words.joinToString(divider)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val names = listOfNotNull(firstName?.trim(), lastName?.trim()).filter { it.isNotEmpty() }
        return names.map { it.take(1).capitalize() }.joinToString(separator = "") { it }.ifEmpty { null }
    }
}