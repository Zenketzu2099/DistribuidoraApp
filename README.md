# 📱 DistribuidoraApp

Aplicación Android desarrollada en **Kotlin** como parte de modulo de Taller de Programación.  
El proyecto permite:

- Autenticación de usuarios con **Firebase Authentication**.
- Registro y consulta de ubicación GPS usando **FusedLocationProviderClient**.
- Almacenamiento de datos en **Firebase Realtime Database**.
- Gestión de sesiones: login, logout y visualización de última ubicación.

---

## 🚀 Funcionalidades principales

1. Inicio de sesión con correo y contraseña registrados en Firebase.
2. Obtención de coordenadas GPS (latitud/longitud).
3. Guardado de ubicación en Firebase en tiempo real.
4. Consulta de última ubicación almacenada.
5. Cierre de sesión y retorno seguro al login.

---

## 📋 Historias de Usuario

- **HU1**: Como administrador, quiero iniciar sesión con mis credenciales de Firebase, para acceder de manera segura a la aplicación.
- **HU2**: Como administrador, quiero visualizar un saludo con mi correo electrónico, para confirmar que inicié sesión correctamente.
- **HU3**: Como administrador, quiero guardar mi ubicación GPS en la nube, para mantener un registro actualizado de mi posición.
- **HU4**: Como administrador, quiero visualizar la última ubicación guardada, para confirmar que la información quedó registrada.
- **HU5**: Como administrador, quiero cerrar sesión de forma segura, para que otras personas no accedan a mi información.

---

## ⚙️ Tecnologías utilizadas

- Kotlin
- Android Studio
- Firebase Authentication
- Firebase Realtime Database
- Google Play Services (Location API)

---

## 🛠️ Configuración del proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Zenketzu2099/DistribuidoraApp.git
2. Abrir en Android Studio.
3. Colocar el archivo google-services.json dentro de la carpeta /app/.
4. Conectar el dispositivo físico o emulador y ejecutar la aplicación.

## 👨‍💻 Autor
- Rodrigo Solís – [GitHub](https://github.com/Zenketzu2099)