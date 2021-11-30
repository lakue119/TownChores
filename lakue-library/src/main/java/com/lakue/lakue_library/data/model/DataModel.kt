package com.lakue.lakue_library.data.model

interface DataModel<Domain> {
    fun toDomain(): Domain
}
