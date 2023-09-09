package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClearClick(view: View) {

        binding.tvInput.text = ""
        lastNumeric = false

    }

    fun onBackClick(view: View) {

        binding.tvInput.text = binding.tvInput.text.toString().dropLast(1)

        try{

            val lastChar = binding.tvInput.text.toString().last()
            if(lastChar.isDigit()){
                onEqual()
            }

        }catch(e : Exception){

            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
            Log.e("last character error", e.toString())

        }

    }

    fun onDigitClick(view: View) {

        if(stateError){
            binding.tvInput.text = (view as Button).text
            stateError = false
        }
        else{
            binding.tvInput.append((view as Button).text)
        }

        lastNumeric = true
        onEqual()

    }

    fun onOperatorClick(view: View) {

        if(!stateError && lastNumeric){

            binding.tvInput.append((view as Button).text)
            lastDot= false
            lastNumeric = false
            onEqual()

        }

    }

    fun onEqualClick(view: View) {

        onEqual()
        binding.tvInput.text = binding.tvResult.text.toString().drop(1)

    }

    fun onAllclearClick(view: View) {

        binding.tvInput.text = ""
        binding.tvResult.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.tvResult.visibility = View.GONE

    }

    fun onEqual(){

        if(lastNumeric && !stateError){
            val txt = binding.tvInput.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {

                val result = expression.evaluate()

                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = "=" + result.toString()

            }catch(e : ArithmeticException){
                Log.e("evalute error", e.toString())
                binding.tvResult.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }

    }

}