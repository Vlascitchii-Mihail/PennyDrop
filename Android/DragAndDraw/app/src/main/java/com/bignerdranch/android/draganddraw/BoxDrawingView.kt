package com.bignerdranch.android.draganddraw

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.graphics.PointF
import android.util.Log
import android.graphics.Paint
import android.graphics.Canvas

private const val TAG = "BoxDrawingView"

class BoxDrawingView(context: Context, attr: AttributeSet? = null): View(context, attr) {

    private  var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()

    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8e0.toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                //reset the state of the object
                currentBox = Box(current).also {
                    boxen.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        Log.i(TAG, "4action at x=${current.x}, y = ${current.y}")
        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        //background filling
        canvas.drawPaint(backgroundPaint)

        boxen.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }
}