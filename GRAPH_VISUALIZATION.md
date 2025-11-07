# Visualización del Grafo de Shelters

## 📊 Nueva Funcionalidad Agregada

Se ha implementado una visualización interactiva del grafo de conexiones entre shelters usando **Cytoscape.js**.

---

## ✨ Características

### 🔹 Backend

**Nuevo Endpoint**: `GET /network/graph`

Devuelve todas las conexiones entre shelters para visualización:

```json
{
  "message": "Graph data loaded successfully",
  "nodes": ["A", "B", "C", "D", ...],
  "edges": [
    {"from": "A", "to": "B", "weight": 15.5},
    {"from": "B", "to": "C", "weight": 12.0},
    ...
  ]
}
```

**Ubicación**: `/src/main/java/com/programacion3/adoptme/controller/NetworkController.java`

**Características del endpoint**:
- Carga todos los shelters de la base de datos
- Carga todas las aristas tipo "NEAR"
- Evita duplicados en grafo no dirigido
- Maneja casos de grafo vacío

---

### 🔹 Frontend

**Ubicación de la visualización**: Tab "Redes & MST" > "Visualización del Grafo de Shelters"

**Componentes agregados**:

1. **HTML** (`index.html`)
   - CDN de Cytoscape.js 3.28.1
   - Contenedor del grafo (600px de alto)
   - Botón para mostrar/ocultar visualización
   - Leyenda interactiva con información

2. **JavaScript** (`app.js`)
   - Función `showGraphVisualization()`
   - Configuración de Cytoscape con layout COSE
   - Estilos personalizados para nodos y aristas
   - Interactividad (drag & drop, zoom, selección)

---

## 🎨 Características Visuales

