package com.lakue.lakue_library.data.model

interface RemoteModel<Data> {
    fun toData(): Data
}
