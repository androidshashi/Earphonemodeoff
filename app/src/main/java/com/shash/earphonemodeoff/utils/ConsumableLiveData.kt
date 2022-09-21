package com.shash.earphonemodeoff.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 *@author = Shashi
 *@date = 26/06/21
 *@description = This class handles
 */
class ConsumableLiveData<T>(var consume: Boolean = false) : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(
            owner,
            Observer<T> {
                if (consume) {
                    if (pending.compareAndSet(true, false)) observer.onChanged(it)
                } else {
                    observer.onChanged(it)
                }
            }
        )
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}