package com.cmt.openapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp



// Tipografía Normal
val NormalTypography = Typography(
    headlineSmall = TextStyle(fontSize = 12.sp, lineHeight = 13.sp), // Título sección detalle incidente
    bodySmall = TextStyle(fontSize = 14.sp, lineHeight = 16.sp), // Cuerpo tarjeta incidente preview
    displaySmall = TextStyle(fontSize = 14.sp, lineHeight = 16.sp, fontWeight = FontWeight.ExtraBold), // Texto acceso solicitud
    titleSmall = TextStyle(fontSize = 16.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold), // Encabezado solicitud
    titleLarge = TextStyle(fontSize = 18.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold), // Título pantalla inicio
    labelLarge = TextStyle(fontSize = 20.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold) // Botón
)

// Tipografía Mediana
val MediumTypography = Typography(
    headlineSmall = TextStyle(fontSize = 13.sp, lineHeight = 14.sp), // +1sp
    bodySmall = TextStyle(fontSize = 15.sp, lineHeight = 17.sp), // +1sp
    displaySmall = TextStyle(fontSize = 16.sp, lineHeight = 18.sp, fontWeight = FontWeight.ExtraBold), // +2sp
    titleSmall = TextStyle(fontSize = 17.sp, lineHeight = 18.sp, fontWeight = FontWeight.Bold), // +1sp
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold), // +2sp
    labelLarge = TextStyle(fontSize = 22.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold) // +2sp
)

// Tipografía Grande
val LargeTypography = Typography(
    headlineSmall = TextStyle(fontSize = 14.sp, lineHeight = 15.sp), // +2sp
    bodySmall = TextStyle(fontSize = 16.sp, lineHeight = 18.sp), // +2sp
    displaySmall = TextStyle(fontSize = 18.sp, lineHeight = 20.sp, fontWeight = FontWeight.ExtraBold), // +4sp
    titleSmall = TextStyle(fontSize = 19.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold), // +3sp
    titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold), // +4sp
    labelLarge = TextStyle(fontSize = 24.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold) // +4sp
)