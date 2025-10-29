📚 Librería G - App Móvil

Asignatura: Desarrollo de Aplicaciones Móviles (DSY1105)

Evaluación: Entrega de Encargo Final

Proyecto final de aplicación móvil nativa desarrollada en Kotlin con Jetpack Compose, integrando servicios web propios en AWS y consumo de APIs externas públicas.

👥 Integrantes del Equipo

Samuel Urzua

Patricio Vergara

Dan Jara

📱 Descripción y Funcionalidades

Esta aplicación permite gestionar una biblioteca personal de libros con las siguientes características técnicas:

CRUD Completo:

Crear: Agrega libros nuevos con título, autor y foto (URI).

Leer: Visualiza la lista de libros guardados local y remotamente.

Actualizar: Edita la información de los libros existentes.

Eliminar: Borra libros desde la interfaz, sincronizando la eliminación en la nube.

Sincronización en la Nube (Backend Propio):

Backend: API REST desarrollada en Node.js con Express alojada en AWS EC2.

Base de Datos: PostgreSQL gestionada en AWS RDS.

Sincronización: Botón manual 🔄 en la barra superior para refrescar datos desde el servidor.

Modo Offline (Persistencia):

Implementación de Room Database para almacenamiento local, permitiendo que la app funcione sin internet.

API Externa:

Consumo de ITBook Store API mediante Retrofit para mostrar un carrusel de novedades tecnológicas en el Home.

🔗 Listado de Endpoints

1. Microservicio Propio (AWS)

⚠️ Nota Importante: La IP de la instancia EC2 es dinámica. Asegúrese de que la instancia esté encendida y reemplace la IP si esta cambia.

Base URL Actual: http://98.94.88.109:3000/     

Método

Ruta

Descripción

GET

/books

Obtener todos los libros almacenados.

POST

/books

Crear un nuevo libro ({title, author}).

PUT

/books/:id

Actualizar un libro existente.

DELETE

/books/:id

Eliminar un libro por su ID.

2. API Externa (Pública)

Fuente: ITBook Store API

Endpoint: https://api.itbook.store/1.0/new

Uso: Muestra "Novedades IT" en la pantalla principal.

🛠️ Instrucciones para Ejecutar

Requisitos Previos

Android Studio (Ladybug / Koala o superior).

JDK 11 o superior.

Dispositivo o Emulador con Min SDK 24 (Android 7.0).

Pasos

Clonar este repositorio.

Abrir el proyecto en Android Studio.

Esperar la sincronización de Gradle (Sync Project with Gradle Files).

Verificar que la IP en RetrofitClient.kt corresponda a la instancia activa de AWS.

Ejecutar en el emulador o instalar el APK firmado adjunto.

🧪 Calidad y Pruebas

Se incluyen pruebas unitarias en BookRepositoryTest.kt cubriendo el 100% de la lógica del repositorio:

add (Insertar)

delete (Borrar uno)

deleteAll (Limpiar base de datos)

getAll (Flujo de datos)

📦 Entregables Adjuntos

En la carpeta raíz (o release/) de este repositorio se encuentran:

APK Firmado: app-release.apk (Listo para instalar).

Llave de Firma: llave_libreria.jks (Password: 123456).

📋 Gestión del Proyecto (Evidencia)

La planificación y distribución de tareas se realizó mediante metodología ágil en Trello.

Enlace al tablero: https://trello.com/invite/b/68dfd6692c6943b370b1d6b2/ATTI965c1cfe16c75fba002bf227ebcc28f51E1366C6/grupo-8-libreriag
