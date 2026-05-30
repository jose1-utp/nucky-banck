package com.example.proyect.nucky_banck.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.activity.compose.BackHandler
import com.example.proyect.nucky_banck.ui.theme.BorderGray
import com.example.proyect.nucky_banck.ui.theme.DeepBlue
import com.example.proyect.nucky_banck.ui.theme.Emerald
import com.example.proyect.nucky_banck.ui.theme.NavyBlue
import com.example.proyect.nucky_banck.ui.theme.TextDark
import com.example.proyect.nucky_banck.ui.theme.TextGray
import com.example.proyect.nucky_banck.ui.theme.White

@Composable
fun HomeView(
    cedula: String,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.loadUserData(cedula)
    }
    // para no permitir volver atrás al login
    BackHandler {
        // No hace nada
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(NavyBlue, DeepBlue)
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Barra superior con saludo y botón de cerrar sesión
            TopBar(
                nombre   = uiState.fullName,
                onLogout = onLogout
            )

            // Card con el saldo y las acciones
            HomeCard(
                saldo        = uiState.saldo,
                cedula       = cedula,
                navController = navController
            )
        }
    }
}


@Composable
private fun TopBar(nombre: String, onLogout: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 56.dp, start = 24.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text  = "Bienvenido de nuevo",
                color = White.copy(alpha = 0.75f),
                fontSize = 14.sp
            )
            Text(
                text       = nombre,
                color      = White,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(onClick = onLogout) {
            Icon(
                imageVector        = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint               = White
            )
        }
    }
}


@Composable
private fun HomeCard(
    saldo: Double,
    cedula: String,
    navController: NavController
) {

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors    = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 36.dp)
        ) {

            // Saldo disponible
            Text(text = "Saldo disponible", color = TextGray, fontSize = 14.sp)

            Text(
                text       = "$ %,.2f".format(saldo),
                color      = NavyBlue,
                fontSize   = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Etiqueta "Cuenta de ahorros"
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Emerald.copy(alpha = 0.12f)
            ) {
                Text(
                    text       = "  Cuenta de ahorros  ",
                    color      = Emerald,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier   = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(color = BorderGray)
            Spacer(modifier = Modifier.height(24.dp))

            // Sección de acciones rápidas
            Text(
                text       = "Acciones rápidas",
                color      = TextDark,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // Botón Transferir — navega a la pantalla de transferencia
                ActionButton(
                    emoji    = "💸",
                    label    = "Transferir",
                    modifier = Modifier.weight(1f),
                    onClick  = {
                        navController.navigate("transfer/$cedula")
                    }
                )

                // Botón Extracto — sin función aún
                ActionButton(
                    emoji    = "📄",
                    label    = "Extracto",
                    modifier = Modifier.weight(1f),
                    onClick  = { }
                )

                // Botón Recargar — sin función aún
                ActionButton(
                    emoji    = "💳",
                    label    = "Recargar",
                    modifier = Modifier.weight(1f),
                    onClick  = { }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sección de movimientos
            Text(
                text       = "Últimos movimientos",
                color      = TextDark,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text     = "Sin movimientos recientes",
                color    = TextGray,
                fontSize = 14.sp
            )
        }
    }
}

// ─────────────────────────────────────────────
// Botón de acción rápida reutilizable
// ─────────────────────────────────────────────
@Composable
private fun ActionButton(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick  = onClick,
        modifier = modifier.height(68.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.outlinedButtonColors(contentColor = NavyBlue),
        border   = ButtonDefaults.outlinedButtonBorder.copy()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}
