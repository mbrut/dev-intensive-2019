package ru.skillbranch.devintensive.models

import android.util.Log


class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME, private var wrongs: Int = 0) {
    val mistakes get() = this.wrongs

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERAIL -> Question.SERAIL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val validationText = question.validateHint(answer)
        Log.d("M_Bender", "$question")
        return when {
            validationText.isNotEmpty() and (question != Question.IDLE) -> "$validationText\n${question.question}" to status.color
            (question == Question.IDLE) or question.ansvers.contains(answer.toLowerCase()) -> {
                val greeting = if (question == Question.IDLE) "" else "Отлично - ты справился\n"
                question = question.nextQuestion()
                "$greeting${question.question}" to status.color
            }
            else -> {
                wrongs++
                var extraText = ""
                if (wrongs > 3) {
                    extraText = ". Давай все по новой"
                    question = Question.NAME
                    status = Status.NORMAL
                    wrongs = 0
                }
                 else {
                    status = status.nextStatus()
                }
                Log.d("M_Bender", "$wrongs")
                "Это неправильный ответ$extraText\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val ansvers: List<String>) {
        NAME("Как меня зовут?", listOf("bender", "бендер")) {
            override fun validateHint(answer: String): String {
                return if (answer.isBlank() or answer[0].isLowerCase())
                    return "Имя должно начинаться с заглавной буквы"
                else
                    ""
            }

            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("bender", "сгибальщик")) {
            override fun validateHint(answer: String): String {
                return if (answer.isBlank() or answer[0].isUpperCase())
                    "Профессия должна начинаться со строчной буквы"
                else
                    ""
            }

            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("метал", "дерево", "metal", "iron", "wood")) {
            override fun validateHint(answer: String): String {
                return if (Regex("\\d+").containsMatchIn(answer))
                    "Материал не должен содержать цифр"
                else
                    ""
            }
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validateHint(answer: String): String {
                return if (Regex("\\D+").containsMatchIn(answer))
                    "Год моего рождения должен содержать только цифры"
                else
                    ""
            }

            override fun nextQuestion(): Question = SERAIL
        },
        SERAIL("Мой серийный номер?", listOf("2716057")) {
            override fun validateHint(answer: String): String {
                return if ((answer.length != 7) or Regex("\\D+").containsMatchIn(answer))
                    "Серийный номер содержит только цифры, и их 7"
                else
                    ""
            }

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validateHint(answer: String): String = ""

            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question

        abstract fun validateHint(answer: String): String
    }
}