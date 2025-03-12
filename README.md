# Aplicación de Catálogo de Películas (TMDB)

## Descripción del Proyecto

Esta aplicación Android permite a los usuarios explorar un catálogo de películas utilizando la API de The Movie Database (TMDB). La aplicación implementa una arquitectura moderna y proporciona las siguientes funcionalidades:

- **Exploración de Películas Populares**: Visualización de películas populares en un formato de cuadrícula.
- **Búsqueda de Películas**: Búsqueda por título con resultados en tiempo real.
- **Detalles de Películas**: Vista detallada con información como sinopsis, año de lanzamiento, géneros y calificación.
- **Paginación**: Carga incremental de resultados para optimizar el rendimiento y la experiencia del usuario.
- **Gestión de Errores**: Manejo apropiado de errores de red y opciones para reintentar.
- **Indicadores Visuales**: Indicadores de carga durante las peticiones a la API.

El proyecto está dividido en dos componentes principales:

1. **Backend (Spring Boot)**: Servidor local simple que sirve como ejemplo de integración con APIs personalizadas.
2. **Frontend (Android)**: Aplicación móvil desarrollada con Kotlin y Jetpack Compose.

## Configuración y Ejecución

### Requisitos Previos

- Java JDK 17 o superior
- Android Studio Iguana (2023.2.1) o superior
- Gradle 8.10.2 o superior
- API Key de TMDB (obtenible en [https://www.themoviedb.org/settings/api](https://www.themoviedb.org/settings/api))

### Configuración del Backend (Spring Boot)

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tu-usuario/tu-repositorio.git
   cd tu-repositorio
   ```

2. **Ejecutar el servidor**:
   - Windows:
     ```bash
     cd demo
     .\gradlew.bat bootRun
     ```
   - Linux/Mac:
     ```bash
     cd demo
     ./gradlew bootRun
     ```

3. **Verificar el funcionamiento**:
   - Abre un navegador y visita `http://localhost:8080/hello`
   - Deberías ver una respuesta JSON: `{"message":"Hola Mundo","status":"success"}`

### Configuración de la Aplicación Android

1. **Configurar la API Key**:
   - Abre `Tarea3/app/src/main/java/com/example/tarea3/api/RetrofitClient.kt`
   - Reemplaza `const val API_KEY = "tu_api_key_aquí"` con tu clave API de TMDB

2. **Abrir el proyecto en Android Studio**:
   - Abre Android Studio
   - Selecciona "Open an Existing Project"
   - Navega hasta la carpeta `Tarea3` y ábrela

3. **Sincronizar el proyecto con Gradle**:
   - Android Studio debería sincronizar automáticamente, pero si no lo hace:
   - Ve a File > Sync Project with Gradle Files

4. **Ejecutar la aplicación**:
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en el botón "Run" (▶️) o presiona Shift+F10
   - Selecciona el dispositivo/emulador donde deseas ejecutar la aplicación

## Arquitectura de la Aplicación

La aplicación sigue una arquitectura MVVM (Model-View-ViewModel) con Repository Pattern:

```
┌─────────────────┐     ┌───────────────┐     ┌─────────────────────┐
│    UI (Compose) │     │   ViewModel   │     │     Repository      │
│                 │◄───►│               │◄───►│                     │
└─────────────────┘     └───────────────┘     └─────────────────────┘
                                                        ▲
                                                        │
                                               ┌────────┴────────┐
                                               │                 │
                                 ┌─────────────▼─────┐   ┌───────▼───────┐
                                 │  Remote Data Source│   │ Local Database│
                                 │    (Retrofit API)  │   │   (Opcional)  │
                                 └───────────────────┘   └─────────────────┘
```

### Componentes Principales:

1. **UI Layer**:
   - Implementada con Jetpack Compose
   - Pantallas principales: Lista de películas, Detalles de película
   - Componentes reutilizables: MovieItem, SearchBar, ErrorMessage

2. **ViewModel Layer**:
   - MovieViewModel: Maneja la lógica de negocio y el estado de la UI
   - Expone StateFlows observables para la UI
   - Coordina las solicitudes al repositorio

3. **Repository Layer**:
   - MovieRepository: Abstrae el origen de los datos
   - Implementa la paginación con Paging 3

4. **Data Sources**:
   - Remote: ApiService (interfaz Retrofit) para realizar solicitudes a TMDB
   - Paging: MoviePagingSource y SearchMoviePagingSource para paginación

5. **Model Layer**:
   - Clases de datos (Movie, MovieDetails, Genre) que modelan las respuestas de la API

## Desafíos Encontrados y Soluciones

### 1. Configuración del Entorno y Errores de Compilación

**Desafío**: Errores de compilación relacionados con dependencias y configuraciones de Gradle.

**Solución**: 
- Actualización de las dependencias a versiones compatibles
- Configuración correcta del compilador Kotlin para Compose
- Implementación de la estructura de carpetas adecuada

### 2. Integración de APIs Experimentales

**Desafío**: Algunos componentes de Material 3 (como TopAppBar) estaban marcados como experimentales.

**Solución**: 
- Uso de anotaciones `@OptIn(ExperimentalMaterial3Api::class)` para manejar correctamente las APIs experimentales
- Implementación de alternativas estables cuando fue posible

### 3. Paginación con Compose

**Desafío**: Integrar la biblioteca Paging 3 con Jetpack Compose para manejar grandes conjuntos de datos.

**Solución**:
- Implementación de PagingSource personalizado
- Uso de collectAsLazyPagingItems() para integrar con Compose
- Manejo adecuado de los estados de carga y error

### 4. Comunicación entre Backend y Frontend

**Desafío**: Permitir que la aplicación Android se comunique con el servidor local durante el desarrollo.

**Solución**:
- Configuración de la dirección IP 10.0.2.2 para acceder al localhost desde el emulador
- Configuración de network_security_config.xml para permitir tráfico HTTP
- Implementación de manejadores de errores robustos

## Dependencias Utilizadas

### Frontend (Android)

#### Jetpack Compose
- **androidx.activity:activity-compose**: Integración de Compose con Activities
- **androidx.compose.ui:ui**: Biblioteca principal de UI de Compose
- **androidx.compose.material3:material3**: Componentes Material Design 3
- **androidx.navigation:navigation-compose**: Navegación en Compose

**Propósito**: Framework moderno de UI declarativa para crear interfaces de usuario.

#### Networking
- **com.squareup.retrofit2:retrofit**: Cliente HTTP para Android
- **com.squareup.retrofit2:converter-gson**: Convertidor JSON para Retrofit
- **com.squareup.okhttp3:logging-interceptor**: Interceptor para registro de peticiones HTTP

**Propósito**: Realizar y gestionar peticiones a la API de TMDB.

#### Paginación
- **androidx.paging:paging-runtime-ktx**: Biblioteca de paginación de Android
- **androidx.paging:paging-compose**: Integración de Paging con Compose

**Propósito**: Cargar y mostrar grandes conjuntos de datos de manera eficiente.

#### Imágenes
- **io.coil-kt:coil-compose**: Biblioteca para carga y caché de imágenes

**Propósito**: Cargar eficientemente las imágenes de películas desde URLs.

#### Coroutines
- **org.jetbrains.kotlinx:kotlinx-coroutines-android**: Programación asíncrona

**Propósito**: Manejo asíncrono de operaciones de red e I/O.

#### Lifecycle
- **androidx.lifecycle:lifecycle-viewmodel-compose**: Integración de ViewModel con Compose

**Propósito**: Gestión del ciclo de vida y estado de la UI.

### Backend (Spring Boot)

- **spring-boot-starter-web**: Framework web de Spring Boot
- **spring-boot-starter-data-jpa**: Persistencia de datos con JPA
- **h2database**: Base de datos en memoria para desarrollo
- **spring-boot-devtools**: Herramientas de desarrollo para recargar automáticamente

**Propósito**: Crear un servidor RESTful simple para ejemplificar la integración con APIs personalizadas.

## Créditos y Recursos

- [The Movie Database (TMDB) API](https://developers.themoviedb.org/3)
- [Android Developers - Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - vea el archivo [LICENSE](LICENSE) para más detalles.
