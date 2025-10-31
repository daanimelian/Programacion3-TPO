# 🧪 Guía de Testing - AdoptMe Frontend + Backend

## ✅ Errores Corregidos

Se corrigieron los siguientes errores de compilación:

1. **AdoptionsController.java** - Faltaban imports:
   - ✅ Agregado `import java.util.Map;`
   - ✅ Agregado `import java.util.HashMap;`

2. **RoutesController.java** - Nombres de campos incorrectos:
   - ✅ Cambiado `e.a` → `e.from`
   - ✅ Cambiado `e.b` → `e.to`

---

## 📋 Prerequisitos

Antes de comenzar, asegúrate de tener:

- ✅ **Java 21** instalado
- ✅ **Maven** instalado (o usar `mvnw`)
- ✅ **Neo4j** corriendo en `localhost:7687` con credenciales `neo4j/neo4j123`
- ✅ **Git** para hacer pull de los cambios

---

## 🔄 Paso 1: Actualizar el Código

```bash
# Hacer pull de los cambios más recientes
git fetch origin
git checkout claude/create-frontend-web-011CUg7oRnLkRECeznZsqCyK
git pull origin claude/create-frontend-web-011CUg7oRnLkRECeznZsqCyK
```

---

## 🏗️ Paso 2: Compilar el Backend

### Opción A: Desde el IDE (IntelliJ/Eclipse)

1. **Refrescar el proyecto:**
   - IntelliJ: `File → Reload All from Disk` o `Ctrl+Alt+Y`
   - Eclipse: `Right click → Refresh` o `F5`

2. **Clean y Rebuild:**
   - IntelliJ: `Build → Rebuild Project`
   - Eclipse: `Project → Clean → Clean all projects`

3. **Verificar que no hay errores de compilación**

### Opción B: Desde la línea de comandos

```bash
# En Windows (PowerShell o CMD)
.\mvnw.cmd clean compile

# En Linux/Mac
./mvnw clean compile
```

Si todo está bien, deberías ver:
```
[INFO] BUILD SUCCESS
```

---

## 🚀 Paso 3: Iniciar Neo4j

1. Abre **Neo4j Desktop**
2. Inicia la base de datos `neo4j` (o la que hayas configurado)
3. Verifica que esté corriendo en: `bolt://localhost:7687`
4. Credenciales: `neo4j / neo4j123`

**Verificar conexión:**
```cypher
RETURN "OK" as status;
```

---

## 🎯 Paso 4: Iniciar el Backend

### Opción A: Desde el IDE

1. Busca la clase `AdoptMApplication.java`
2. Click derecho → `Run 'AdoptMApplication'`
3. Espera a ver el mensaje:
   ```
   Started AdoptMApplication in X.XXX seconds
   ```

### Opción B: Desde la línea de comandos

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**El backend debería estar corriendo en:** `http://localhost:8080`

### ✅ Verificar Backend

Abre tu navegador o usa curl:
```bash
curl http://localhost:8080/ping
```

Deberías recibir una respuesta (puede ser vacía o un mensaje de "pong").

---

## 🎨 Paso 5: Iniciar el Frontend

El frontend ya está creado en la carpeta `/frontend`.

### Opción A: Python HTTP Server (RECOMENDADO)

```bash
# Navega a la carpeta frontend
cd frontend

# Inicia el servidor
python -m http.server 8000

# O en Python 3 explícitamente
python3 -m http.server 8000
```

**El frontend estará en:** `http://localhost:8000`

### Opción B: Abrir directamente en el navegador

1. Navega a la carpeta `frontend`
2. Doble click en `index.html`

⚠️ **Nota:** Si usas esta opción y tienes problemas de CORS, usa la Opción A.

### Opción C: VS Code Live Server

1. Instala la extensión "Live Server"
2. Click derecho en `index.html` → "Open with Live Server"

---

## 🧪 Paso 6: Probar la Aplicación

### 6.1 Verificar Conexión

1. Abre el frontend: `http://localhost:8000`
2. Verifica el indicador de conexión en el header:
   - 🟢 Verde = Conectado
   - 🔴 Rojo = Error de conexión
   - 🟡 Amarillo = Conectando

### 6.2 Probar Dashboard

1. Ve a la pestaña **Dashboard**
2. Deberías ver:
   - Número de refugios (4)
   - Número de perros (6)
   - Número de adoptantes (3)
   - Listas con los datos

### 6.3 Probar Algoritmos de Grafos

**BFS/DFS:**
1. Ve a la pestaña **Rutas & Grafos**
2. Selecciona: Origen = A, Destino = C
3. Click en "Ejecutar BFS"
4. Deberías ver un camino como: `A → H → C` o similar
5. Click en "Ejecutar DFS"
6. Deberías ver otro camino válido

**Dijkstra:**
1. En la misma pestaña
2. Selecciona: Origen = A, Destino = C
3. Click en "Calcular Ruta Óptima"
4. Deberías ver la distancia total en km

**TSP:**
1. Selecciona refugios: A, B, C
2. Click en "Calcular Ruta TSP"
3. Deberías ver la ruta óptima y distancia total

