package com.example.pocketsongbook.common.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ParcelableArgument<TProperty : Parcelable>(
    private val argsKey: String
) : ReadWriteProperty<Fragment, TProperty> {
    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: TProperty) {
        val arguments = thisRef.run { arguments ?: Bundle().also { arguments = it } }
        arguments.putParcelable(argsKey, value)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): TProperty {
        return thisRef.requireArguments().getParcelable(argsKey)!!
    }
}