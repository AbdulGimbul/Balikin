package dev.balikin.poject

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashView ->
            val icon = splashView.iconView

            val distanceY = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                -500f,
                icon.resources.displayMetrics
            )

            val scaleX = ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 50f)
            val scaleY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 50f)
            val translateY = ObjectAnimator.ofFloat(icon, View.TRANSLATION_Y, 0f, distanceY)

            AnimatorSet().apply {
                playTogether(scaleX, scaleY, translateY)
                duration = 1000L
                interpolator = DecelerateInterpolator() // EASE-OUT
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator) {
                        splashView.remove()
                    }
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                start()
            }
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}