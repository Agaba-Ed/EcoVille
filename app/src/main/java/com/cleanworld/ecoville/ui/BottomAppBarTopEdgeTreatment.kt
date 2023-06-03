package com.cleanworld.ecoville.ui

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

/**
 * Top edge treatment for the bottom app bar which "cradles" a circular FloatingActionButton.
 * This edge features a downward semicircular cutout from the edge line.
 * The two corners created by the cutout can optionally be rounded.
 * The circular cutout can also support a vertically offset FloatingActionButton; i.e.,
 * the cutout need not be a perfect semicircle, but could be an arc of less than 180 degrees
 * that does not start or finish with a vertical path. This vertical offset must be positive
 */

class BottomAppBarTopEdgeTreatment(var fabMargin:Float=0f, var roundCornerRadius:Float=0f, var cradleVerticleOffset:Float=0f): EdgeTreatment(),Cloneable {


    private val fabDiameter = 0f
    var horizontalOffset = 0f
    private val fabCornerSize = -1f

    companion object {
        private const val ARC_QUARTER = 90
        private const val ARC_HALF = 180
        private const val ANGLE_UP = 270
        private const val ANGLE_LEFT = 180
        private const val ROUNDED_CORNER_FAB_OFFSET = 1.75f
    }

    override fun getEdgePath(length: Float, center: Float, interpolation: Float,
                             shapePath: ShapePath
    ) {
        if(fabMargin==0f) {
            //There is no cutout to draw
            shapePath.lineTo(length, 0f)
            return
        }

        val cradleDiameter = fabMargin * 2 + fabDiameter
        val cradleRadius = cradleDiameter / 2f
        val roundedCornerOffset=interpolation*roundCornerRadius
        val middle=center+horizontalOffset


        // The center offset of the cutout tweens between the vertical offset when attached, and the
        // cradleRadius as it becomes detached.
        var verticalOffset = interpolation * cradleVerticleOffset+(1-interpolation)*cradleRadius
        val verticalOffsetRatio = verticalOffset / cradleRadius

        if (verticalOffsetRatio >= 1) {
            // The cutout is positioned at the top of the FAB
            shapePath.lineTo(length, 0f)
            return
        }

        // Calculate the path of the cutout by calculating the location of two adjacent circles. One
        // circle is for the rounded corner. If the rounded corner circle radius is 0 the corner will
        // not be rounded. The other circle is the cutout.

        // Calculate the X distance between the center of the two adjacent circles using pythagorean
        // theorem.

        val cornerSize=fabCornerSize* interpolation
        val useCircleCutout = fabCornerSize == -1f || abs(fabCornerSize * 2f - fabDiameter) < .1f
        var arcOffset = 0f
        if (!useCircleCutout) {
            verticalOffset = 0f
            arcOffset = ROUNDED_CORNER_FAB_OFFSET
        }

        val distanceBetweenCenters = cradleRadius + roundedCornerOffset
        val distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters
        val distanceY = verticalOffset + roundedCornerOffset
        val distanceX = sqrt((distanceBetweenCentersSquared - distanceY * distanceY).toDouble()).toFloat()

        // Calculate the x position of the rounded corner circles.

        // Calculate the x position of the rounded corner circles.
        val leftRoundedCornerCircleX = middle - distanceX
        val rightRoundedCornerCircleX = middle + distanceX

        // Calculate the arc between the center of the two circles.

        // Calculate the arc between the center of the two circles.
        val cornerRadiusArcLength = Math.toDegrees(atan((distanceX / distanceY).toDouble())).toFloat()
        val cutoutArcOffset = ARC_QUARTER - cornerRadiusArcLength + arcOffset

        // Draw the starting line up to the left rounded corner.

        // Draw the starting line up to the left rounded corner.
        shapePath.lineTo( /* x = */leftRoundedCornerCircleX, 0f)

        // Draw the arc for the left rounded corner circle. The bounding box is the area around the
        // circle's center which is at `(leftRoundedCornerCircleX, roundedCornerOffset)`.

        // Draw the arc for the left rounded corner circle. The bounding box is the area around the
        // circle's center which is at `(leftRoundedCornerCircleX, roundedCornerOffset)`.
        shapePath.addArc( /* left = */
            leftRoundedCornerCircleX - roundedCornerOffset, 0f,  /* right = */
            leftRoundedCornerCircleX + roundedCornerOffset,  /* bottom = */
            roundedCornerOffset * 2,  /* startAngle = */
            ANGLE_UP.toFloat(),  /* sweepAngle = */
            cornerRadiusArcLength
        )

        if (useCircleCutout) {
            // Draw the cutout circle.
            shapePath.addArc( /* left = */
                middle - cradleRadius,  /* top = */
                -cradleRadius - verticalOffset,  /* right = */
                middle + cradleRadius,  /* bottom = */
                cradleRadius - verticalOffset,  /* startAngle = */
                ANGLE_LEFT - cutoutArcOffset,  /* sweepAngle = */
                cutoutArcOffset * 2 - ARC_HALF
            )
        } else {
            val cutoutDiameter = fabMargin + cornerSize * 2f
            shapePath.addArc( /* left = */
                middle - cradleRadius,  /* top = */
                -(cornerSize + fabMargin),  /* right = */
                middle - cradleRadius + cutoutDiameter,  /* bottom = */
                fabMargin + cornerSize,  /* startAngle = */
                ANGLE_LEFT - cutoutArcOffset,  /* sweepAngle = */
                (cutoutArcOffset * 2 - ARC_HALF) / 2f
            )
            shapePath.lineTo(
                middle + cradleRadius - (cornerSize + fabMargin / 2f),  /* y = */
                cornerSize + fabMargin
            )
            shapePath.addArc( /* left = */
                middle + cradleRadius - (cornerSize * 2f + fabMargin),  /* top = */
                -(cornerSize + fabMargin),  /* right = */
                middle + cradleRadius,  /* bottom = */
                fabMargin + cornerSize, 90f,  /* sweepAngle = */
                -90 + cutoutArcOffset
            )
        }

        // Draw an arc for the right rounded corner circle. The bounding box is the area around the
        // circle's center which is at `(rightRoundedCornerCircleX, roundedCornerOffset)`.

        // Draw an arc for the right rounded corner circle. The bounding box is the area around the
        // circle's center which is at `(rightRoundedCornerCircleX, roundedCornerOffset)`.
        shapePath.addArc( /* left = */
            rightRoundedCornerCircleX - roundedCornerOffset, 0f,  /* right = */
            rightRoundedCornerCircleX + roundedCornerOffset,  /* bottom = */
            roundedCornerOffset * 2,  /* startAngle = */
            ANGLE_UP - cornerRadiusArcLength,  /* sweepAngle = */
            cornerRadiusArcLength
        )

        // Draw the ending line after the right rounded corner.

        // Draw the ending line after the right rounded corner.
        shapePath.lineTo( /* x = */length, 0f)



    }





}
