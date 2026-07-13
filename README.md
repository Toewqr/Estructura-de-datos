# Simulador de Gestión de Mudanzas
## Descripción

**Sistema integral de simulación logística** para una empresa de mudanzas. Permite gestionar ciudades, rutas, clientes y pedidos, optimizando el transporte mediante algoritmos de búsqueda en grafos y estructuras de datos eficientes.

El proyecto fue desarrollado íntegramente en **Java** aplicando los principios de la **Programación Orientada a Objetos** y construyendo **estructuras de datos desde cero** (Árbol AVL, Grafo, Lista Enlazada, Cola) para garantizar un control total sobre el rendimiento y la funcionalidad.

##  Características

- **Gestión de Ciudades** – ABM (Alta, Baja, Modificación) sobre un árbol AVL balanceado.
- **Gestión de Rutas** – Grafo no dirigido con pesos (distancias en km). Permite:
  - Alta/Baja de rutas.
  - Consulta de conexión directa.
  - Camino con **menos ciudades** (BFS).
  - Camino con **menor distancia** (backtracking con poda).
  - Caminos que **pasan por una ciudad intermedia** (DFS).
  - Verificación de **distancia máxima** permitida.
  - **Camino perfecto**: secuencia de ciudades que permite transportar pedidos sin exceder la capacidad del camión.
  - **Planificación de viajes**: cálculo de carga adicional posible en una ruta.
- **Gestión de Clientes** – ABM con búsqueda por documento (HashMap).
- **Carga de datos desde archivos** – Ciudades, clientes, rutas y solicitudes en formato CSV.
- **Registro de eventos** – Logger que registra todas las operaciones significativas.
- **Visualización de estructuras** – Muestra el árbol AVL y el grafo de forma legible en consola.

##  Tecnologías y Conceptos

- **Lenguaje**: Java 17
- **Estructuras de Datos** (implementadas manualmente):
  - Árbol AVL (balanceado)
  - Grafo (lista de adyacencia)
  - Lista enlazada simple
  - Cola
- **Algoritmos**:
  - BFS (Búsqueda en Anchura) – para caminos con menos vértices.
  - DFS (Búsqueda en Profundidad) – para caminos que pasan por una ciudad intermedia.
  - Backtracking con poda – para camino de menor distancia.
  - Algoritmo de planificación de carga (simulación de espacio en camión).
- **Modelado**: POO con clases cohesivas (`Ciudad`, `Cliente`, `Pedido`, `GrafoCiudades`, `ArbolAVL`, etc.).




