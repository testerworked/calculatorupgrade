package com.sample.calculatorupgrade

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var inputET: EditText
    private lateinit var resultTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        inputET = findViewById(R.id.inputET)
        resultTV = findViewById(R.id.resultTV)

        val customKeyboard = findViewById<GridLayout>(R.id.customKeyboard)
        for (i in 0 until customKeyboard.childCount) {
            val button = customKeyboard.getChildAt(i) as Button
            button.setOnClickListener { onKeyboardButtonClick(button) }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.button_reset).setOnClickListener {
            resetInput()
        }
    }

    private fun resetInput() {
        inputET.text.clear()
        resultTV.text = ""
    }

    private fun onKeyboardButtonClick(button: Button) {
        val buttonText = button.text.toString()

        if (buttonText == "=") {
            calculateResult()
        } else {
            inputET.setText(inputET.text.toString() + buttonText)
        }
    }

    private fun calculateResult() {
        val expression = inputET.text.toString()
        try {
            val result = evaluateExpression(expression)
            resultTV.text = result.toString()
        } catch (e: Exception) {
            resultTV.text = "Ошибка"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val formattedExpression = expression.replace(" ", "")
        val numbers = mutableListOf<Double>()
        val operations = mutableListOf<Char>()
        val regex = Regex("([+-]?\\d*\\.?\\d+|[-+/*])")

        regex.findAll(formattedExpression).forEach {
            if (it.value.toDoubleOrNull() != null) {
                numbers.add(it.value.toDouble())
            } else {
                operations.add(it.value.first())
            }
        }

        var i = 0
        while (i < operations.size) {
            when (operations[i]) {
                '*', '/' -> {
                    val operation = operations.removeAt(i)
                    val leftNumber = numbers.removeAt(i)
                    val rightNumber = numbers.removeAt(i)
                    val result = if (operation == '*') leftNumber * rightNumber else leftNumber / rightNumber
                    numbers.add(i, result)
                }
                else -> i++
            }
        }

        var result = numbers[0]
        for (j in 0 until operations.size) {
            when (operations[j]) {
                '+' -> result += numbers[j + 1]
                '-' -> result -= numbers[j + 1]
            }
        }
        return result
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}