### 6.4 Probar MST

1. Ve a la pestaña **Redes & MST**
2. Click en "Algoritmo de Kruskal"
3. Deberías ver las aristas del árbol de expansión mínima
4. Click en "Algoritmo de Prim"
5. Deberías ver el mismo resultado (o similar)

### 6.5 Probar Matching

**Greedy:**
1. Ve a la pestaña **Matching**
2. Selecciona un adoptante (ej: Camila)
3. Click en "Ejecutar Greedy"
4. Deberías ver los perros asignados con sus scores

**Backtracking:**
1. En la misma pestaña
2. Click en "Ejecutar Backtracking"
3. Deberías ver múltiples asignaciones de perros a adoptantes

### 6.6 Probar Ordenamiento

1. Ve a la pestaña **Ordenamiento**
2. Selecciona criterio: Prioridad
3. Selecciona algoritmo: MergeSort
4. Click en "Ordenar Perros"
5. Deberías ver la lista ordenada
6. Prueba con QuickSort y otros criterios (Edad, Peso)

### 6.7 Probar Transporte

1. Ve a la pestaña **Transporte**
2. Ingresa capacidad: 50 kg
3. Click en "Optimizar Transporte"
4. Deberías ver qué perros maximizan la prioridad dentro de la capacidad

---

## 🐛 Problemas Comunes

### Error: "Cannot connect to backend"

**Solución:**
1. Verifica que el backend esté corriendo en `http://localhost:8080`
2. Prueba: `curl http://localhost:8080/ping`
3. Revisa los logs del backend para errores

### Error: "Connection refused to Neo4j"

**Solución:**
1. Inicia Neo4j Desktop
2. Verifica que la base esté activa (luz verde)
3. Confirma el puerto 7687
4. Verifica credenciales en `application.yml`

### Error: "No data showing in Dashboard"

**Solución:**
1. Verifica que Neo4j tenga datos (ejecuta en Neo4j Browser):
   ```cypher
   MATCH (n) RETURN count(n) AS total;
   ```
2. Si no hay datos, reinicia el backend para que se ejecute el seed
3. O ejecuta manualmente el seed desde Neo4j Browser

### Error: CORS en el navegador

**Solución:**
1. No abras `index.html` directamente
2. Usa un servidor HTTP:
   ```bash
   cd frontend
   python -m http.server 8000
   ```

---

## 📊 Checklist de Verificación

- [ ] ✅ Backend compila sin errores
- [ ] ✅ Neo4j está corriendo y tiene datos
- [ ] ✅ Backend responde en `http://localhost:8080/ping`
- [ ] ✅ Frontend se abre en `http://localhost:8000`
- [ ] ✅ Indicador de conexión muestra verde
- [ ] ✅ Dashboard muestra estadísticas (4 refugios, 6 perros, 3 adoptantes)
- [ ] ✅ BFS/DFS encuentra caminos
- [ ] ✅ Dijkstra calcula distancias
- [ ] ✅ TSP encuentra ruta óptima
- [ ] ✅ Kruskal/Prim calculan MST
- [ ] ✅ Greedy asigna perros
- [ ] ✅ Backtracking asigna múltiples perros
- [ ] ✅ MergeSort/QuickSort ordenan perros
- [ ] ✅ Knapsack optimiza transporte

---

## 🎉 Si todo funciona...

¡Felicitaciones! Tienes una aplicación completa con:
- ✅ Backend Spring Boot con 10+ algoritmos
- ✅ Frontend web interactivo y moderno
- ✅ Base de datos de grafos Neo4j
- ✅ Todos los algoritmos funcionando

---

## 📞 Ayuda Adicional

Si encuentras errores:

1. **Verifica los logs del backend** en la consola/terminal
2. **Abre la consola del navegador** (F12) para ver errores del frontend
3. **Revisa la pestaña Network** en las DevTools para ver las requests fallidas
4. **Verifica los logs de Neo4j** en Neo4j Desktop

---

## 🔍 Endpoints de Testing Manual

Si quieres probar el backend directamente:

```bash
# Health check
curl http://localhost:8080/ping

# Listar refugios
curl http://localhost:8080/shelters

# Listar perros
curl http://localhost:8080/dogs

# Listar adoptantes
curl http://localhost:8080/adopters

# BFS
curl "http://localhost:8080/graph/reachable?from=A&to=C&method=bfs"

# Dijkstra
curl "http://localhost:8080/routes/shortest?from=A&to=C"

# TSP
curl "http://localhost:8080/routes/tsp/bnb?nodes=A,B,C"

# MST Kruskal
curl "http://localhost:8080/network/mst?algorithm=kruskal"

# Greedy
curl "http://localhost:8080/adoptions/greedy?adopterId=P1"

# Backtracking
curl "http://localhost:8080/adoptions/constraints/backtracking"

# Ordenar perros
curl "http://localhost:8080/dogs/sort?criteria=priority&algorithm=mergesort"

# Knapsack
curl "http://localhost:8080/transport/optimal-dp?capacityKg=50"
```

---

¡Buena suerte con el testing! 🚀
