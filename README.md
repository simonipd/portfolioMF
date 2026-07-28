# PortfolioMF 📈 — Finanzas personales, fondos mutuos y acciones.

¡Hola! 👋 Bienvenido a **PortfolioMF**. 

## 🏗️ ¿Cómo se estructura este proyecto?

Hemos construido esta aplicación utilizando las tecnologías más modernas y robustas del ecosistema Android:

*   **Jetpack Compose & Material 3**: Para una interfaz vibrante, moderna y adaptable.
*   **Arquitectura que respira (MVVM + Flow)**: Los datos fluyen de forma reactiva. Si algo cambia en el repositorio, la pantalla se entera al segundo.
*   **Independencia Total**: Implementamos un sistema de **Mocks** para probar toda la app en cualquier lugar, sin depender de una API externa, manteniendo la compatibilidad con servicios reales.

---

## 🛠️ Guía Técnica para Desarrolladores

Si vas a explorar las entrañas del proyecto, aquí tienes el mapa de ingeniería que explica cómo está todo orquestado.

### 📂 Mapa del Proyecto (Source Tree)

El código sigue una arquitectura de capas clara para separar la lógica de negocio de la interfaz:

```text
app/src/main/java/com/example/portfoliomf/
├── data/               # Capa de Datos (Data Layer)
│   ├── api/            # Retrofit, Interceptors, safeApiCall y NetworkResult.
│   ├── models/         # DTOs. Valores monetarios en String (Precisión Financiera).
│   └── repository/     # Fuente Única de Verdad (SSOT).
├── ui/                 # Capa de Presentación (UI Layer)
│   ├── assets/         # Búsqueda y Detalle: ViewModels y Screens de Compose.
│   ├── portfolio/      # Gestión de Patrimonio: ViewModels y Screens de Compose.
│   └── theme/          # Design System basado en Material 3.
└── MainActivity.kt      # Inyección de Dependencias manual y Navegación centralizada.
```

### 🧠 Decisiones de Arquitectura y Patrones

*   **DI Manual (Inyección de Dependencias)**: Para mantener el proyecto ligero y libre de "magia" innecesaria, las dependencias se inyectan manualmente en `MainActivity`. Esto facilita el rastreo de instancias y el testing.
*   **Flujo de Datos Unidireccional (UDF)**: La UI envía eventos a los ViewModels, y estos emiten un único estado inmutable vía `StateFlow`. Compose observa este flujo y se repinta automáticamente.
*   **Precisión Numérica**: En finanzas, los errores de redondeo de `Double` son inaceptables. Todos nuestros modelos manejan montos como **`String`** para garantizar que lo que devuelve el servidor sea exactamente lo que ve el usuario.
*   **safeApiCall**: Implementamos un envoltorio genérico en `BaseRepository` para capturar excepciones de red y transformarlas en estados de `NetworkResult` (Success, Error, Loading).

### 🧪 Estrategia de Pruebas

El proyecto cuenta con una suite de pruebas unitarias robusta:
*   **MockK**: Para el simulado de servicios y repositorios.
*   **Turbine**: Para validar la emisión de estados en flujos asíncronos (`Flow`).
*   **Coroutines Test**: Para el manejo controlado de despachadores asíncronos.

### 🌐 Networking y Entornos

En `data/api/NetworkConfig.kt` puedes conmutar entornos instantáneamente:
*   **Local Mock**: Interceptores de OkHttp devuelven datos estáticos (ideal para desarrollo offline).
*   **Real Network**: Activa `USE_REAL_NETWORK = true` para conectar con APIs reales o servidores de prueba.

---

## 🚀 Cómo Empezar

1.  **Clona y Abre**: Importa el proyecto en Android Studio (Hedgehog o superior).
2.  **Sincroniza**: Deja que Gradle prepare el terreno.
3.  **Ejecuta**: ¡Dale al botón **Run** y explora el portafolio!

---

Hecho con ❤️ por SPD.