### Nodos (Shelters)
- **Color**: Azul (#3498db)
- **Tamaño**: 50x50 px
- **Etiqueta**: ID del shelter
- **Border**: 3px azul oscuro
- **Selección**: Cambia a rojo (#e74c3c)

### Aristas (Conexiones)
- **Color**: Gris (#95a5a6)
- **Grosor**: 3px
- **Etiqueta**: Distancia en km (ej: "15.5 km")
- **Curva**: Bezier para mejor legibilidad
- **Selección**: Cambia a rojo y grosor 5px

### Layout
- **Algoritmo**: COSE (Compound Spring Embedder)
- **Características**:
  - Distribución automática optimizada
  - Minimiza cruce de aristas
  - Espaciado uniforme entre nodos
  - Ajuste automático al contenedor

---

## 🎮 Interactividad

### Funciones disponibles:

1. **Mostrar/Ocultar**: Botón toggle para abrir/cerrar visualización
2. **Drag & Drop**: Arrastra nodos para reorganizar el grafo
3. **Zoom**: Scroll del mouse para acercar/alejar
4. **Pan**: Arrastra el fondo para mover el canvas
5. **Selección**: Click en nodos o aristas para resaltarlos
6. **Auto-fit**: El grafo se ajusta automáticamente al contenedor

---

## 🔧 Configuración Técnica

### Parámetros del Layout COSE

```javascript
{
    name: 'cose',
    idealEdgeLength: 100,      // Longitud ideal de aristas
    nodeOverlap: 20,           // Espacio mínimo entre nodos
    nodeRepulsion: 400000,     // Fuerza de repulsión entre nodos
    edgeElasticity: 100,       // Elasticidad de las aristas
    gravity: 80,               // Atracción hacia el centro
    numIter: 1000,             // Iteraciones del algoritmo
    coolingFactor: 0.95,       // Factor de enfriamiento
    padding: 30                // Espacio alrededor del grafo
}
```

### Deduplicación de Aristas

Para grafos no dirigidos, se evitan aristas duplicadas:

```javascript
const edgeKey = [edge.from, edge.to].sort().join('-');
if (!edgeSet.has(edgeKey)) {
    edgeSet.add(edgeKey);
    // Add edge
}
```

---

## 📱 Responsive Design

- Canvas ajustable: 100% del ancho del contenedor
- Altura fija: 600px (optimizado para visualización)
- Leyenda adaptable con grid responsive
- Funciona en dispositivos móviles y desktop

---

## 🚀 Uso

### Paso 1: Navegar al tab "Redes & MST"
```
Dashboard → Redes & MST
```

### Paso 2: Hacer click en "Mostrar Grafo de Conexiones"
```
🗺️ Visualización del Grafo de Shelters
[Mostrar Grafo de Conexiones]
```

### Paso 3: Interactuar con el grafo
- Arrastra nodos para reorganizar
- Haz zoom con el scroll
- Selecciona nodos para ver detalles
- Click nuevamente en el botón para ocultar

---

## 🎯 Casos de Uso

1. **Análisis de Red**
   - Visualizar la topología completa de shelters
   - Identificar clusters y regiones
   - Detectar nodos centrales (hubs)

2. **Planificación de Rutas**
   - Ver todas las conexiones disponibles
   - Comparar distancias entre shelters
   - Identificar rutas alternativas

3. **Debugging**
   - Verificar que todas las conexiones existen
   - Validar pesos de aristas
   - Comprobar conectividad del grafo

4. **Presentaciones**
   - Mostrar la red de forma visual e intuitiva
   - Explicar algoritmos sobre el grafo real
   - Comparar MST vs grafo completo

---

## 📊 Datos del Grafo

Con los datos actuales de DbSeed:
- **15 Shelters** (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)
- **39 Aristas** (conexiones NEAR con distancias)
- **4 Regiones** + 1 Hub central

**Topología**:
- Región Norte: A-B-C-D
- Región Sur: E-F-G-H
- Región Este: I-J-K
- Región Oeste: L-M-N-O
- Hub: Nodo central conectado a múltiples regiones

---

## 🔍 Ejemplo de Respuesta del Endpoint

```json
{
  "message": "Graph data loaded successfully",
  "nodes": [
    "A", "B", "C", "D", "E", "F", "G", "H",
    "I", "J", "K", "L", "M", "N", "O"
  ],
  "edges": [
    {"from": "A", "to": "B", "weight": 15.5},
    {"from": "B", "to": "C", "weight": 12.0},
    {"from": "C", "to": "D", "weight": 10.5},
    {"from": "A", "to": "E", "weight": 35.0},
    // ... más aristas
  ]
}
```

---

## 🐛 Troubleshooting

### El grafo no se muestra
1. Verificar que el servidor esté corriendo en `localhost:8080`
2. Abrir la consola del navegador (F12) para ver errores
3. Verificar que el endpoint `/network/graph` responda correctamente

### Los nodos se superponen
1. Hacer click en "Mostrar Grafo" nuevamente para recalcular layout
2. Ajustar parámetros de `nodeRepulsion` o `idealEdgeLength` si es necesario

### Las etiquetas no se ven
1. Hacer zoom para ver mejor
2. Los pesos se muestran en las aristas automáticamente

---

## 🔄 Comparación: Grafo Completo vs MST

| Característica | Grafo Completo | MST (Kruskal/Prim) |
|----------------|----------------|---------------------|
| Nodos | Todos los shelters | Todos los shelters |
| Aristas | Todas las conexiones (39) | Mínimas para conectar (14) |
| Peso total | ~600 km | ~220 km |
| Rutas | Múltiples caminos | Un único camino entre nodos |
| Uso | Ver topología completa | Optimizar red de conexiones |

---

## 📚 Tecnologías Utilizadas

- **Cytoscape.js**: v3.28.1
- **Layout Algorithm**: COSE (Compound Spring Embedder)
- **Spring Boot**: Backend REST API
- **Neo4j**: Base de datos de grafos
- **Vanilla JavaScript**: Sin frameworks adicionales

---

## ✅ Próximas Mejoras Potenciales

1. **Filtros**: Filtrar aristas por distancia mínima/máxima
2. **Búsqueda**: Buscar nodos específicos
3. **Highlight Path**: Resaltar camino entre dos nodos seleccionados
4. **Export**: Exportar imagen PNG del grafo
5. **Layouts Alternativos**: Circle, Grid, Breadthfirst
6. **Tooltips**: Información detallada al hover
7. **Animaciones**: Resaltar nuevas aristas del MST
8. **Comparación Visual**: Mostrar MST vs grafo completo lado a lado

---

## 📝 Archivos Modificados

1. `/src/main/java/com/programacion3/adoptme/controller/NetworkController.java`
   - Nuevo endpoint `GET /network/graph`
   - Nuevo DTO `GraphResponse`

2. `/frontend/index.html`
   - CDN de Cytoscape.js
   - Sección de visualización con contenedor
   - Leyenda interactiva

3. `/frontend/app.js`
   - Función `showGraphVisualization()`
   - Configuración de Cytoscape
   - Estilos y layout

---

## 🎉 Resultado

Una visualización interactiva y profesional del grafo de shelters que permite:
- ✅ Ver todas las conexiones de forma visual
- ✅ Interactuar con el grafo (drag, zoom, selección)
- ✅ Entender la topología de la red
- ✅ Complementar los algoritmos MST y rutas

**¡Ahora los usuarios pueden explorar visualmente la red completa de shelters!** 🚀
