package nl.mpcjanssen.simpletask

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

abstract class ThemedNoActionBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(TodoApplication.config.activeTheme)
        if (TodoApplication.config.forceEnglish) {
            val conf = resources.configuration
            conf.locale = Locale.ENGLISH
            resources.updateConfiguration(conf, resources.displayMetrics)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val content = findViewById<ViewGroup>(android.R.id.content)
        val topBar = (content.findViewById<View>(R.id.main_actionbar) as View?)
                ?: (content.findViewById<View>(R.id.toolbar_edit_filter) as View?)
        applySystemBarInsets(content, topBar, applyTopToRoot = topBar == null)
    }
}

abstract class ThemedActionBarActivity : AppCompatActivity() {

    // The window action bar overlaps the content under edge-to-edge, on every screen using it
    protected open val includeActionBarInContentInset = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(TodoApplication.config.activeActionBarTheme)
        if (TodoApplication.config.forceEnglish) {
            val conf = resources.configuration
            conf.locale = Locale.ENGLISH
            resources.updateConfiguration(conf, resources.displayMetrics)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        applySystemBarInsets(
                findViewById(android.R.id.content),
                null,
                applyTopToRoot = includeActionBarInContentInset,
                actionBarHeight = if (includeActionBarInContentInset) actionBarHeight(this) else 0
        )
        addStatusBarBackground(this)
    }
}

abstract class ThemedPreferenceActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(TodoApplication.config.activeActionBarTheme)
        if (TodoApplication.config.forceEnglish) {
            val conf = resources.configuration
            conf.locale = Locale.ENGLISH
            resources.updateConfiguration(conf, resources.displayMetrics)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        applySystemBarInsets(
                findViewById(android.R.id.content),
                null,
                applyTopToRoot = true,
                actionBarHeight = actionBarHeight(this)
        )
        addStatusBarBackground(this)
    }
}

// Android 15+ no longer paints the status bar with the theme's colorPrimaryDark, so screens
// using the window action bar showed the light window background behind white status icons.
// Draw that colour behind the status bar ourselves.
private fun addStatusBarBackground(activity: Activity) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        return
    }
    val attributes = activity.obtainStyledAttributes(intArrayOf(androidx.appcompat.R.attr.colorPrimaryDark))
    val color = attributes.getColor(0, 0)
    attributes.recycle()
    val decor = activity.window.decorView as ViewGroup
    val background = View(activity)
    background.setBackgroundColor(color)
    decor.addView(background, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, Gravity.TOP))
    ViewCompat.setOnApplyWindowInsetsListener(background) { view, insets ->
        val height = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        if (view.layoutParams.height != height) {
            view.layoutParams.height = height
            view.requestLayout()
        }
        insets
    }
}

private fun actionBarHeight(context: Context): Int {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        return 0
    }
    val attributes = context.obtainStyledAttributes(intArrayOf(androidx.appcompat.R.attr.actionBarSize))
    val height = attributes.getDimensionPixelSize(0, 0)
    attributes.recycle()
    return height
}

private fun applySystemBarInsets(
        root: ViewGroup,
        topBar: View?,
        applyTopToRoot: Boolean,
        actionBarHeight: Int = 0
) {
    val rootPaddingTop = root.paddingTop
    val rootPaddingBottom = root.paddingBottom
    val topBarPaddingTop = topBar?.paddingTop ?: 0

    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val topInset = if (applyTopToRoot) {
            val systemTopInset = insets.getSystemWindowInsets().top
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            maxOf(systemTopInset, statusBarInset + actionBarHeight)
        } else {
            0
        }
        // Include the keyboard on Android 15+: with forced edge-to-edge, adjustResize no longer
        // shrinks the window. Older versions still resize, so adding it there would double-pad.
        val bottomTypes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.ime()
        } else {
            WindowInsetsCompat.Type.navigationBars()
        }
        val navigationBarInset = insets.getInsets(bottomTypes).bottom
        view.setPadding(
                view.paddingLeft,
                rootPaddingTop + topInset,
                view.paddingRight,
                rootPaddingBottom + navigationBarInset
        )
        insets
    }
    topBar?.let { bar ->
        ViewCompat.setOnApplyWindowInsetsListener(bar) { view, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                    view.paddingLeft,
                    topBarPaddingTop + statusBarInset,
                    view.paddingRight,
                    view.paddingBottom
            )
            insets
        }
    }
    ViewCompat.requestApplyInsets(root)
}

