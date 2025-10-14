# 🐶 AdoptMe – Trabajo Práctico Final (Programación 3 – UADE)

Aplicación desarrollada en **Spring Boot + Neo4j**, que simula un sistema de adopción de perros entre refugios y adoptantes.  
El proyecto incluye lógica de grafos para encontrar caminos entre refugios (BFS) y próximamente otros algoritmos de búsqueda y priorización.

---

## 🚀 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.5.x**
- **Neo4j 5.x (base de datos de grafos)**
- **Maven Wrapper (`mvnw`)**
- **Lombok** (para reducir código repetitivo)
- **IntelliJ IDEA / VS Code / Eclipse**

---

## 📂 Estructura del proyecto

```
src/
 └── main/java/com/programacion3/adoptme
     ├── AdoptMApplication.java
     ├── config/
     │    └── DbSeed.java              # Semilla inicial de la base (Shelters, Dogs, Adopters)
     ├── controller/                   # Controladores REST
     ├── domain/                       # Entidades (Shelter, Dog, Adopter)
     ├── dto/                          # Clases para respuestas personalizadas (PathResponse)
     ├── repo/                         # Repositorios Neo4j
     ├── service/                      # Lógica de negocio y algoritmos (BFS, etc)
     └── util/                         # Clases auxiliares (Edge, etc)
```


---

## ⚙️ Requisitos previos

### 1️⃣ Java y Maven
Verificar instalación:
```
java -version
mvn -version
```
Si no los tenés:
- Java 17: https://adoptium.net
- Maven: https://maven.apache.org/download.cgi

---

### 2️⃣ Neo4j Desktop

1. Descargar desde 👉 https://neo4j.com/download/
2. Crear una base local llamada adoptme
3. Configurar:
    - Usuario: neo4j
    - Contraseña: neo4j123
    - Puerto Bolt: 7687
4. Iniciar la base
5. Abrir Neo4j Browser y probar:
   RETURN 1 AS ok;

---

## 🔧 Configuración de Spring Boot

Archivo: src/main/resources/application.yml
```
spring:
neo4j:
uri: bolt://localhost:7687
authentication:
username: neo4j
password: neo4j123
data:
neo4j:
database: neo4j

server:
port: 8080
```
---

## ▶️ Ejecución del proyecto

### En IntelliJ
1. Abrir el proyecto (File → Open → pom.xml)
2. Esperar a que Maven descargue dependencias
3. Verificar que Neo4j esté corriendo
4. Click derecho sobre AdoptMApplication → Run

### Desde terminal
```
./mvnw.cmd spring-boot:run   # Windows

./mvnw spring-boot:run       # Linux/Mac
```
---

## 🌱 Base de datos inicial (Seed)

Al iniciar la aplicación, se ejecuta automáticamente la clase DbSeed.java, que:

- Limpia la base (MATCH (n) DETACH DELETE n)
- Crea refugios (A, B, C) conectados con relaciones :NEAR
- Crea perros (Luna, Toto, Rex) ubicados en refugios
- Crea adoptantes (Carla, Leo)

Podés visualizarlo en Neo4j Browser con:

MATCH (n) RETURN n;

---

## 🧪 Endpoints principales

Método | URL | Descripción
-------|-----|-------------
GET | /ping | Verifica conexión
GET | /shelters | Lista los refugios
GET | /dogs | Lista los perros
GET | /adopters | Lista los adoptantes
GET | /graph/reachable?from=A&to=C | Ejecuta BFS entre refugios

Ejemplo de respuesta:

{
"exists": true,
"path": ["A", "B", "C"]
}

---

## 🧠 Algoritmos implementados

- BFS (Breadth-First Search): encuentra el camino más corto en cantidad de saltos entre refugios.
- Próximamente: Dijkstra (camino mínimo ponderado por distancia o tiempo).

---

## 💡 Tips y resolución de errores

- Si la app tira error de conexión a Neo4j:
    - Verificá que esté corriendo en el puerto 7687
    - Revisá usuario y contraseña en application.yml

- Si el seed falla:
    - Borrar manualmente todos los nodos:
      MATCH (n) DETACH DELETE n;
    - Luego reiniciar la app

- Si IntelliJ no reconoce lombok:
    - Instalar el plugin desde Settings → Plugins → Lombok
    - Habilitar Annotation Processing

---

## 🧍‍♀️ Integrantes del grupo

Nombre | Legajo   
--------|----------
Daniela Melian | 198423   
Lucas Vitale | [Legajo] 
Camila Di Laudo| 1193552

---

## 🧾 Licencia

Proyecto académico – Universidad Argentina de la Empresa (UADE)
Materia: Programación 3 – 2C 2025
