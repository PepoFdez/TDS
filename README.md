# 📲 AppChat - Aplicación de Chat de Escritorio

<p align="center">
  <img src="https://github.com/user-attachments/assets/56585911-3d26-41f4-bd98-41f2f6d6d590" alt="Logo de StudyMate" width="200">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-8+-blue.svg" alt="Java Version">
  <img src="https://img.shields.io/badge/Maven-3.6+-orange.svg" alt="Maven Version">
  <img src="https://img.shields.io/badge/License-Apache_2.0-blue.svg" alt="License: Apache 2.0">
  </p>

AppChat es una aplicación de chat de escritorio desarrollada en Java que permite a los usuarios comunicarse mediante mensajes de texto y emojis, tanto en conversaciones individuales como grupales. Ofrece gestión de contactos, perfiles de usuario y funcionalidades premium como la exportación de chats a PDF.

## ❕ Características Principales

* **Autenticación de Usuarios**: Registro e inicio de sesión seguros para los usuarios.
* **Gestión de Contactos**:
    * Añadir nuevos contactos individuales.
    * Modificar contactos existentes (nombre).
    * Crear y gestionar grupos de chat, incluyendo la adición y eliminación de miembros.
* **Mensajería Instantánea**:
    * Chat en tiempo real con contactos individuales y grupos.
    * Soporte para el envío de emojis en los mensajes.
    * Visualización de mensajes en formato de burbuja de chat.
* **Perfiles de Usuario**:
    * Visualización de la información del perfil del usuario actual y de los contactos.
    * Modificación de los datos del perfil del usuario (nombre, contraseña, saludo, imagen, etc.).
* **Búsqueda Avanzada**:
    * Funcionalidad para buscar mensajes por contenido de texto, número de teléfono o contacto específico.
* **Funcionalidades Premium**:
    * Opción para que los usuarios activen una suscripción Premium.
    * Sistema de descuentos aplicables al activar la suscripción Premium.
    * Exportación de historiales de chat a formato PDF para usuarios Premium.
    * Gestión de la suscripción Premium activa (ver beneficios, anular).
* **Diseño y Arquitectura**:
    * Interfaz gráfica de usuario (GUI) desarrollada con Java Swing.
    * Capa de persistencia que interactúa con un servicio H2.

## 🗃 Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes principales:

* `gui`: Contiene todas las clases relacionadas con la interfaz gráfica de usuario (ventanas, paneles, diálogos).
* `controlador`: Incluye la clase `Controlador` que actúa como intermediario entre la GUI y la lógica de dominio/persistencia.
* `dominio`: Alberga las entidades del núcleo del negocio (Usuario, Contacto, Mensaje, Grupo, Descuentos) y su lógica asociada.
* `dao`: Define las interfaces para los Objetos de Acceso a Datos (DAO) y sus implementaciones concretas para el servicio de persistencia TDS. También incluye el `PoolDAO` para la gestión de identidades de objetos.
* `utils`: Clases de utilidad (ej: `Utils.java` para formateo, `ExportPDF.java`).

## 👨‍💻 Tecnologías Utilizadas

* **Lenguaje**: Java
* **Interfaz Gráfica**: Java Swing
* **Gestión de Dependencias**: Maven (ver sección de dependencias)
* **Persistencia**: Librería TDS (específica del contexto del proyecto)
* **Componentes Adicionales**:
    * JDateChooser (para selección de fechas en la GUI)
    * BubbleText (para la visualización de mensajes en burbujas)


## 🚀 Instrucciones para ejecutar el proyecto

Sigue estos pasos para ejecutar StudyMate en tu entorno local:

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/PepoFdez/TDS.git
   cd TDS
   ```

2. **Asegúrate de tener instalado:**
   - **Java 8** o superior
   - **Maven 3.6** o superior

3. **Abre el proyecto en tu IDE favorita**
   - Puedes utilizar cualquier IDE compatible con Java y Maven, como **IntelliJ IDEA**, **Eclipse**, **VSCode**, etc.
   - Importa el proyecto como un proyecto Maven desde la carpeta `AppChat`.

4. **Ejecuta la aplicación**
   - Desde tu IDE, busca la clase principal del proyecto `Lanzador` y ejecútala.

---

¿Tienes dudas? Consulta la [Documentación](doc/Doc.pdf) para una guía paso a paso del uso de la aplicación.



