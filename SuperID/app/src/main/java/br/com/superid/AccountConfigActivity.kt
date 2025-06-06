package br.com.superid


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.superid.ui.theme.SuperIDTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Tela de configurações da conta do usuário
class AccountConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se o usuário está com e-mail validado
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && !user.isEmailVerified) {
            Toast.makeText(this, "Valide o seu e-mail.", Toast.LENGTH_LONG).show()
        }

        // Define o conteúdo da tela com o tema
        setContent {
            SuperIDTheme {
                TelaPerfil()
            }
        }
    }
}

@Composable
fun TelaPerfil() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    val emailUsuario = auth.currentUser?.email ?: "Email não disponível"
    val uid = auth.currentUser?.uid
    var nomeUsuario by remember { mutableStateOf("Carregando...") }

    // Busca o nome do usuário no Firestore ao abrir a tela
    LaunchedEffect(uid) {
        uid?.let {
            firestore.collection("Usuario").document(it)
                .get()
                .addOnSuccessListener { doc ->
                    nomeUsuario = doc.getString("nome") ?: "Nome não encontrado"
                }
                .addOnFailureListener {
                    nomeUsuario = "Erro ao carregar nome"
                }
        }
    }

    // Layout principal da tela
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior com ícones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Ícone de editar (sem ação associada por enquanto)
            Icon(
                painter = painterResource(id = R.drawable.logo_pencil),
                contentDescription = "Editar",
                modifier = Modifier.size(60.dp),
                tint = Color.Unspecified
            )

            // Ícone de voltar para o Dashboard
            Icon(
                painter = painterResource(id = R.drawable.logo_back),
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(65.dp)
                    .clickable {
                        val intent = Intent(context, DashboardActivity::class.java)
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    },
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Avatar
        Image(
            painter = painterResource(id = R.drawable.logo_icon),
            contentDescription = "Avatar",
            modifier = Modifier.size(230.dp)
        )

        // Nome e e-mail do usuário
        Text(nomeUsuario, fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(emailUsuario, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(80.dp))

        // Menu de ações do perfil
        HorizontalDivider()
        ProfileButton("Validar e-mail") {
            val intent = Intent(context, EmailVerificationActivity::class.java)
            context.startActivity(intent)
        }
        HorizontalDivider()
        ProfileButton("Recupere a sua senha master") {
            val intent = Intent(context, MasterPasswordRecovery::class.java)
            context.startActivity(intent)
        }
        HorizontalDivider()

        Spacer(modifier = Modifier.height(160.dp))

        // logout
        Text(
            text = "Sair da conta",
            color = Color(0xFF122C4F),
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(context, "Logout realizado", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, WelcomeActivity::class.java)
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()
            }
        )
    }
}

@Composable
fun ProfileButton(titulo: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, fontSize = 16.sp)
        Text(">", fontSize = 16.sp)
    }
}
