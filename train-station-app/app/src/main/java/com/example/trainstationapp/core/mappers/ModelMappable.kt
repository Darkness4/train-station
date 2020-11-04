package com.example.trainstationapp.core.mappers

interface ModelMappable<out R> {
    fun asModel(): R
}
