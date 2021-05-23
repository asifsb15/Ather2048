package com.atherenergy.ather2048

import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var x1: Float = 0.0F
    private var x2: Float = 0.0F
    private var y1: Float = 0.0F
    private var y2: Float = 0.0F
    private val MIN_DISTANCE: Int = 150
    private var winner = false
    var score = 0
    private var mat = Array(4) { IntArray(4) }
    var prev = Array(4) { IntArray(4) }
    var textIds = Array(4) { IntArray(4) }
    var cardIds = Array(4) { IntArray(4) }
    private val evenArray = mutableListOf<Int>(2, 4)
    private val positionArray = mutableListOf<Int>(0, 1, 2, 3)
    private fun mappingGrid() {
        textIds[0][0] = R.id.text1
        textIds[0][1] = R.id.text2
        textIds[0][2] = R.id.text3
        textIds[0][3] = R.id.text4
        textIds[1][0] = R.id.text5
        textIds[1][1] = R.id.text6
        textIds[1][2] = R.id.text7
        textIds[1][3] = R.id.text8
        textIds[2][0] = R.id.text9
        textIds[2][1] = R.id.text10
        textIds[2][2] = R.id.text11
        textIds[2][3] = R.id.text12
        textIds[3][0] = R.id.text13
        textIds[3][1] = R.id.text14
        textIds[3][2] = R.id.text15
        textIds[3][3] = R.id.text16
        cardIds[0][0] = R.id.Grid1
        cardIds[0][1] = R.id.Grid2
        cardIds[0][2] = R.id.Grid3
        cardIds[0][3] = R.id.Grid4
        cardIds[1][0] = R.id.Grid5
        cardIds[1][1] = R.id.Grid6
        cardIds[1][2] = R.id.Grid7
        cardIds[1][3] = R.id.Grid8
        cardIds[2][0] = R.id.Grid9
        cardIds[2][1] = R.id.Grid10
        cardIds[2][2] = R.id.Grid11
        cardIds[2][3] = R.id.Grid12
        cardIds[3][0] = R.id.Grid13
        cardIds[3][1] = R.id.Grid14
        cardIds[3][2] = R.id.Grid15
        cardIds[3][3] = R.id.Grid16
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //develop branch test
        initGame()
    }

    //initialize all the values of the game
    private fun initGame() {
        clearGrid()
        mappingGrid()
        //set two values at the beginning
        val randomValue1 = randomNumberGen(evenArray)
        val randomValue2 = randomNumberGen(evenArray)
        val grid1Pos1 = randomNumberGen(positionArray)
        val grid1Pos2 = randomNumberGen(positionArray)
        val grid2Pos1 = randomNumberGen(positionArray)
        val grid2Pos2 = randomNumberGen(positionArray)
        mat[grid1Pos1][grid1Pos2] = randomValue1
        mat[grid2Pos1][grid2Pos2] = randomValue2
        //show the values in UI
        showValues()
        updateScore()
    }

    //reset all the grid values at the beginning
    private fun clearGrid() {
        for (i in 0..3) {
            for (j in 0..3) {
                mat[i][j] = 0
            }
        }
    }

    //updates score here
    private fun updateScore() {
        val textView: TextView = findViewById(R.id.score1)
        textView.text = getString(R.string.score).plus(" ").plus(score)
    }

    private fun insertNumberInRandom() {
        var flag = false
        //run loop till flag becomes true
        while (!flag) {
            flag = insertNumber()
        }
    }

    private fun insertNumber(): Boolean {
        val valueForGrid = randomNumberGen(evenArray)
        val gridPos1 = randomNumberGen(positionArray)
        val gridPos2 = randomNumberGen(positionArray)
        return if (mat[gridPos1][gridPos2] == 0) {
            mat[gridPos1][gridPos2] = valueForGrid
            val textView: TextView = findViewById(textIds[gridPos1][gridPos2])
            textView.text = valueForGrid.toString()
            true
        } else {
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //Toast.makeText(applicationContext,"Entered onTouch",Toast.LENGTH_SHORT).show()
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                this.x1 = event.x
                this.y1 = event.y
                true
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                val deltaX = x2 - x1
                val deltaY = y2 - y1
                if (abs(deltaX) > MIN_DISTANCE && x2 > x1) {
                    Toast.makeText(applicationContext, "SwipeRight", Toast.LENGTH_SHORT).show()
                    onSwipeRight()
                } else if (abs(deltaX) > MIN_DISTANCE && x2 < x1) {
                    Toast.makeText(applicationContext, "SwipeLeft", Toast.LENGTH_SHORT).show()
                    onSwipeLeft()
                } else if (abs(deltaY) > MIN_DISTANCE && y2 > y1) {
                    Toast.makeText(applicationContext, "SwipeDown", Toast.LENGTH_SHORT).show()
                    onSwipeDown()
                } else if (abs(deltaY) > MIN_DISTANCE && y2 < y1) {
                    Toast.makeText(applicationContext, "SwipeUp", Toast.LENGTH_SHORT).show()
                    onSwipeUp()
                }
                true
            }
            else -> return super.onTouchEvent(event)
        }
    }

    private fun slideRight() {
        var k = 3
        for (i in 0..3) {
            for (j in 3 downTo 0) {
                if (mat[i][j] != 0) {
                    mat[i][k] = mat[i][j]
                    k -= 1
                }
            }
            if (k >= 0) {
                for (j in k downTo 0) {
                    mat[i][j] = 0
                }
            }
            k = 3
        }
    }

    private fun onSwipeRight() {
        // keep all the value in temp int array
        for (i in 0..3) {
            for (j in 0..3) {
                prev[i][j] = mat[i][j]
            }
        }
        //slide all values
        slideRight()
        //add if two values are same
        for (i in 0..3) {
            for (j in 3 downTo 1) {
                if (mat[i][j] == mat[i][j - 1]) {
                    mat[i][j] += mat[i][j - 1]
                    score += mat[i][j]
                    if (mat[i][j] == 2048) {
                        winner = true
                    }
                    mat[i][j - 1] = 0
                }
            }
        }
        //slide all the value to right one more time
        slideRight()
        swipeValidationForALL()
    }

    private fun onSwipeLeft() {
        for (i in 0..3) {
            for (j in 0..3) {
                prev[i][j] = mat[i][j]
            }
        }
        slideLeft()
        for (i in 0..3) {
            for (j in 0..2) {
                if (mat[i][j] == mat[i][j + 1]) {
                    mat[i][j] += mat[i][j + 1]
                    score += mat[i][j]
                    if (mat[i][j] == 2048) {
                        winner = true
                    }
                    mat[i][j + 1] = 0
                }
            }
        }
        slideLeft()
        swipeValidationForALL()
    }

    private fun slideLeft() {

        var k = 0
        for (i in 0..3) {
            for (j in 0..3) {
                if (mat[i][j] != 0) {
                    mat[i][k] = mat[i][j]
                    k += 1
                }
            }
            if (k <= 3) {
                for (j in k..3) {
                    mat[i][j] = 0
                }
            }
            k = 0
        }
    }

    private fun onSwipeUp() {

        for (i in 0..3) {
            for (j in 0..3) {
                prev[i][j] = mat[i][j]
            }
        }
        slideUp()
        for (j in 0..3) {
            for (i in 0..2) {
                if (mat[i][j] == mat[i + 1][j]) {
                    mat[i][j] += mat[i + 1][j]
                    score += mat[i][j]
                    if (mat[i][j] == 2048)
                        winner = true
                    mat[i+1][j] = 0
                }
            }
        }
        slideUp()
        swipeValidationForALL()
    }

    private fun slideUp() {
        var k = 0
        for (j in 0..3) {
            for (i in 0..3) {
                if (mat[i][j] != 0) {
                    mat[k][j] = mat[i][j]
                    k += 1
                }
            }
            if (k <= 3) {
                for (i in k..3) {
                    mat[i][j] = 0
                }
            }
            k = 0
        }
    }

    private fun onSwipeDown(){
        for(i in 0..3){
            for (j in 0..3){
                prev[i][j]=mat[i][j]
            }
        }
        slideDown()
        for (j in 0..3){
            for (i in 3 downTo 1){
                if(mat[i][j]==mat[i-1][j]){
                    mat[i][j]+=mat[i-1][j]
                    score+=mat[i][j]
                    if(mat[i][j]==2048)
                        winner = true
                    mat[i-1][j]=0
                }
            }
        }
        slideDown()
        swipeValidationForALL()
    }

    private fun slideDown() {
        var k = 3
        for (j in 0..3) {
            for (i in 3 downTo 0) {
                if (mat[i][j] != 0) {
                    mat[k][j] = mat[i][j]
                    k -= 1
                }
            }
            if (k >= 0) {
                for (i in k downTo 0) {
                    mat[i][j] = 0
                }
            }
            k = 3
        }
    }

    private fun swipeValidationForALL() {
        //check for the count
        var count = 0
        for (i in 0..3) {
            for (j in 0..3) {
                if (prev[i][j] == mat[i][j]) {
                    count += 1
                }
            }
        }

        if (count < 16) {
            //still space available fill the grid with random number
            insertNumberInRandom()
        } else if (count == 16) {
            if (gameOver()) {
                Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_LONG).show()
            }
        }
        //show values in UI and update the score
        showValues()
        updateScore()

        //if value is true end the process and show winner
        if (winner) {
            Toast.makeText(this, getString(R.string.winner), Toast.LENGTH_LONG).show()
        }
    }

    private fun gameOver(): Boolean {
        //check all possible sides
        for(i in 0..3){
            for(j in 0..3){
                //check if any grid is empty
                if(mat[i][j]==0){
                    return false
                }
            }
        }
        //slide all grids
        slideUp()
        slideDown()
        slideLeft()
        slideRight()
        //check for the grid count
        var count =0
        for (i in 0..3){
            for(j in 0..3){
                if(prev[i][j]==mat[i][j]){
                    count+=1
                }
            }
        }
        //move all tep matrix array to main array
        for (i in 0..3){
            for (j in 0..3){
                mat[i][j]=prev[i][j]
            }
        }
        return count==16
    }

    private fun showValues() {
        for (i in 0..3) {
            for (j in 0..3) {
                val t: TextView = findViewById(textIds[i][j])
                val c: CardView = findViewById(cardIds[i][j])
                if (mat[i][j] == 0) {
                    t.text = ("")
                    c.setCardBackgroundColor(resources.getColor(R.color.tint0, this.theme))
                } else {
                    t.text = (mat[i][j].toString())
                    when {
                        mat[i][j] == 2 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint2, this.theme))
                        }
                        mat[i][j] == 4 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint4, this.theme))
                        }
                        mat[i][j] == 8 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint8, this.theme))
                        }
                        mat[i][j] == 16 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint16, this.theme))
                        }
                        mat[i][j] == 32 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint32, this.theme))
                        }
                        mat[i][j] == 64 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint64, this.theme))
                        }
                        mat[i][j] == 128 -> {
                            c.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint128,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 256 -> {
                            c.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint256,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 512 -> {
                            c.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint512,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 1024 -> {
                            c.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint1024,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 2048 -> {
                            c.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint2048,
                                    this.theme
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun randomNumberGen(x: MutableList<Int>): Int {
        return x.shuffled().first()
    }

}