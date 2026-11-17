# Kotlin Homework1 報告（Lab2 猜拳介面、Lab3 猜拳程式）

> 繳交檔名：`113360122_林彥廷.pdf`

## 一、讀書會
| 項目 | 內容 |
|---|---|
| 組員 | （填寫） |
| 討論時間 | （填寫） |
| 地點 / Google Meet | （填寫） |
| 照片 / 截圖 | （貼上） |

## 二、GitHub
- 程式連結：`https://github.com/Huety1214/kotlin_homework1`
- GitHub 截圖：（貼上）

## 三、Lab 內容
- **Lab2 版面配置**：用 ConstraintLayout 排出姓名輸入框、剪刀/石頭/布的 RadioGroup、猜拳按鈕，以及名字、勝利者、我方出拳、電腦出拳四個結果欄位。
- **Lab3 物件控制與事件監聽**：用 `findViewById` 綁定元件，在 `btnMora.setOnClickListener` 中讀取玩家出拳、亂數產生電腦出拳，判斷勝負後更新畫面。

## 四、優化程式碼心得（電腦猜拳可讀性）

原本的寫法用 `0、1、2` 代表剪刀、石頭、布，勝負判斷要列出三組 `||` 條件，閱讀時必須一直對照「0 是什麼」。我做了三項優化：

1. **用 `enum class Mora` 取代魔術數字**：出拳名稱跟數值綁在一起，`getMoraString()` 不再需要。
2. **勝負公式化**：剪刀→石頭→布，後一個剛好贏前一個，所以 `(我方 - 電腦 + 3) % 3` 等於 0 是平手、1 是贏、2 是輸。三組條件縮成一行。
3. **`when` 回傳 `Pair` 再解構**：勝利者文字與提示訊息一次決定，畫面更新只寫一次。

```kotlin
private enum class Mora(val label: String) { SCISSOR("剪刀"), STONE("石頭"), PAPER("布") }

val (winner, message) = when ((myMora.ordinal - targetMora.ordinal + 3) % 3) {
    0 -> "平手" to "平局，請再試一次！"
    1 -> playerName to "恭喜你獲勝了！！！"
    else -> "電腦" to "可惜，電腦獲勝了！"
}
```

## 五、Java 與 Kotlin 語法差異

**1. 取得元件與宣告變數**

Java 語法
```java
EditText edName = findViewById(R.id.edName);
final int targetMora = (int) (Math.random() * 3);
```
Kotlin 語法
```kotlin
val edName = findViewById<EditText>(R.id.edName)   // val = 不可變，型別自動推論
val targetMora = (0..2).random()                   // Range 直接取亂數
```

**2. 點擊事件**

Java 語法
```java
btnMora.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) { ... }
});
```
Kotlin 語法
```kotlin
btnMora.setOnClickListener { ... }   // SAM 轉換，lambda 直接當監聽器
```

**3. 多重判斷：switch vs when**

Java 語法
```java
int myMora;
switch (radioGroup.getCheckedRadioButtonId()) {
    case R.id.btnScissor: myMora = 0; break;
    case R.id.btnStone:   myMora = 1; break;
    default:              myMora = 2;
}
```
Kotlin 語法
```kotlin
val myMora = when (radioGroup.checkedRadioButtonId) {  // when 是運算式，可直接賦值
    R.id.btnScissor -> Mora.SCISSOR
    R.id.btnStone -> Mora.STONE
    else -> Mora.PAPER
}
```

**4. 字串組合**

Java 語法
```java
tvName.setText("名字\n" + playerName);
```
Kotlin 語法
```kotlin
tvName.text = "名字\n$playerName"   // 字串模板；getText/setText 變成 property
```

**5. 空字串判斷**

Java 語法
```java
if (edName.getText().toString().equals("")) { ... }
```
Kotlin 語法
```kotlin
if (edName.text.isEmpty()) { ... }
```

## 六、心得
（依自己的感想修改）Kotlin 的 `when`、字串模板和 lambda 讓同樣的猜拳邏輯比 Java 少很多樣板程式碼。把 0/1/2 換成 enum 之後，程式讀起來就像在描述規則本身，之後要加新的出拳種類也更容易。
