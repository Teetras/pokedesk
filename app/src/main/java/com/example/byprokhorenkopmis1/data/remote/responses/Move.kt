package com.example.byprokhorenkopmis1.data.remote.responses

import kotlinx.serialization.SerialName

data class Move(
    val move: MoveX,
    @SerialName("version-group-details")
    val version_group_details: List<VersionGroupDetail>
)