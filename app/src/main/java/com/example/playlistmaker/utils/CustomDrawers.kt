package com.example.playlistmaker.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.DashPathEffect
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.Float
import kotlin.Int
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class CustomDrawers {
    companion object {
        fun drawCustomDashedBorder(
            strokeWidth: Float,
            dashLength: Float,
            dashGap: Float,
            color: Int,
            cornerRadius: Float = 0f,
            view: View? = null
        ): Drawable {
            return CustomDashedBorder(
                strokeWidth = strokeWidth,
                dashLength = dashLength,
                dashGap = dashGap,
                color = color,
                cornerRadius = cornerRadius,
                view = view
            )
        }
    }
}

class CustomDashedBorder(
    private val color: Int = Color.BLACK,
    private val strokeWidth: Float = 4f,
    private val dashLength: Float = 20f,
    private val dashGap: Float = 10f,
    private val cornerRadius: Float = 0f,
    private val view: View? = null
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        this.color = this@CustomDashedBorder.color
        this.strokeWidth = this@CustomDashedBorder.strokeWidth
        strokeCap = Paint.Cap.SQUARE
    }

    private val paths = Array(4) { Path() }
    private val effects = Array<PathEffect?>(4) { null }
    private var boundsRect: RectF = RectF()

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        boundsRect = RectF(bounds)
        boundsRect.inset(strokeWidth / 2, strokeWidth / 2)
        updatePathsAndEffects()
    }

    private fun updatePathsAndEffects() {
        val effectiveRadius = min(cornerRadius, min(boundsRect.width(), boundsRect.height()))

        val sides = arrayOf(
            Side(
                boundsRect.left + effectiveRadius,
                boundsRect.top,
                boundsRect.right - effectiveRadius,
                boundsRect.top,
                boundsRect.width() - 2 * effectiveRadius
            ),
            Side(
                boundsRect.right,
                boundsRect.top + effectiveRadius,
                boundsRect.right,
                boundsRect.bottom - effectiveRadius,
                boundsRect.height() - 2 * effectiveRadius
            ),
            Side(
                boundsRect.right - effectiveRadius,
                boundsRect.bottom,
                boundsRect.left + effectiveRadius,
                boundsRect.bottom,
                boundsRect.width() - 2 * effectiveRadius
            ),
            Side(
                boundsRect.left,
                boundsRect.bottom - effectiveRadius,
                boundsRect.left,
                boundsRect.top + effectiveRadius,
                boundsRect.height() - 2 * effectiveRadius
            )
        )

        sides.forEachIndexed { index, side ->
            paths[index].reset()
            paths[index].moveTo(side.startX, side.startY)
            paths[index].lineTo(side.endX, side.endY)

            effects[index] = calculatePerfectEffectForSide(side.length)
        }
    }

    private fun calculatePerfectEffectForSide(sideLength: Float): PathEffect? {
        if (sideLength <= 0) return null

        val patternLength = dashLength + dashGap
        val fullPatterns = max(1, floor(sideLength / patternLength).toInt())

        // Рассчитываем скорректированную длину штриха
        val adjustedDash = (sideLength - (fullPatterns - 1) * dashGap) / fullPatterns

        // Гарантируем, что штрих не будет меньше 1px
        val finalDash = max(1f, adjustedDash)

        return DashPathEffect(floatArrayOf(finalDash, dashGap), 0f)
    }

    override fun draw(canvas: Canvas) {
        if (view != null) {
            view.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(
                        0,
                        0,
                        view.width,
                        view.height,
                        cornerRadius
                    )
                }
            }

            view.clipToOutline = true
        }


        if (cornerRadius > 0) {
            // Рисуем скругленные углы отдельно
            drawRoundedCorners(canvas)
        }

        // Рисуем прямые стороны с пунктиром
        paths.forEachIndexed { index, path ->
            paint.pathEffect = effects[index]
            canvas.drawPath(path, paint)
        }
    }

    private fun drawRoundedCorners(canvas: Canvas) {
        val effectiveRadius = min(cornerRadius, min(boundsRect.width(), boundsRect.height()) / 2)
        val cornerPaint = Paint(paint).apply {
            pathEffect = null // Не используем пунктир для углов
        }

        // 4 угла
        val corners = arrayOf(
            // Левый верхний
            RectF(
                boundsRect.left,
                boundsRect.top,
                boundsRect.left + 2 * effectiveRadius,
                boundsRect.top + 2 * effectiveRadius
            ),
            // Правый верхний
            RectF(
                boundsRect.right - 2 * effectiveRadius,
                boundsRect.top,
                boundsRect.right,
                boundsRect.top + 2 * effectiveRadius
            ),
            // Правый нижний
            RectF(
                boundsRect.right - 2 * effectiveRadius,
                boundsRect.bottom - 2 * effectiveRadius,
                boundsRect.right,
                boundsRect.bottom
            ),
            // Левый нижний
            RectF(
                boundsRect.left,
                boundsRect.bottom - 2 * effectiveRadius,
                boundsRect.left + 2 * effectiveRadius,
                boundsRect.bottom
            )
        )

        corners.forEachIndexed { index, rect ->
            val arcs = listOf(180f, 270f, 0f, 90f)

            canvas.drawArc(rect, arcs[index], 90f, false, cornerPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    private data class Side(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float,
        val length: Float
    )
}