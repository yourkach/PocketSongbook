package com.ybond.core_entities.models

data class ChangeableOption<T>(
    val selectedValue: T,
    val isDefault: Boolean
)