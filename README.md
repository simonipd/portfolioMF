# PortfolioMF 📈 — Finanzas personales, fondos mutuos y acciones.

¡Hola! 👋 Bienvenido a **PortfolioMF**. 

## 🏗️ ¿Como se estructura este proyecto?

Las tecnologías que se aplicarón en esta proyecto son:

*   **Jetpack Compose & Material 3**
*   **Arquitectura que respira (MVVM + Flow)**: Los datos fluyen de forma reactiva. Si algo cambia, la pantalla se entera al segundo.
*   **Independencia Total**: Se implemento un sistema de **Mocks**, para probar toda la app en cualquier lugar, sin depender de una API externa. Sin embargo también
se realizó la imlementación para ocupar apis

## 🛠️ Guía Técnica para Desarrolladores

Aquí tenemos un breve mapa del proyecto:

### 📂 Organización del Proyecto

El código está estructurado bajo el patrón **MVVM (Model-View-ViewModel)** y dividido en capas claras para facilitar el mantenimiento y la escalabilidad:

*   **`data/` (Capa de Datos)**:
   *   **`api/`**: Contiene la configuración de **Retrofit**, el `MockInterceptor` para el funcionamiento offline y `NetworkConfig` para conmutar entornos.
   *   **`models/`**: DTOs definidos con `String` para montos y porcentajes, garantizando **precisión absoluta** al evitar errores de punto flotante.
   *   **`repository/`**: El repositorio actúa como la fuente única de verdad, mediando entre la red y los ViewModels.

*   **`ui/` (Capa de Presentación)**:
   *   **`portfolio/`**: Pantalla principal y listado de posiciones con su respectiva lógica de negocio.
   *   **`assets/`**: Módulos de búsqueda avanzada y detalle de activos con flujo de compra.
   *   **`theme/`**: Implementación de **Material 3** para una estética moderna y cohesiva.

### 🔄 Manejo de Estado y Reactividad

*   **Kotlin Flow & StateFlow**: Los ViewModels exponen un `UIState` reactivo. La interfaz de usuario (Compose) observa estos estados, reaccionando automáticamente a cambios de datos, estados de carga (`Loading`) o errores.
*   **NetworkResult**: Implementamos una `Sealed Class` para encapsular las respuestas de red, permitiendo un manejo exhaustivo de excepciones y códigos de error.

### 🌐 Estrategia de Networking

El proyecto es **independiente de la red por defecto** gracias a un robusto sistema de Mocks:
*   **Local Mock**: Ideal para desarrollo y pruebas de UI rápidas.
*   **Switch de Red**: En `NetworkConfig.kt`, cambia `USE_REAL_NETWORK = true` para conectar con un servidor real o el Mock Server de Postman.

### 🗺️ Navegación

Utilizamos **Jetpack Compose Navigation**. La lógica de rutas y el paso de argumentos (como el `ticker` del activo) están centralizados en `MainActivity.kt`, manteniendo las pantallas desacopladas.

## 🚀 Cómo Empezar

1.  **Clona y Abre**: Importa el proyecto en Android Studio (Hedgehog+).
2.  **Sincroniza**: Deja que Gradle configure el entorno.
3.  **Ejecuta**: ¡Dale al botón **Run** y explora el portafolio!

---

Hecho con ❤️ por SPD.
