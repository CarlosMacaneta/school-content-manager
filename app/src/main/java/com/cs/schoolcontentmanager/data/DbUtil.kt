package com.cs.schoolcontentmanager.data

import com.google.firebase.storage.ktx.storageMetadata

object DbUtil {

    fun setMetadata(
        contentType_: String? = null,
        cacheControl_: String? = null,
        contentEncoding_: String? = null,
        contentLanguage_: String? = null,
        contentDisposition_: String? = null
    ) = storageMetadata {
        contentType = contentType_
        cacheControl = cacheControl_
        contentEncoding = contentEncoding_
        contentLanguage = contentLanguage_
        contentDisposition = contentDisposition_
    }

    fun setCustomMetadata(
        contentType_: String? = null,
        cacheControl_: String? = null,
        contentEncoding_: String? = null,
        contentLanguage_: String? = null,
        contentDisposition_: String? = null,
        customMetadata: MutableMap<String, String> = mutableMapOf()
    ) = storageMetadata {
        contentType = contentType_
        cacheControl = cacheControl_
        contentEncoding = contentEncoding_
        contentLanguage = contentLanguage_
        contentDisposition = contentDisposition_

        customMetadata.keys.forEach { key ->
            setCustomMetadata(key, customMetadata[key])
        }
    }

}