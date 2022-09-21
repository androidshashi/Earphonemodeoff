package com.shash.earphonemodeoff.utils.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains Fragment extension functions
 */

private const val TAG = "FragmentExtensions"

fun Fragment.showToast(text: String?) {
    Toast.makeText(requireContext(), text?:"", Toast.LENGTH_SHORT).show()
}

