package com.sample.calculatorupgrade

object ExpressionEvaluator {
    fun evaluate(expression: String): Double {
        val sanitizedExpression = expression.replace("×", "*").replace("÷", "/")
        return object : Any() {
            var pos = -1
            var ch: Char = ' '

            fun nextChar() {
                ch = if (++pos < sanitizedExpression.length) sanitizedExpression[pos] else '\u0000'
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < sanitizedExpression.length) throw RuntimeException("Unexpected: $ch")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+') -> x += parseTerm()
                        eat('-') -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*') -> x *= parseFactor()
                        eat('/') -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor()
                if (eat('-')) return -parseFactor()
                var x: Double
                val startPos = pos
                if (eat('(')) {
                    x = parseExpression()
                    eat(')')
                } else if (ch in '0'..'9' || ch == '.') {
                    while (ch in '0'..'9' || ch == '.') nextChar()
                    x = sanitizedExpression.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: $ch")
                }
                return x
            }
        }.parse()
    }
}
