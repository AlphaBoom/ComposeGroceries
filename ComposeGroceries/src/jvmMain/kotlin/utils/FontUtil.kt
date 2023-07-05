package utils

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

enum class SupportFontFamilyType {
    AmorieModella,
    Clarendon,
    RoswellTwoItc,
    WarnockPro,
}

fun loadFont(type: SupportFontFamilyType): FontFamily {
    return when (type) {
        SupportFontFamilyType.AmorieModella -> FontFamily(
            Font("Amorie-Modella-Bold.ttf", weight = FontWeight.Bold),
            Font("Amorie-Modella-Light.ttf", weight = FontWeight.Light),
            Font("Amorie-Modella-Medium.ttf", weight = FontWeight.Medium)
        )

        SupportFontFamilyType.Clarendon -> FontFamily(Font("ClarendonBT.ttf"))
        SupportFontFamilyType.RoswellTwoItc -> FontFamily(Font("roswell_two_itc.ttf"))
        SupportFontFamilyType.WarnockPro -> FontFamily(
            Font("Warnock Pro Light Caption.otf", style = FontStyle.Normal),
            Font("Warnock Pro Light Italic Display.otf", style = FontStyle.Italic)
        )
    }
}