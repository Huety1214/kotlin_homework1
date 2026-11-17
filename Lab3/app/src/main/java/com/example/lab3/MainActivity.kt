package com.example.lab3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 優化重點：
 * 1. 用 enum class Mora 取代 0/1/2 魔術數字，出拳名稱跟數值綁在一起。
 * 2. 勝負判斷改成 (我方 - 電腦 + 3) % 3 的公式，取代三組 || 條件。
 * 3. 用 enum class Result 表示結果，畫面更新集中在一個地方。
 */
class MainActivity : AppCompatActivity() {

    // 出拳種類：ordinal 0=剪刀、1=石頭、2=布（後一個剛好贏前一個）
    private enum class Mora(val label: String) {
        SCISSOR("剪刀"), STONE("石頭"), PAPER("布");

        companion object {
            fun random(): Mora = entries.random()
        }
    }

    private enum class Result { DRAW, WIN, LOSE }

    // (a - b + 3) % 3：0 = 平手、1 = a 贏、2 = a 輸
    private fun judge(me: Mora, cpu: Mora): Result =
        when ((me.ordinal - cpu.ordinal + 3) % 3) {
            0 -> Result.DRAW
            1 -> Result.WIN
            else -> Result.LOSE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val edName = findViewById<EditText>(R.id.edName)
        val tvText = findViewById<TextView>(R.id.tvText)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvWinner = findViewById<TextView>(R.id.tvWinner)
        val tvMyMora = findViewById<TextView>(R.id.tvMyMora)
        val tvTargetMora = findViewById<TextView>(R.id.tvTargetMora)

        findViewById<Button>(R.id.btnMora).setOnClickListener {
            val playerName = edName.text.toString().trim()
            if (playerName.isEmpty()) {
                tvText.text = "請輸入玩家姓名"
                return@setOnClickListener
            }

            val myMora = when (radioGroup.checkedRadioButtonId) {
                R.id.btnScissor -> Mora.SCISSOR
                R.id.btnStone -> Mora.STONE
                else -> Mora.PAPER
            }
            val targetMora = Mora.random()

            tvName.text = "名字\n$playerName"
            tvMyMora.text = "我方出拳\n${myMora.label}"
            tvTargetMora.text = "電腦出拳\n${targetMora.label}"

            // when 是運算式，可直接解構成 (勝利者, 提示文字)
            val (winner, message) = when (judge(myMora, targetMora)) {
                Result.DRAW -> "平手" to "平局，請再試一次！"
                Result.WIN -> playerName to "恭喜你獲勝了！！！"
                Result.LOSE -> "電腦" to "可惜，電腦獲勝了！"
            }
            tvWinner.text = "勝利者\n$winner"
            tvText.text = message
        }
    }
}
