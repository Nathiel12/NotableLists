package ucne.edu.notablelists.presentation.users

import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthScreen(
    isLogin: Boolean,
    onNavigateToOther: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var isProcessingClick by remember { mutableStateOf(false) }
    var lastClickTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) isProcessingClick = false
    }

    LaunchedEffect(state.error, state.usernameError, state.passwordError) {
        if (state.error != null || state.usernameError != null || state.passwordError != null) {
            isProcessingClick = false
        }
    }

    LaunchedEffect(state.navigationEvent) {
        state.navigationEvent?.let { effect ->
            when (effect) {
                is UserSideEffect.NavigateToProfile -> onNavigateToProfile()
                else -> Unit
            }
            viewModel.onEvent(UserEvent.NavigationHandled)
        }
    }

    BackHandler(enabled = state.isLoading || isProcessingClick) {}

    if (state.showSkipDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(UserEvent.DismissSkipDialog) },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("¿Estás seguro?") },
            text = { Text(if(isLogin) "Si usas la aplicación sin iniciar sesión podrías perder tus notas." else "Si usas la aplicación sin cuenta podrías perder tus notas.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(UserEvent.DismissSkipDialog)
                        onNavigateToProfile()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) { Text("Omitir registro") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(UserEvent.DismissSkipDialog) }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AndroidView(
                            factory = { ctx ->
                                ImageView(ctx).apply {
                                    val pm = ctx.packageManager
                                    try {
                                        val appIcon = pm.getApplicationIcon(ctx.packageName)
                                        setImageDrawable(appIcon)
                                    } catch (e: Exception) {}
                                    scaleType = ImageView.ScaleType.FIT_CENTER
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Notable Lists", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                AuthHeader(isLogin)
                Spacer(modifier = Modifier.height(48.dp))

                state.error?.let {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(it, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                AuthTextField(
                    value = state.username,
                    onValueChange = { viewModel.onEvent(UserEvent.UserNameChanged(it)) },
                    label = "Usuario",
                    error = state.usernameError ?: if(state.error != null && state.error!!.contains("Usuario")) state.error else null,
                    icon = { Icon(Icons.Default.AccountCircle, null) },
                    enabled = !state.isLoading && !isProcessingClick,
                    hasError = state.usernameError != null || state.error != null
                )
                Spacer(modifier = Modifier.height(24.dp))

                AuthTextField(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(UserEvent.PasswordChanged(it)) },
                    label = "Contraseña",
                    error = state.passwordError ?: if(state.error != null && state.error!!.contains("Contraseña")) state.error else null,
                    isPassword = true,
                    enabled = !state.isLoading && !isProcessingClick,
                    hasError = state.passwordError != null || state.error != null
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime > 1000 && !isProcessingClick && !state.isLoading) {
                            lastClickTime = currentTime
                            isProcessingClick = true
                            viewModel.onEvent(if (isLogin) UserEvent.LoginUser else UserEvent.CreateUser)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !state.isLoading && !isProcessingClick,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(if (isLogin) "Iniciar Sesión" else "Registrarse", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(if (isLogin) "¿No tienes cuenta? " else "¿Ya tienes cuenta? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    TextButton(
                        onClick = {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastClickTime > 1000 && !isProcessingClick && !state.isLoading) {
                                lastClickTime = currentTime
                                onNavigateToOther()
                            }
                        },
                        enabled = !state.isLoading && !isProcessingClick
                    ) {
                        Text(if (isLogin) "Regístrate aquí" else "Inicia Sesión", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime > 1000 && !isProcessingClick && !state.isLoading) {
                            lastClickTime = currentTime
                            viewModel.onEvent(UserEvent.ShowSkipDialog)
                        }
                    },
                    enabled = !state.isLoading && !isProcessingClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Continuar sin cuenta", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (state.isLoading || isProcessingClick) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.05f)).clickable(indication = null, interactionSource = remember { MutableInteractionSource() }, onClick = {}),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) CircularWavyProgressIndicator(modifier = Modifier.size(48.dp), color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun AuthHeader(isLogin: Boolean) {
    Column {
        Text(
            text = buildAnnotatedString {
                append(if (isLogin) "Hola de nuevo,\n" else "Bienvenido,\n")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(if (isLogin) "Inicia Sesión" else "Crea tu cuenta")
                }
            },
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isLogin) "Sincroniza tus notas en cualquier lugar." else "Únete para guardar tus notas en la nube.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    isPassword: Boolean = false,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean,
    hasError: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val cornerRadius by animateDpAsState(targetValue = if (isFocused) 8.dp else 24.dp, label = "shape")

    if (error != null) {
        Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp, start = 8.dp))
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
        enabled = enabled,
        isError = hasError,
        shape = RoundedCornerShape(cornerRadius),
        interactionSource = interactionSource,
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant, focusedBorderColor = MaterialTheme.colorScheme.primary),
        leadingIcon = icon
    )
}