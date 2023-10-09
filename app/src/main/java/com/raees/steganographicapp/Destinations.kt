package com.raees.steganographicapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface Destinations {
    val route: String
}

object Home:Destinations {
    override val route = "Home"

}

object Import:Destinations {
    override val route = "Import"

}

object EmbedText:Destinations {
    override val route = "Embed"

}

object ViewImage:Destinations {
    override val route = "ViewImage"
    val argImageId= "argImageId"
}