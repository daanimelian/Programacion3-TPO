# 🔄 Cómo Reiniciar el Servidor

## Problema Actual

Error 500: "No static resource network/graph"

**Causa**: El servidor no reconoce el nuevo endpoint `/network/graph` porque no se reinició después de agregar el código.

---

## ✅ Solución: Reiniciar el Servidor

### Opción 1: Desde IntelliJ IDEA

1. **Detener el servidor actual**:
   - Click en el botón ⏹️ (Stop) en la barra de herramientas
   - O presiona `Ctrl + F2`

2. **Compilar el proyecto**:
   - Menu: `Build` → `Build Project`
   - O presiona `Ctrl + F9`

3. **Ejecutar nuevamente**:
   - Click en el botón ▶️ (Run)
   - O presiona `Shift + F10`

### Opción 2: Desde Terminal (Maven)

```bash
# 1. Detener cualquier proceso Java corriendo
pkill -f "spring-boot" || pkill -f "java.*adoptme"

# 2. Limpiar y compilar
./mvnw clean compile

# 3. Ejecutar la aplicación
./mvnw spring-boot:run
```

### Opción 3: Desde Terminal (Gradle - si aplica)

```bash
# 1. Detener el servidor
./gradlew --stop

# 2. Ejecutar nuevamente
./gradlew bootRun
```

---

## 🔍 Verificar que el Servidor Está Corriendo

### Paso 1: Verificar proceso Java

```bash
ps aux | grep java
```

Deberías ver un proceso similar a:
```
java -jar adoptme-0.0.1-SNAPSHOT.jar
```

### Paso 2: Verificar endpoint de salud

```bash
curl http://localhost:8080/health
```

Respuesta esperada:
```json
{
  "status": "UP"
}
```

### Paso 3: Verificar el nuevo endpoint

```bash
curl http://localhost:8080/network/graph
```

Respuesta esperada:
```json
{
  "message": "Graph data loaded successfully",
  "nodes": ["A", "B", "C", ...],
  "edges": [{"from": "A", "to": "B", "weight": 15.5}, ...]
}
```

---

## 🐛 Si Sigue Sin Funcionar

### 1. Verificar que el código se compiló

```bash
# Verificar que la clase existe en target/
ls -la target/classes/com/programacion3/adoptme/controller/NetworkController.class
```

### 2. Verificar logs del servidor

Busca en los logs:
```
Mapped "{[/network/graph],methods=[GET]}"
```

Si NO aparece, el endpoint no se registró.

### 3. Limpiar completamente y reconstruir

```bash
# Borrar todo el target/
rm -rf target/

# Reconstruir desde cero
./mvnw clean install

# Ejecutar
./mvnw spring-boot:run
```

### 4. Verificar puertos

```bash
# Ver qué está usando el puerto 8080
lsof -i :8080
```

Si otro proceso lo está usando:
```bash
# Matar el proceso
kill -9 <PID>
```

---

## 📝 Cambios Recientes que Requieren Reinicio

Los siguientes cambios **SIEMPRE** requieren reiniciar el servidor:

1. ✅ **Nuevos endpoints REST** (nuestro caso: `/network/graph`)
2. ✅ Nuevos Controllers o Services
3. ✅ Cambios en configuración (@Configuration, @Bean)
4. ✅ Cambios en propiedades (application.properties)
5. ✅ Nuevas dependencias en pom.xml

**NO requieren reinicio**:
- ❌ Cambios en frontend (HTML, CSS, JS)
- ❌ Cambios en archivos estáticos

---

## 🚀 Verificación Post-Reinicio

Una vez reiniciado el servidor, prueba:

1. **Abrir el frontend**: http://localhost:8080
2. **Ir al tab**: "Redes & MST"
3. **Click en**: "Mostrar Grafo de Conexiones"
4. **Resultado esperado**: Ver el grafo con 15 nodos y 39 aristas

---

## ⚡ Hot Reload (Opcional)

Para evitar reiniciar constantemente, puedes usar **Spring Boot DevTools**:

### 1. Agregar dependencia en pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

### 2. Habilitar automatic restart

En IntelliJ:
- File → Settings → Build, Execution, Deployment → Compiler
- ✅ Check "Build project automatically"

### 3. Registry (IntelliJ)

- Help → Find Action → Registry
- ✅ Check "compiler.automake.allow.when.app.running"

Con esto, el servidor se recargará automáticamente cuando guardes cambios.

---

## 📞 Comandos Útiles

```bash
# Ver logs en tiempo real
tail -f logs/spring-boot-application.log

# Ver qué endpoints están registrados
curl http://localhost:8080/actuator/mappings

# Reinicio rápido (un solo comando)
pkill -f "spring-boot" && ./mvnw spring-boot:run

# Reinicio con logs limpios
clear && pkill -f "spring-boot" && ./mvnw spring-boot:run
```

---

## ✅ Checklist de Reinicio

- [ ] Detener el servidor actual
- [ ] Verificar que no haya procesos Java corriendo
- [ ] Compilar el proyecto (`./mvnw compile`)
- [ ] Ejecutar el servidor (`./mvnw spring-boot:run`)
- [ ] Esperar el mensaje: "Started AdoptmeApplication"
- [ ] Verificar endpoint: `curl http://localhost:8080/network/graph`
- [ ] Refrescar el navegador (F5)
- [ ] Probar la funcionalidad del grafo

---

**Nota**: Si usas Docker, el comando es:
```bash
docker-compose restart
# o
docker restart adoptme-container
```
