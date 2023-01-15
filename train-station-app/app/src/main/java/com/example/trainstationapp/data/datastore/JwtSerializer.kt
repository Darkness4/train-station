package com.example.trainstationapp.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object JwtSerializer : Serializer<JwtOuterClass.Jwt> {
    override val defaultValue: JwtOuterClass.Jwt = JwtOuterClass.Jwt.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): JwtOuterClass.Jwt {
        try {
            return JwtOuterClass.Jwt.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: JwtOuterClass.Jwt, output: OutputStream) = t.writeTo(output)
}
