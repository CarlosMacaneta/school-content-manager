package com.cs.schoolcontentmanager.data.datasource

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.cs.schoolcontentmanager.Settings
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer: Serializer<Settings> {

    override val defaultValue: Settings = Settings.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): Settings =
        try {
            Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}