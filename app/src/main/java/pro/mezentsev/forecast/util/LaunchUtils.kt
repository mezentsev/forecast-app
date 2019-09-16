package pro.mezentsev.forecast.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) = startActivityForResult(newIntent<T>(this, init), requestCode, options)

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) = startActivity(newIntent<T>(this, init), options)

inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) = startActivityForResult(newIntent<T>(requireContext(), init), requestCode, options)

inline fun <reified T : Any> newIntent(
    context: Context,
    noinline init: Intent.() -> Unit = {}
) = Intent(context, T::class.java).apply(init)