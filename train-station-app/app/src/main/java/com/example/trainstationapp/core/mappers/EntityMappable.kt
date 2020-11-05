package com.example.trainstationapp.core.mappers

interface EntityMappable<out R> {
    fun asEntity(): R
}
