package nl.project.michaelmunatsi.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalDim = compositionLocalOf { Dimensions() }

data class Dimensions(
    val default: Dp = 0.dp,
    val dp_200: Dp = 200.dp,
    val dp_16: Dp = 16.dp,
    val dp_15: Dp = 15.dp,
    val dp_10: Dp = 10.dp,
    val dp_20: Dp = 20.dp,
    val dp_40: Dp = 40.dp,
    val dp_25: Dp = 25.dp,
    val sp_20: TextUnit = 20.sp
)

