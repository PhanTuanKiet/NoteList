package com.example.tuankiet.notelist

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.MotionEvent
import android.view.View

enum class ButtonState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

class SwipeController : ItemTouchHelper.Callback() {

    var swipeBack: Boolean = false
    var buttonState = ButtonState.GONE
    var buttonInstance : RectF? = null
    var currentItemViewHolder : RecyclerView.ViewHolder? = null
    val buttonWidth: Float = 300f
    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
        return ItemTouchHelper.Callback.makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonState != ButtonState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var dx_temp = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonState != ButtonState.GONE){
                if (buttonState == ButtonState.LEFT_VISIBLE)
                    dx_temp = Math.max(dX, buttonWidth)
                if (buttonState == ButtonState.RIGHT_VISIBLE)
                    dx_temp = Math.min(dX, -buttonWidth)
                super.onChildDraw(c, recyclerView, viewHolder, dx_temp, dY, actionState, isCurrentlyActive)
            } else {
                setTouchListener(c, recyclerView, viewHolder, dx_temp, dY, actionState, isCurrentlyActive)
            }
        }

        if (buttonState == ButtonState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder

        drawButtons(c, viewHolder)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        recyclerView.setOnTouchListener { v, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonWidth)
                    buttonState = ButtonState.RIGHT_VISIBLE
                else if (dX > buttonWidth)
                    buttonState = ButtonState.LEFT_VISIBLE

                if (buttonState != ButtonState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    setItemClickable(recyclerView, false)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { v, event -> false }
                setItemClickable(recyclerView, true)
                swipeBack = false
            }
            false
        }
    }

    fun setItemClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {

        var buttonWidthWithoutPadding: Float = buttonWidth - 20
        var corners: Float = 16f
        val itemView = viewHolder.itemView
        val p = Paint()

        val leftButton = RectF(itemView.left.toFloat(),
            itemView.top.toFloat(),itemView.left + buttonWidthWithoutPadding, itemView.bottom.toFloat())
        p.setColor(Color.BLUE)
        c.drawRoundRect(leftButton,corners,corners,p)
        drawText("EDIT",c,leftButton,p)

        val rightButton = RectF(itemView.right.toFloat() - buttonWidthWithoutPadding,
            itemView.top.toFloat(),itemView.right.toFloat(), itemView.bottom.toFloat())
        p.setColor(Color.RED)
        c.drawRoundRect(rightButton,corners,corners,p)
        drawText("DELETE",c,rightButton,p)

        buttonInstance = null
        if (buttonState == ButtonState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
        else if (buttonState == ButtonState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }

    }

    fun drawText(text : String,c : Canvas,button : RectF,p : Paint){
        var textSize : Float = 60f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(text, button.centerX() - textWidth / 2, button.centerY() + textSize / 2, p)

    }
}