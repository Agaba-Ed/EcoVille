package com.cleanworld.ecoville.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.animation.TransformationCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel


class BottomAppBar @JvmOverloads constructor(var ctx:Context,var attrs:AttributeSet?=null,var defStyleAttr:Int=0):Toolbar(ctx,attrs,defStyleAttr) {

    private var navigationIconTint: Int = 0
    private val materialShapeDrawable = MaterialShapeDrawable()
    private var modeAnimator: Animator? = null
    private var menuAnimator: Animator? = null

    /** Keeps track of currently running animations. */
    private var runningAnimations: Int = 0
    private var animationListeners: ArrayList<AnimationListener>? = null


    @MenuRes
    private val pendingMenuResId = NO_MENU_RES_ID


    /** Callback to be invoked when the BottomAppBar is animating. */
    internal open interface AnimationListener {
        fun onAnimationStart(bar: BottomAppBar?)
        fun onAnimationEnd(bar: BottomAppBar?)
    }


    companion object {
        private const val ANIMATION_DURATION = 300L
        private const val NO_MENU_RES_ID = 0
    }

    /**
     * If the FloatingActionButton is actually cradled in the BottomAppBar
     */
    private var isFabCradled: Boolean = true


    private var bottomInset: Int = 0
    private var rightInset: Int = 0
    private var leftInset: Int = 0

    /**
     * Listens to any transformations to the FAB so the cutout can react to the change.
     */

    private val fabTransformationCallback = object : TransformationCallback<FloatingActionButton> {
        override fun onTranslationChanged(fab: FloatingActionButton) {

            materialShapeDrawable?.interpolation = if (fab.visibility == View.VISIBLE) fab.scaleY else 0f

        }

        override fun onScaleChanged(fab: FloatingActionButton) {
            val horizontalOffset = fab.translationX
            if (getTopEdgeTreatment().horizontalOffset != horizontalOffset) {
                getTopEdgeTreatment().horizontalOffset = horizontalOffset
                materialShapeDrawable!!.invalidateSelf()
            }


            // If the translation of the fab has changed, update the vertical offset.
            val verticalOffset = Math.max(0f, -fab.translationY)
            if (getTopEdgeTreatment().cradleVerticleOffset != verticalOffset) {
                getTopEdgeTreatment().cradleVerticleOffset = verticalOffset
                materialShapeDrawable!!.invalidateSelf()
            }
            materialShapeDrawable!!.interpolation = if (fab.visibility == VISIBLE) fab.scaleY else 0f
        }

    }

    private fun getTopEdgeTreatment(): BottomAppBarTopEdgeTreatment {
        return materialShapeDrawable!!.shapeAppearanceModel.topEdge as BottomAppBarTopEdgeTreatment
    }

    init {

        ctx = context

        var fabCradleMargin = 10f
        var fabCornerRadius = 10f
        var fabVerticalOffset = 10f


        val topEdgeTreatment: EdgeTreatment = BottomAppBarTopEdgeTreatment(
            fabCradleMargin,
            fabCornerRadius,
            fabVerticalOffset
        )
        val shapeAppearanceModel = ShapeAppearanceModel.builder().setTopEdge(topEdgeTreatment).build()
        materialShapeDrawable!!.shapeAppearanceModel = shapeAppearanceModel
        materialShapeDrawable!!.shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
        materialShapeDrawable!!.paintStyle = Paint.Style.FILL
        materialShapeDrawable!!.initializeElevationOverlay(context)
        setElevation(elevation.toFloat())
        ViewCompat.setBackground(this, materialShapeDrawable)
    }


    fun findDependentView():View?{

        if (parent !is CoordinatorLayout) {
            // If we aren't in a CoordinatorLayout we won't have a dependent FAB.
            return null
        }

        val dependencies = (parent as CoordinatorLayout).getDependencies(this)
        for (i in dependencies.indices) {
            val dependency = dependencies[i]
            if (dependency is FloatingActionButton) {
                return dependency
            }
        }
        return null
    }

    fun findDependentFab(): FloatingActionButton? {

        val view: View? = findDependentView()
        return if (view is FloatingActionButton) view else null
    }


