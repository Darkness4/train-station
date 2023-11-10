package com.example.trainstationapp.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object JwtSerializer : Serializer<Session.Jwt> {
    override val defaultValue: Session.Jwt = Session.Jwt.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Session.Jwt {
        try {
            return Session.Jwt.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Session.Jwt, output: OutputStream) = t.writeTo(output)
}
