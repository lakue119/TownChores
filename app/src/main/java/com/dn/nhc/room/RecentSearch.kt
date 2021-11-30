package com.dn.nhc.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentSearch(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var keyword: String
)
