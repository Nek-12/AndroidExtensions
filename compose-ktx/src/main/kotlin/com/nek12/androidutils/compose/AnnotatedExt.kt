package com.nek12.androidutils.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

@Deprecated(Migration)
fun String.span(spanStyle: SpanStyle) = buildAnnotatedString {
    withStyle(spanStyle) {
        append(this@span)
    }
}

@Deprecated(Migration)
fun String.bold() = span(SpanStyle(fontWeight = FontWeight.Bold))

@Deprecated(Migration)
fun String.italic() = span(SpanStyle(fontStyle = FontStyle.Italic))

@Deprecated(Migration)
fun String.strike() = span(SpanStyle(textDecoration = TextDecoration.LineThrough))

@Deprecated(Migration)
fun String.underline() = span(SpanStyle(textDecoration = TextDecoration.Underline))

@Deprecated(Migration)
fun String.decorate(vararg decorations: TextDecoration) =
    span(SpanStyle(textDecoration = TextDecoration.combine(decorations.toList())))

@Deprecated(Migration)
fun String.size(size: TextUnit) = span(SpanStyle(fontSize = size))

@Deprecated(Migration)
fun String.background(color: Color) = span(SpanStyle(background = color))

@Deprecated(Migration)
fun String.color(color: Color) = span(SpanStyle(color = color))

@Deprecated(Migration)
fun String.weight(weight: FontWeight) = span(SpanStyle(fontWeight = weight))

@Deprecated(Migration)

fun String.style(style: FontStyle) = span(SpanStyle(fontStyle = style))

@Deprecated(Migration)
fun String.shadow(color: Color, offset: Offset = Offset.Zero, blurRadius: Float = 0.0f) =
    span(SpanStyle(shadow = Shadow(color, offset, blurRadius)))

@Deprecated(Migration)
fun String.fontFamily(fontFamily: FontFamily) = span(SpanStyle(fontFamily = fontFamily))
