package com.example.trainstationapp.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object CodeVerifierSerializer : Serializer<Session.CodeVerifier> {
    override val defaultValue: Session.CodeVerifier = Session.CodeVerifier.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Session.CodeVerifier {
        try {
            return Session.CodeVerifier.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Session.CodeVerifier, output: OutputStream) = t.writeTo(output)
}
