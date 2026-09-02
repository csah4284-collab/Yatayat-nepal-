package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.PaymentMethod
import com.example.ui.theme.ESewaGreen
import com.example.ui.theme.ESewaGreenDark
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.KhaltiPurpleDark
import com.example.ui.theme.NepalCrimsonPrimary
import com.example.ui.theme.StatusSuccess
import com.example.util.LanguageManager
import kotlinx.coroutines.delay

@Composable
fun PaymentMethodSelectorSheet(
    selectedMethod: PaymentMethod,
    onMethodSelect: (PaymentMethod) -> Unit,
    language: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = LanguageManager.getString("payment_method", language),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaymentMethodChip(
                method = PaymentMethod.ESEWA,
                isSelected = selectedMethod == PaymentMethod.ESEWA,
                brandColor = ESewaGreen,
                onClick = { onMethodSelect(PaymentMethod.ESEWA) },
                modifier = Modifier.weight(1f)
            )
            PaymentMethodChip(
                method = PaymentMethod.KHALTI,
                isSelected = selectedMethod == PaymentMethod.KHALTI,
                brandColor = KhaltiPurple,
                onClick = { onMethodSelect(PaymentMethod.KHALTI) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaymentMethodChip(
                method = PaymentMethod.FONEPAY,
                isSelected = selectedMethod == PaymentMethod.FONEPAY,
                brandColor = FonepayRed,
                onClick = { onMethodSelect(PaymentMethod.FONEPAY) },
                modifier = Modifier.weight(1f)
            )
            PaymentMethodChip(
                method = PaymentMethod.CASH,
                isSelected = selectedMethod == PaymentMethod.CASH,
                brandColor = Color(0xFF1E293B),
                onClick = { onMethodSelect(PaymentMethod.CASH) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PaymentMethodChip(
    method: PaymentMethod,
    isSelected: Boolean,
    brandColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) brandColor else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(12.dp)
            ),
        color = if (isSelected) brandColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(brandColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (method) {
                        PaymentMethod.ESEWA -> "e"
                        PaymentMethod.KHALTI -> "K"
                        PaymentMethod.FONEPAY -> "f"
                        PaymentMethod.CASH -> "रू"
                        PaymentMethod.CARD -> "💳"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = method.brandName,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) brandColor else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ESewaPaymentGatewayDialog(
    amountNpr: Double,
    userPhone: String,
    onSuccess: (txnId: String) -> Unit,
    onDismiss: () -> Unit
) {
    var esewaId by remember { mutableStateOf(userPhone.replace("+977", "").trim()) }
    var mpin by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // eSewa Branded Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(ESewaGreen, ESewaGreenDark))
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "eSewa",
                                    color = ESewaGreenDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "eSewa Secure Checkout",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Nepal's 1st Payment Gateway",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                        if (!isProcessing) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }
                    }
                }

                // Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (isSuccess) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = ESewaGreen,
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "Payment of NPR $amountNpr Successful!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ESewaGreenDark
                        )
                        Text(
                            text = "Txn ID: ESW-${System.currentTimeMillis() % 10000000}-NP",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    } else {
                        // Amount Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Payable Amount:", color = Color(0xFF475569), fontSize = 13.sp)
                                Text(
                                    "NPR $amountNpr",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = ESewaGreenDark
                                )
                            }
                        }

                        OutlinedTextField(
                            value = esewaId,
                            onValueChange = { esewaId = it },
                            label = { Text("eSewa ID (Mobile Number)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = mpin,
                            onValueChange = { if (it.length <= 4) mpin = it },
                            label = { Text("eSewa 4-Digit MPIN / Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ESewaGreen) },
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                if (esewaId.length < 8) {
                                    errorMessage = "Please enter valid eSewa mobile number"
                                    return@Button
                                }
                                if (mpin.length < 4) {
                                    errorMessage = "Please enter your 4-digit MPIN"
                                    return@Button
                                }
                                isProcessing = true
                                errorMessage = ""
                            },
                            enabled = !isProcessing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ESewaGreen)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Pay NPR $amountNpr via eSewa", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (isProcessing && !isSuccess) {
        LaunchedEffect(Unit) {
            delay(1600)
            isProcessing = false
            isSuccess = true
            val txn = "ESW-${(10000000..99999999).random()}-NP"
            delay(1000)
            onSuccess(txn)
        }
    }
}

@Composable
fun KhaltiPaymentGatewayDialog(
    amountNpr: Double,
    userPhone: String,
    onSuccess: (txnId: String) -> Unit,
    onDismiss: () -> Unit
) {
    var khaltiNumber by remember { mutableStateOf(userPhone.replace("+977", "").trim()) }
    var khaltiPin by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Khalti Branded Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(KhaltiPurple, KhaltiPurpleDark))
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Khalti",
                                    color = KhaltiPurple,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Khalti Digital Wallet",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Instant 1-Click Payment",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                        if (!isProcessing) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }
                    }
                }

                // Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (isSuccess) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = KhaltiPurple,
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "Payment of NPR $amountNpr Successful!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = KhaltiPurpleDark
                        )
                        Text(
                            text = "Txn ID: KHL-${System.currentTimeMillis() % 10000000}-NP",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total Fare Amount:", color = Color(0xFF475569), fontSize = 13.sp)
                                Text(
                                    "NPR $amountNpr",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = KhaltiPurple
                                )
                            }
                        }

                        OutlinedTextField(
                            value = khaltiNumber,
                            onValueChange = { khaltiNumber = it },
                            label = { Text("Khalti Registered Mobile") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = khaltiPin,
                            onValueChange = { if (it.length <= 4) khaltiPin = it },
                            label = { Text("Khalti 4-Digit MPIN / OTP") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = KhaltiPurple) },
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                if (khaltiNumber.length < 8) {
                                    errorMessage = "Please enter valid Khalti number"
                                    return@Button
                                }
                                if (khaltiPin.length < 4) {
                                    errorMessage = "Please enter 4-digit Khalti MPIN"
                                    return@Button
                                }
                                isProcessing = true
                                errorMessage = ""
                            },
                            enabled = !isProcessing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KhaltiPurple)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Pay NPR $amountNpr with Khalti", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (isProcessing && !isSuccess) {
        LaunchedEffect(Unit) {
            delay(1600)
            isProcessing = false
            isSuccess = true
            val txn = "KHL-${(10000000..99999999).random()}-NP"
            delay(1000)
            onSuccess(txn)
        }
    }
}

@Composable
fun FonepayQrDialog(
    amountNpr: Double,
    onSuccess: (txnId: String) -> Unit,
    onDismiss: () -> Unit
) {
    var isProcessing by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Fonepay Interoperable QR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = FonepayRed
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(2.dp, FonepayRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = "Fonepay QR Code",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(140.dp)
                    )
                }

                Text(
                    text = "Scan with any Nepal Bank / Mobile Banking App",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Text(
                    text = "Amount: NPR $amountNpr",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = FonepayRed
                )

                Button(
                    onClick = {
                        isProcessing = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = FonepayRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Simulate Bank App Scan & Pay", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (isProcessing) {
        LaunchedEffect(Unit) {
            delay(1500)
            onSuccess("FNP-${(10000000..99999999).random()}-NP")
        }
    }
}
