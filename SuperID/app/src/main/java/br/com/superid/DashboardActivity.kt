package br.com.superid

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth

// Tela principal do usuário após login  Dashboard
class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se o e-mail do usuário está verificado e exibe aviso, se necessário
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && !user.isEmailVerified) {
            Toast.makeText(this, "Valide o seu e-mail.", Toast.LENGTH_LONG).show()
        }

        // Define o conteúdo da tela com tema do app
        setContent {
            SuperIDTheme {
                TelaPrincipal() // Composable principal da dashboard
            }
        }
    }
}

@Composable
fun TelaPrincipal() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBarContent() // Logo no topo da tela

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de opções do menu
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botão para funcionalidade de login sem senha via QR Code
            OpcaoMenu(
                iconRes = R.drawable.logo_qrcode,
                texto = "Login sem senha",
                onClick = {
                    context.startActivity(Intent(context, CameraScreen::class.java))
                }
            )

            // Botão para acessar o gerenciador de senhas
            OpcaoMenu(
                iconRes = R.drawable.logo_senha,
                texto = "Gerenciar senhas",
                onClick = {
                    context.startActivity(Intent(context, PasswordManagerActivity::class.java))
                }
            )

            // Botão para configurações da conta
            OpcaoMenu(
                iconRes = R.drawable.logo_conf,
                texto = "Configurar conta",
                onClick = {
                    context.startActivity(Intent(context, AccountConfigActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun TopAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo grande do SuperID na parte superior
        Image(
            painter = painterResource(id = R.drawable.logo_superidauth),
            contentDescription = "Logo",
            modifier = Modifier
                .height(220.dp)
                .width(220.dp)
        )
    }
}

@Composable
fun OpcaoMenu(iconRes: Int, texto: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDFF0FF))
            .clickable { onClick() }
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = texto,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = texto,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
