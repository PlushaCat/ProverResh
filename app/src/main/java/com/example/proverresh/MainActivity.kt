package com.example.proverresh

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

    class MainActivity : AppCompatActivity() {

        private lateinit var tvExample: TextView
        private lateinit var tvAnswer: TextView
        private lateinit var btnStart: Button
        private lateinit var btnCorrect: Button
        private lateinit var btnIncorrect: Button
        private lateinit var tvStatistics: TextView

        private var operand1 = 0
        private var operand2 = 0
        private var operator = ""
        private var correctAnswer = 0
        private var correctChoices = 0
        private var incorrectChoices = 0
        private var startTime = 0L
        private var choiceTimes: MutableList<Long> = mutableListOf()
        private var maxTime = 0L
        private var minTime = Long.MAX_VALUE
        private var averageTime = 0.0
        private var AnswerVar = ""
        private var AnswerCheck = true

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            tvExample = findViewById(R.id.tv_example)
            tvAnswer = findViewById(R.id.tv_answer)
            btnStart = findViewById(R.id.btn_start)
            btnCorrect = findViewById(R.id.btn_correct)
            btnIncorrect = findViewById(R.id.btn_incorrect)
            tvStatistics = findViewById(R.id.tv_statistics)

            btnStart.setOnClickListener {
                generateExample()
                btnStart.isEnabled = false
                btnCorrect.isEnabled = true
                btnIncorrect.isEnabled = true
                startTime = SystemClock.elapsedRealtime()
            }

            btnCorrect.setOnClickListener {
                checkChoice(AnswerCheck, true)

            }

            btnIncorrect.setOnClickListener {
                checkChoice(AnswerCheck, false)

            }

            updateStatistics()
        }

        private fun generateExample() {
            operand1 = Random.nextInt(10, 100)
            operand2 = Random.nextInt(10, 100)
            operator = when (Random.nextInt(4)) {
                0 -> "*"
                1 -> "/"
                2 -> "-"
                else -> "+"
            }
            if (operator == "/") {
                while (operand1 % operand2 != 0) {
                    operand1 = Random.nextInt(10, 100)
                    operand2 = Random.nextInt(10, 100)
                }
            }

            tvExample.text = "$operand1 $operator $operand2"


            if (Random.nextBoolean()) {
                correctAnswer = when (operator) {
                    "*" -> operand1 * operand2
                    "/" -> String.format("%.2f", operand1.toDouble() / operand2.toDouble()).toDouble()
                    "-" -> operand1 - operand2
                    else -> operand1 + operand2
                }.toInt()
                AnswerCheck = true
                tvAnswer.text = "Ответ: $correctAnswer"
            } else {
                val randomIncorrect = Random.nextInt(10, 100)
                AnswerCheck = false
                tvAnswer.text = "Ответ: $randomIncorrect"
            }
        }

        private fun checkChoice(Answer: Boolean, ButtonAnswer: Boolean) {
            val endTime = SystemClock.elapsedRealtime()
            val choiceTime = endTime - startTime
            choiceTimes.add(choiceTime)
            if (Answer == ButtonAnswer) {
                correctChoices++
                AnswerVar = "Пример решен правильно"
                if (choiceTime > maxTime) maxTime = choiceTime
                if (choiceTime < minTime) minTime = choiceTime
            } else {
                AnswerVar = "Пример решен неправильно"
                incorrectChoices++
            }

            calculateAverageTime()
            updateStatistics()

            generateExample()
        }

        private fun calculateAverageTime() {
            if (choiceTimes.isNotEmpty()) {
                averageTime = choiceTimes.average().toDouble()
            }
        }

        private fun updateStatistics() {
            val percentage = String.format("%.2f", (correctChoices.toDouble() / (correctChoices + incorrectChoices).toDouble() * 100))
            tvStatistics.text = "Статистика:\n" +
                    "Правильных: $correctChoices\n" +
                    "Неправильных: $incorrectChoices\n" +
                    "Процент: $percentage%\n" +
                    "Макс. время: ${formatTime(maxTime)}\n" +
                    "Мин. время: ${formatTime(minTime)}\n" +
                    "Среднее время: ${formatTime(averageTime.toLong())}\n" +
                    "${AnswerVar}"
        }

        private fun formatTime(timeInMillis: Long): String {
            val seconds = timeInMillis / 1000
            val milliseconds = timeInMillis % 1000
            return String.format("%02d.%03d", seconds, milliseconds)
        }


    }
