package com.libreriag.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        color = TextPrimary
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        color = TextSecondary
    )
)
