package com.sample.calculatorupgrade

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var inputET: EditText
    private lateinit var resultTV: TextView
    private lateinit var inputField: EditText
    private lateinit var outputField: TextView
    private var currentExpression: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        supportActionBar?.title = "Калькулятор"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        inputField = findViewById(R.id.inputET)
        outputField = findViewById(R.id.resultTV)

        // Инициализация кнопок
        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9, R.id.buttonDot, R.id.buttonPlus,
            R.id.buttonMinus, R.id.buttonDivide, R.id.buttonMultiply
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick((it as Button).text.toString()) }
        }

        findViewById<Button>(R.id.buttonEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.resetData).setOnClickListener { clearInput() }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }


    private fun onButtonClick(value: String) {
        currentExpression += value
        inputField.setText(currentExpression)
    }

    private fun clearInput() {
        currentExpression = ""
        inputField.setText("")
        outputField.text = ""
    }

    private fun calculateResult() {
        try {
            inputField.setText(inputField.text.toString() + "=")
            val result = ExpressionEvaluator.evaluate(currentExpression)
            outputField.text = result.toString()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка: неверное выражение", Toast.LENGTH_SHORT).show()
        }
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