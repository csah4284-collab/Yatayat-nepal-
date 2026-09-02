package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.NepalLocation
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.NepalGoldSecondary
import com.example.ui.theme.StatusSuccess

@Composable
fun NepalMapCanvas(
    pickupLocation: NepalLocation?,
    dropLocation: NepalLocation?,
    tripProgress: Float, // 0.0f to 1.0f
    vehicleType: String,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true
) {
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "map_pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE5E9F0))
            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(20.dp))
            .pointerInput(isInteractive) {
                if (isInteractive) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.7f, 3.0f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val centerX = (w / 2f) + offsetX
            val centerY = (h / 2f) + offsetY

            // 1. Draw Background terrain & Kathmandu Valley contours
            drawRect(color = Color(0xFFF1F5F9))

            // Draw Mountainous Hills in background
            val hillPath = Path().apply {
                moveTo(0f, 0f)
                cubicTo(w * 0.3f, 40f, w * 0.7f, 20f, w, 80f)
                lineTo(w, 0f)
                close()
            }
            drawPath(path = hillPath, color = Color(0xFFE2E8F0).copy(alpha = 0.6f))

            // 2. Draw Bagmati & Bishnumati River curve
            val riverPath = Path().apply {
                moveTo(w * 0.15f, 0f)
                cubicTo(w * 0.35f, h * 0.3f, w * 0.25f, h * 0.65f, w * 0.6f, h)
            }
            drawPath(
                path = riverPath,
                color = Color(0xFF93C5FD),
                style = Stroke(width = 16f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // 3. Draw Kathmandu Ring Road & Major Arterial Roads
            // Ring Road Loop
            drawOval(
                color = Color(0xFFCBD5E1),
                topLeft = Offset(centerX - (w * 0.42f * scale), centerY - (h * 0.36f * scale)),
                size = androidx.compose.ui.geometry.Size(w * 0.84f * scale, h * 0.72f * scale),
                style = Stroke(width = 14f * scale)
            )
            drawOval(
                color = Color(0xFFFFFFFF),
                topLeft = Offset(centerX - (w * 0.42f * scale), centerY - (h * 0.36f * scale)),
                size = androidx.compose.ui.geometry.Size(w * 0.84f * scale, h * 0.72f * scale),
                style = Stroke(width = 8f * scale)
            )

            // Cross Roads / Prithvi Highway / Araniko Highway
            val mainRoad1 = Path().apply {
                moveTo(0f, centerY - 20f)
                lineTo(w, centerY + 40f)
            }
            drawPath(
                path = mainRoad1,
                color = Color.White,
                style = Stroke(width = 10f * scale, cap = StrokeCap.Round)
            )

            val mainRoad2 = Path().apply {
                moveTo(centerX - 40f, 0f)
                lineTo(centerX + 60f, h)
            }
            drawPath(
                path = mainRoad2,
                color = Color.White,
                style = Stroke(width = 10f * scale, cap = StrokeCap.Round)
            )

            // Grid / secondary street lines
            for (i in -3..3) {
                val lineOffset = i * 80f * scale
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(0f, centerY + lineOffset),
                    end = Offset(w, centerY + lineOffset),
                    strokeWidth = 3f * scale
                )
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(centerX + lineOffset, 0f),
                    end = Offset(centerX + lineOffset, h),
                    strokeWidth = 3f * scale
                )
            }

            // 4. Calculate Pickup and Drop Screen Coordinates
            val pX = if (pickupLocation != null) {
                centerX - (w * 0.22f * scale)
            } else {
                centerX - 60f
            }
            val pY = if (pickupLocation != null) {
                centerY - (h * 0.15f * scale)
            } else {
                centerY - 50f
            }

            val dX = if (dropLocation != null) {
                centerX + (w * 0.28f * scale)
            } else {
                centerX + 120f
            }
            val dY = if (dropLocation != null) {
                centerY + (h * 0.20f * scale)
            } else {
                centerY + 100f
            }

            // 5. Draw Route Path Polyline (Bezier with intermediate turn points)
            val routePath = Path().apply {
                moveTo(pX, pY)
                val midX1 = pX + (dX - pX) * 0.4f
                val midY1 = pY + 20f
                val midX2 = pX + (dX - pX) * 0.7f
                val midY2 = dY - 30f
                cubicTo(midX1, midY1, midX2, midY2, dX, dY)
            }

            // Route Shadow/Border
            drawPath(
                path = routePath,
                color = NepalCrimsonPrimary.copy(alpha = 0.3f),
                style = Stroke(
                    width = 12f * scale,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Dynamic dashed/solid active route
            drawPath(
                path = routePath,
                color = NepalCrimsonPrimary,
                style = Stroke(
                    width = 6f * scale,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), 0f)
                )
            )

            // 6. Draw Pickup Pin (Green)
            drawCircle(
                color = StatusSuccess.copy(alpha = pulseAlpha),
                radius = pulseRadius * 2f * scale,
                center = Offset(pX, pY)
            )
            drawCircle(
                color = Color.White,
                radius = 14f * scale,
                center = Offset(pX, pY)
            )
            drawCircle(
                color = StatusSuccess,
                radius = 10f * scale,
                center = Offset(pX, pY)
            )

            // 7. Draw Destination Pin (Nepal Crimson)
            drawCircle(
                color = NepalCrimsonPrimary.copy(alpha = 0.4f),
                radius = 18f * scale,
                center = Offset(dX, dY)
            )
            drawCircle(
                color = Color.White,
                radius = 14f * scale,
                center = Offset(dX, dY)
            )
            drawCircle(
                color = NepalCrimsonPrimary,
                radius = 10f * scale,
                center = Offset(dX, dY)
            )

            // 8. Vehicle Position & Animation along Route
            val currentProg = tripProgress.coerceIn(0.0f, 1.0f)
            val currentVehX = pX + (dX - pX) * currentProg
            val currentVehY = pY + (dY - pY) * currentProg

            // Vehicle Glow Beacon
            drawCircle(
                color = NepalGoldSecondary.copy(alpha = pulseAlpha),
                radius = (16f + pulseRadius) * scale,
                center = Offset(currentVehX, currentVehY)
            )

            // Draw Vehicle Indicator Box with rotation
            val angle = if (dX != pX) {
                Math.toDegrees(Math.atan2((dY - pY).toDouble(), (dX - pX).toDouble())).toFloat()
            } else 0f

            rotate(degrees = angle, pivot = Offset(currentVehX, currentVehY)) {
                drawRoundRect(
                    color = Color(0xFF0F172A),
                    topLeft = Offset(currentVehX - 16f * scale, currentVehY - 10f * scale),
                    size = androidx.compose.ui.geometry.Size(32f * scale, 20f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f * scale, 6f * scale)
                )
                drawRoundRect(
                    color = NepalGoldSecondary,
                    topLeft = Offset(currentVehX - 12f * scale, currentVehY - 7f * scale),
                    size = androidx.compose.ui.geometry.Size(24f * scale, 14f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale, 4f * scale)
                )
                // Headlight beams
                drawCircle(
                    color = Color.White,
                    radius = 3f * scale,
                    center = Offset(currentVehX + 12f * scale, currentVehY - 4f * scale)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3f * scale,
                    center = Offset(currentVehX + 12f * scale, currentVehY + 4f * scale)
                )
            }
        }

        // Map Overlay Controls (Zoom In, Zoom Out, Reset Center)
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                IconButton(
                    onClick = { scale = (scale + 0.3f).coerceAtMost(3.0f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(
                    onClick = { scale = (scale - 0.3f).coerceAtLeast(0.7f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(
                    onClick = {
                        scale = 1.0f
                        offsetX = 0f
                        offsetY = 0f
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Center Map", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Live GPS Badge
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF0F172A).copy(alpha = 0.88f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(StatusSuccess)
                )
                Text(
                    text = " GPS Live: Kathmandu Valley",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