    private fun maybeAnimateMenuView( newFabAttached: Boolean) {

        var newFabAttached = newFabAttached
        if (!ViewCompat.isLaidOut(this)) {
            // If this method is called before the BottomAppBar is laid out and able to animate, make sure
            // the desired menu is still set even if the animation is skipped.
            replaceMenu(pendingMenuResId)
            return
        }
        if (menuAnimator != null) {
            menuAnimator!!.cancel()
        }
        val animators: List<Animator> = java.util.ArrayList()

        // If there's no visible FAB, treat the animation like the FAB is going away.
        if (!isFabVisibleOrWillBeShown()) {
            newFabAttached = false
        }
        createMenuViewTranslationAnimation( newFabAttached, animators)
        val set = AnimatorSet()
        set.playTogether(animators)
        menuAnimator = set
        (menuAnimator as AnimatorSet).addListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    dispatchAnimationStart()
                }

                override fun onAnimationEnd(animation: Animator) {
                    dispatchAnimationEnd()
                    menuAnimator = null
                }
            })
        (menuAnimator as AnimatorSet).start()
    }

    private fun dispatchAnimationEnd() {

    }

    private fun dispatchAnimationStart() {

    }

    private fun createMenuViewTranslationAnimation(targetAttached: Boolean, animators: List<Animator>) {
        val actionMenuView: ActionMenuView = getActionMenuView() ?: return

        // Stop if there is no action menu view to animate

        // Stop if there is no action menu view to animate

        val fadeIn: Animator = ObjectAnimator.ofFloat(actionMenuView, "alpha", 1f)

        val translationXDifference: Float = (actionMenuView.translationX
                - getActionMenuViewTranslationX(actionMenuView, targetAttached))

        // If the MenuView has moved at least a pixel we will need to animate it.

        // If the MenuView has moved at least a pixel we will need to animate it.
        if (Math.abs(translationXDifference) > 1) {
            // We need to fade the MenuView out and in because it's position is changing
            val fadeOut: Animator = ObjectAnimator.ofFloat(actionMenuView, "alpha", 0f)
            fadeOut.addListener(
                object : AnimatorListenerAdapter() {
                    var cancelled = false
                    override fun onAnimationCancel(animation: Animator) {
                        cancelled = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (!cancelled) {
                            val replaced =
                                pendingMenuResId != NO_MENU_RES_ID
                            replaceMenu(pendingMenuResId)
                            translateActionMenuView(actionMenuView, targetAttached, replaced)
                        }
                    }
                })
            val set = AnimatorSet()
            set.duration = ANIMATION_DURATION / 2
            set.playSequentially(fadeOut, fadeIn)
            //animators.add(set)
        } else if (actionMenuView.alpha < 1) {
            // If the previous animation was cancelled in the middle and now we're deciding we don't need
            // fade the MenuView away and back in, we need to ensure the MenuView is visible
           // animators.add(fadeIn)
        }


    }

    private fun translateActionMenuView(actionMenuView: ActionMenuView, targetAttached: Boolean, replaced: Boolean) {

    }

    private fun getActionMenuViewTranslationX(actionMenuView: ActionMenuView, targetAttached: Boolean): Byte {
        TODO("Not yet implemented")
    }


    private fun isFabVisibleOrWillBeShown(): Boolean {
        val fab = findDependentFab()
        return fab != null && fab.visibility == View.VISIBLE
    }

    private fun replaceMenu(pendingMenuResId: Int) {}

    private fun createFabTranslationXAnimation( animators: MutableList<Animator>
    ) {
        val animator = ObjectAnimator.ofFloat(findDependentFab(), "translationX", getFabTranslationX())
        ANIMATION_DURATION
        animators.add(animator)
    }

    private fun getFabTranslationX(): Float {

        return 0f
    }


    private fun getActionMenuView(): ActionMenuView? {
        for (i in 0 until getChildCount()) {
            val view = getChildAt(i)
            if (view is ActionMenuView) {
                return view
            }
        }
        return null
    }


}









