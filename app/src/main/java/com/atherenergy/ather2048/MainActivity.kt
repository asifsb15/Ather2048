package com.atherenergy.ather2048

import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.atherenergy.ather2048.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var x1: Float = 0.0F
    private var x2: Float = 0.0F
    private var y1: Float = 0.0F
    private var y2: Float = 0.0F
    private val MIN_DISTANCE: Int = 150
    private var isWinner = false
    private var gridScoreToWin = 2048 // this goes on like 2048, 4096...
    private var totalScore = 0
    private var mat = Array(4) { IntArray(4) }
    private var prev = Array(4) { IntArray(4) }
    private var textViewIds = Array(4) { IntArray(4) }
    private var cardViewIds = Array(4) { IntArray(4) }
    private val evenArray = mutableListOf<Int>(2, 4)
    private val positionArray = mutableListOf<Int>(0, 1, 2, 3)
    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //develop branch test
        initGame()
    }

    //initialize all the values of the game
    private fun initGame() {
        resetGrid()
        idsMapping()
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
        setUI()
        updateScore()
    }

    //reset all the grid values at the beginning
    private fun resetGrid() {
        for (i in 0..3) {
            for (j in 0..3) {
                mat[i][j] = 0
            }
        }
    }

    //updates score here
    private fun updateScore() {
        mBinding.tvScore.text = getString(R.string.score).plus(" ").plus(totalScore)
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
            val textView: TextView = findViewById(textViewIds[gridPos1][gridPos2])
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
//                    Toast.makeText(applicationContext, "SwipeRight", Toast.LENGTH_SHORT).show()
                    onSwipeRight()
                } else if (abs(deltaX) > MIN_DISTANCE && x2 < x1) {
//                    Toast.makeText(applicationContext, "SwipeLeft", Toast.LENGTH_SHORT).show()
                    onSwipeLeft()
                } else if (abs(deltaY) > MIN_DISTANCE && y2 > y1) {
//                    Toast.makeText(applicationContext, "SwipeDown", Toast.LENGTH_SHORT).show()
                    onSwipeDown()
                } else if (abs(deltaY) > MIN_DISTANCE && y2 < y1) {
//                    Toast.makeText(applicationContext, "SwipeUp", Toast.LENGTH_SHORT).show()
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
                    totalScore += mat[i][j]
                    if (mat[i][j] == gridScoreToWin) {
                        isWinner = true
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
                    totalScore += mat[i][j]
                    if (mat[i][j] == gridScoreToWin) {
                        isWinner = true
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
                    totalScore += mat[i][j]
                    if (mat[i][j] == gridScoreToWin)
                        isWinner = true
                    mat[i + 1][j] = 0
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

    private fun onSwipeDown() {
        for (i in 0..3) {
            for (j in 0..3) {
                prev[i][j] = mat[i][j]
            }
        }
        slideDown()
        for (j in 0..3) {
            for (i in 3 downTo 1) {
                if (mat[i][j] == mat[i - 1][j]) {
                    mat[i][j] += mat[i - 1][j]
                    totalScore += mat[i][j]
                    if (mat[i][j] == gridScoreToWin)
                        isWinner = true
                    mat[i - 1][j] = 0
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
            if (gameOverCheck()) {
                dialogPopup(true)
//                Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_LONG).show()
            }
        }
        //show values in UI and update the score
        setUI()
        updateScore()

        //if value is true end the process and show winner
        if (isWinner) {
            dialogPopup(false)
//            Toast.makeText(this, getString(R.string.winner), Toast.LENGTH_LONG).show()
        }
    }

    private fun gameOverCheck(): Boolean {
        //check all possible sides
        for (i in 0..3) {
            for (j in 0..3) {
                //check if any grid is empty
                if (mat[i][j] == 0) {
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
        var count = 0
        for (i in 0..3) {
            for (j in 0..3) {
                if (prev[i][j] == mat[i][j]) {
                    count += 1
                }
            }
        }
        //move all tep matrix array to main array
        for (i in 0..3) {
            for (j in 0..3) {
                mat[i][j] = prev[i][j]
            }
        }
        return count == 16
    }

    private fun setUI() {
        for (i in 0..3) {
            for (j in 0..3) {
                val textView: TextView = findViewById(textViewIds[i][j])
                val cardView: CardView = findViewById(cardViewIds[i][j])
                if (mat[i][j] == 0) {
                    textView.text = ("")
                    cardView.setCardBackgroundColor(resources.getColor(R.color.tint0, this.theme))
                } else {
                    textView.text = (mat[i][j].toString())
                    when {
                        mat[i][j] == 2 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint2,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 4 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint4,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 8 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint8,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 16 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint16,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 32 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint32,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 64 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint64,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 128 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint128,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 256 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint256,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 512 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint512,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 1024 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint1024,
                                    this.theme
                                )
                            )
                        }
                        mat[i][j] == 2048 -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tint2048,
                                    this.theme
                                )
                            )
                        }
                        else -> {
                            cardView.setCardBackgroundColor(
                                resources.getColor(
                                    R.color.tintAbove,
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

    private fun dialogPopup(isGameOver: Boolean) {
        try {
            val builder =
                AlertDialog.Builder(this)
            builder.setTitle(if (isGameOver) getString(R.string.game_over) else getString(R.string.winner))
            val dialogClickListener =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE ->  //Yes button clicked
                        {
                            if (isGameOver) {
                                //replay
                                initGame()
                                gridScoreToWin = 2048
                            }else{
                                gridScoreToWin *= 2
                            }
                            builder.create().dismiss()
                        }
                        DialogInterface.BUTTON_NEGATIVE ->  //No button clicked
                            builder.create().dismiss()
                    }
                }
            var value = gridScoreToWin
            if(!isGameOver){
                value = gridScoreToWin*2
            }
            builder.setMessage(if (isGameOver) getString(R.string.play_again) else getString(R.string.play_continue).plus(" $value"))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun idsMapping() {
        textViewIds[0][0] = mBinding.text1.id
        textViewIds[0][1] = mBinding.text2.id
        textViewIds[0][2] = mBinding.text3.id
        textViewIds[0][3] = mBinding.text4.id
        textViewIds[1][0] = mBinding.text5.id
        textViewIds[1][1] = mBinding.text6.id
        textViewIds[1][2] = mBinding.text7.id
        textViewIds[1][3] = mBinding.text8.id
        textViewIds[2][0] = mBinding.text9.id
        textViewIds[2][1] = mBinding.text10.id
        textViewIds[2][2] = mBinding.text11.id
        textViewIds[2][3] = mBinding.text12.id
        textViewIds[3][0] = mBinding.text13.id
        textViewIds[3][1] = mBinding.text14.id
        textViewIds[3][2] = mBinding.text15.id
        textViewIds[3][3] = mBinding.text16.id
        cardViewIds[0][0] = mBinding.Grid1.id
        cardViewIds[0][1] = mBinding.Grid2.id
        cardViewIds[0][2] = mBinding.Grid3.id
        cardViewIds[0][3] = mBinding.Grid4.id
        cardViewIds[1][0] = mBinding.Grid5.id
        cardViewIds[1][1] = mBinding.Grid6.id
        cardViewIds[1][2] = mBinding.Grid7.id
        cardViewIds[1][3] = mBinding.Grid8.id
        cardViewIds[2][0] = mBinding.Grid9.id
        cardViewIds[2][1] = mBinding.Grid10.id
        cardViewIds[2][2] = mBinding.Grid11.id
        cardViewIds[2][3] = mBinding.Grid12.id
        cardViewIds[3][0] = mBinding.Grid13.id
        cardViewIds[3][1] = mBinding.Grid14.id
        cardViewIds[3][2] = mBinding.Grid15.id
        cardViewIds[3][3] = mBinding.Grid16.id
    }

}