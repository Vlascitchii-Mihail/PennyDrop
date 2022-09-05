package com.bignerdranch.android.draganddraw

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.graphics.PointF
import android.util.Log
import android.graphics.Paint
import android.graphics.Canvas

private const val TAG = "BoxDrawingView"

//холст
class BoxDrawingView(context: Context, attr: AttributeSet? = null): View(context, attr) {

    private  var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()

    //rectangle's color
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8e0.toInt()
    }

    //touch listener
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        /**
         * @param  X and Y - touch coordinates
         * @param  PointF holds two float coordinates
         */
        val current = PointF(event.x, event.y)
        var action = ""
        when(event.action) {

            //user touched the screen
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"

                //creating a new rectangle and adding in list
                currentBox = Box(current).also {
                    boxen.add(it)
                }
            }

            //user moves the finger on the screen
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"

                //updating currentBox.end
                updateCurrentBox(current)
            }

            //user removes his finger
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"

                //updating currentBox.end
                updateCurrentBox(current)

                //reset the rectangle's variable of the object
                currentBox = null
            }

            //parent view intercepted (перехватило) the event
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        Log.i(TAG, "4action at x=${current.x}, y = ${current.y}")
        return true
    }

    //updating currentBox.end
    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current

            //updating the rectangle state on the screen when we move the finger
            //calls the onDraw() function
            invalidate()
        }
    }

    //drawing all the subclasses and shows the rectangle on the screen
    /**
     * @param Canvas - all the operations with graphics. What to draw
     * @param Paint - styling the drawing objects
     */
    override fun onDraw(canvas: Canvas) {
        //background's filling
        canvas.drawPaint(backgroundPaint)

        boxen.forEach { box ->

            //drawing the rectangle
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }
}