package ru.skillbranch.devintensive.models

import android.util.Log

class Bender( var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERAIL -> Question.SERAIL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val validationText = question.validate(answer)
        return when {
            validationText != null -> validationText to status.color
            question.ansvers.contains(answer.toLowerCase()) -> {
                question = question.nextQuestion()
                "Отлично - это правильный ответ!\n${question.question}" to status.color
            }
            else -> {
                status = status.nextStatus()
                "Это не правильный ответ!\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 255, 0));

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
            override fun validate(answer: String): String? {
                if (answer.isNotEmpty() and !answer[0].isUpperCase()) {
                    return "Имя должно начинаться с заглавной буквы"
                }
                return null
            }

            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("bender", "сгибальщик")) {
            override fun validate(answer: String): String? {
                if (answer.isNotEmpty() and !answer[0].isLowerCase()) {
                    return "Профессия должна начинаться со строчной буквы"
                }
                return null
            }

            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан", listOf("метал", "дерево", "metal", "iron", "wood")) {
            override fun validate(answer: String): String? {
                Log.d("M_Bender", "${Regex("\\d+").findAll(answer)}")
                if (Regex("\\d+").containsMatchIn(answer)) {
                    return "Материал не должен содержать цифр"
                }
                return null
            }
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validate(answer: String): String? {
                if (Regex("\\D+").containsMatchIn(answer)) {
                    return "Год моего рождения должен содержать только цифры"
                }
                return null
            }

            override fun nextQuestion(): Question = SERAIL
        },
        SERAIL("Мой серийный номер?", listOf("2716057")) {
            override fun validate(answer: String): String? {
                if ((answer.length != 7) and Regex("\\D+").containsMatchIn(answer)) {
                    return "Серийный номер содержит только цифры, и их 7"
                }
                return null            }

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validate(answer: String): String? = "Отлично - ты справился\nНа этом все, вопросов больше нет"

            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question

        abstract fun validate(answer: String): String?
    }
}