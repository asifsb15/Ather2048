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
    var mat = Array(4) { IntArray(4) }
    var prev = Array(4) { IntArray(4) }
    var textIds = Array(4) { IntArray(4) }
    var cardIds = Array(4) { IntArray(4) }
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

    private fun initGame() {
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
                    Toast.makeText(applicationContext, "SwipeRight", Toast.LENGTH_LONG)
                } else if (abs(deltaX) > MIN_DISTANCE && x2 < x1) {
                    Toast.makeText(applicationContext, "SwipeLeft", Toast.LENGTH_LONG)
                } else if (abs(deltaY) > MIN_DISTANCE && y2 > y1) {
                    Toast.makeText(applicationContext, "SwipeDown", Toast.LENGTH_LONG)
                } else if (abs(deltaY) > MIN_DISTANCE && y2 < y1) {
                    Toast.makeText(applicationContext, "SwipeUp", Toast.LENGTH_LONG)
                }
                true
            }
            else -> return super.onTouchEvent(event)
        }
    }

    fun showValues() {
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
                            c.setCardBackgroundColor(resources.getColor(R.color.tint128, this.theme))
                        }
                        mat[i][j] == 256 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint256, this.theme))
                        }
                        mat[i][j] == 512 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint512, this.theme))
                        }
                        mat[i][j] == 1024 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint1024, this.theme))
                        }
                        mat[i][j] == 2048 -> {
                            c.setCardBackgroundColor(resources.getColor(R.color.tint2048, this.theme))
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