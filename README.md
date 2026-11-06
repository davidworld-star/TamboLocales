# TamboLocales - Control de Tiendas Tambo+

Aplicación móvil nativa en Kotlin para el control y administración de tiendas y locales Tambo+ a nivel nacional.

## 📱 Características principales

1. **Splash Screen:** Pantalla inicial con logo oficial de Tambo+, temporizador de ~2s y transición limpia hacia el inicio de sesión.
2. **Login de Usuarios:** Autenticación con credenciales del sistema, validación de correo y contraseña, y recuperación de clave mediante cuadro de diálogo interactivo.
3. **Control de Locales:** Menú principal intuitivo con accesos directos hacia el mapa interactivo de Google Maps y el listado mantenedor de locales.
4. **Listado de Locales (Mantenedor):** Listado dinámico con RecyclerView en tarjetas amarillas de Tambo+, con estado vacío ("No hay locales registrados"), observando la base de datos en tiempo real. Al presionar una tarjeta navega al mapa individual.
5. **Diálogo de Registro de Local:** Ventana flotante (`DialogFragment`) para agregar nuevos locales con validación de campos obligatorios y coordenadas geográficas (`lat, lon`).
6. **Mapa de un Local:** Vista individual en Google Maps centrada en el local seleccionado con nivel de zoom ajustado (~17x), nombre corto subrayado en la parte superior y ubicación actual del usuario mediante GPS.
7. **Mapa General de Todos los Locales:** Vista global interactiva con Google Maps que visualiza todos los locales con marcadores rojos, ajusta los límites (`LatLngBounds`), muestra una tarjeta flotante de detalle morada al seleccionar un marcador, permite ocultarla al tocar el mapa y soporta toque prolongado (`onMapLongClick`) para registrar un nuevo local con las coordenadas seleccionadas.

---

## 🏗️ Arquitectura y Tecnologías Usadas

- **Patrón de Arquitectura:** MVVM (Model - View - ViewModel) + Repository Pattern.
- **Lenguaje:** Kotlin.
- **UI:** Android XML Views + Material Design 3 (Material3 Light Theme) + ViewBinding.
- **Base de Datos:** Room Database precargada con datos iniciales (KSP).
- **Mapas:** Google Maps SDK for Android (Play Services Maps & Location).
- **Navegación:** Jetpack Navigation Component (Single Activity Architecture + Fragments).
- **Concurrencia:** Kotlin Coroutines & LiveData.
- **Gestión de Configuración:** Gradle Version Catalog (`libs.versions.toml`) + Secrets Gradle Plugin.

---

## 🔑 Configuración de Google Maps API Key

El proyecto hace uso del plugin **Secrets Gradle Plugin** para gestionar de forma segura la API Key de Google Maps.

1. Copie el archivo de ejemplo `local.properties.example` y cree su archivo `local.properties` en la raíz del proyecto (si aún no existe):
   ```bash
   cp local.properties.example local.properties
   ```
2. Abra `local.properties` y agregue su API Key de Google Maps SDK for Android:
   ```properties
   MAPS_API_KEY=TU_API_KEY_AQUI
   ```
3. El plugin inyectará automáticamente `${MAPS_API_KEY}` en el archivo `AndroidManifest.xml` durante la compilación.

---

## 🔑 Credenciales de Prueba

Para ingresar a la aplicación en la pantalla de Login utilice las siguientes credenciales:

- **Correo:** `admin@tambo.com`
- **Contraseña:** `Tambo123`

---

## 📁 Estructura del Proyecto

```text
com.tecsup.tambolocales/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Base de datos Room precargada
│   │   ├── LocalDao.kt             # Data Access Object para consultas
│   │   └── LocalEntity.kt          # Entidad de local (id, nombre, dirección, etc.)
│   └── repository/
│       └── LocalRepository.kt      # Repositorio de la capa de datos
├── ui/
│   ├── control/
│   │   └── ControlFragment.kt      # Pantalla del menú principal de control
│   ├── locales/
│   │   ├── LocalViewHolder.kt      # ViewHolder para la tarjeta de cada local
│   │   ├── LocalesAdapter.kt       # Adapter RecyclerView con DiffUtil
│   │   ├── LocalesFragment.kt      # Listado mantenedor de locales
│   │   └── RegistroLocalDialogFragment.kt # Diálogo de registro de tiendas
│   ├── login/
│   │   ├── LoginFragment.kt        # Vista de login
│   │   └── LoginViewModel.kt       # Lógica de validación de credenciales
│   ├── maps/
│   │   ├── MapaLocalFragment.kt    # Vista del mapa para un local individual
│   │   └── MapaLocalesFragment.kt  # Vista del mapa general de todos los locales
│   ├── splash/
│   │   └── SplashFragment.kt       # Pantalla de carga inicial
│   ├── LocalViewModel.kt           # ViewModel compartido (AndroidViewModel)
│   └── MainActivity.kt             # Activity contenedora (NavHostFragment)
```

---

## 🚀 Cómo Ejecutar el Proyecto

1. Clone el repositorio o abra el proyecto en **Android Studio** (Griffin / 2026.1 o superior).
2. Asegúrese de que `local.properties` contenga una clave válida en `MAPS_API_KEY`.
3. Sincronice los archivos Gradle (*Sync Project with Gradle Files*).
4. Seleccione un dispositivo o emulador Android (API 26+) y ejecute la aplicación (`Shift + F10` o clic en **Run**).
