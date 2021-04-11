package kt.fellowweeb.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val INPUT_KEY = "INPUT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach { btn ->
            btn.setOnClickListener { input.append(btn.text) }
        }
        btnClr.setOnClickListener { input.text = null }
        btnDot.setOnClickListener { input.append(btnDot.text) }
        btnEval.setOnClickListener {
            input.text = try {
                ExpressionBuilder(input.text.toString()).build().evaluate().toString()
            } catch (e: RuntimeException) {
                Log.i(TAG, e.toString())
                getString(R.string.error)
            }
        }
        listOf(btnPlus, btnMinus, btnMult, btnDiv).forEach { btn ->
            btn.setOnClickListener {
                input.append(btn.text);
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(INPUT_KEY, input.text)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        input.text = savedInstanceState.getCharSequence(INPUT_KEY)
    }
}