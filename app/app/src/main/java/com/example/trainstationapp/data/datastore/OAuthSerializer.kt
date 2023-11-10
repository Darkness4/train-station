package com.example.trainstationapp.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object OAuthSerializer : Serializer<Session.OAuth> {
    override val defaultValue: Session.OAuth = Session.OAuth.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Session.OAuth {
        try {
            return Session.OAuth.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Session.OAuth, output: OutputStream) = t.writeTo(output)
}
