package com.estrutura.dados.graph;

import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 *  Graph (Grafo) — Implementação Didática em Java
 *  Estrutura de Dados | Lista de Adjacência
 *
 *  Complexidade das Operações:
 *  ┌──────────────────┬────────────┬─────────────┐
 *  │  Operação        │ Lista Adj. │ Matriz Adj. │
 *  ├──────────────────┼────────────┼─────────────┤
 *  │  addVertex()     │   O(1)     │   O(V²)     │
 *  │  addEdge()       │   O(1)     │   O(1)      │
 *  │  BFS()           │   O(V+E)   │   O(V²)     │
 *  │  DFS()           │   O(V+E)   │   O(V²)     │
 *  │  hasEdge()       │   O(V)     │   O(1)      │
 *  │  getNeighbors()  │   O(1)     │   O(V)      │
 *  └──────────────────┴────────────┴─────────────┘
 *  Espaço: O(V + E)
 *
 *  Série: Estruturas de Dados em Java — Ep.08
 *  Autor: @seulinkedin
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class GraphExemplo {

    // ─────────────────────────────────────────────────────────────
    //  1. Graph — Lista de Adjacência com suporte a
    //     grafos dirigidos (directed) e não dirigidos
    // ─────────────────────────────────────────────────────────────
    static class Graph {

        private final Map<Integer, List<Integer>> adjList;
        private final boolean directed;

        /**
         * @param directed true = grafo dirigido (A→B ≠ B→A)
         *                 false = não dirigido    (A-B = B-A)
         */
        public Graph(boolean directed) {
            this.adjList  = new HashMap<>();
            this.directed = directed;
        }

        // ── addVertex ─────────────────────────────────────────────
        /**
         * Adiciona um vértice ao grafo.
         * Complexidade: O(1)
         */
        public void addVertex(int v) {
            adjList.putIfAbsent(v, new ArrayList<>());
        }

        // ── addEdge ───────────────────────────────────────────────
        /**
         * Adiciona uma aresta entre dois vértices.
         * Complexidade: O(1)
         * Se não dirigido, adiciona nos dois sentidos.
         */
        public void addEdge(int from, int to) {
            adjList.putIfAbsent(from, new ArrayList<>());
            adjList.putIfAbsent(to,   new ArrayList<>());

            adjList.get(from).add(to);

            if (!directed) {
                adjList.get(to).add(from);
            }
        }

        // ── hasEdge ───────────────────────────────────────────────
        /**
         * Verifica se existe aresta entre dois vértices.
         * Complexidade: O(V) — percorre a lista do vértice
         */
        public boolean hasEdge(int from, int to) {
            List<Integer> neighbors = adjList.get(from);
            return neighbors != null && neighbors.contains(to);
        }

        // ── getNeighbors ──────────────────────────────────────────
        /**
         * Retorna os vizinhos de um vértice.
         * Complexidade: O(1)
         */
        public List<Integer> getNeighbors(int v) {
            return adjList.getOrDefault(v, Collections.emptyList());
        }

        // ── BFS ───────────────────────────────────────────────────
        /**
         * Busca em Largura (Breadth-First Search).
         * Percorre nível por nível usando uma Queue.
         * Complexidade: O(V + E)
         *
         * Usa: menor caminho (não ponderado), nível de separação,
         *      sugestão de conexões (LinkedIn, Instagram)
         */
        public List<Integer> bfs(int start) {
            List<Integer>    visited = new ArrayList<>();
            Set<Integer>     seen    = new HashSet<>();
            Queue<Integer>   queue   = new LinkedList<>();

            seen.add(start);
            queue.offer(start);

            while (!queue.isEmpty()) {
                int current = queue.poll();
                visited.add(current);

                for (int neighbor : getNeighbors(current)) {
                    if (!seen.contains(neighbor)) {
                        seen.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }
            return visited;
        }

        // ── DFS iterativo ─────────────────────────────────────────
        /**
         * Busca em Profundidade (Depth-First Search) — iterativo.
         * Usa uma Stack explícita para evitar StackOverflow em
         * grafos muito grandes.
         * Complexidade: O(V + E)
         *
         * Usa: detectar ciclos, componentes conexos,
         *      ordenação topológica
         */
        public List<Integer> dfs(int start) {
            List<Integer>  visited = new ArrayList<>();
            Set<Integer>   seen    = new HashSet<>();
            Deque<Integer> stack   = new ArrayDeque<>();

            stack.push(start);

            while (!stack.isEmpty()) {
                int current = stack.pop();

                if (seen.contains(current)) continue;

                seen.add(current);
                visited.add(current);

                // Adiciona vizinhos na ordem reversa para
                // manter a ordem de visita equivalente ao DFS recursivo
                List<Integer> neighbors = getNeighbors(current);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    if (!seen.contains(neighbors.get(i))) {
                        stack.push(neighbors.get(i));
                    }
                }
            }
            return visited;
        }

        // ── DFS recursivo ─────────────────────────────────────────
        /**
         * Busca em Profundidade — versão recursiva.
         * Mais legível para grafos de tamanho moderado.
         * Complexidade: O(V + E)
         */
        public List<Integer> dfsRecursive(int start) {
            List<Integer> visited = new ArrayList<>();
            Set<Integer>  seen    = new HashSet<>();
            dfsHelper(start, seen, visited);
            return visited;
        }

        private void dfsHelper(int v, Set<Integer> seen, List<Integer> visited) {
            seen.add(v);
            visited.add(v);
            for (int neighbor : getNeighbors(v)) {
                if (!seen.contains(neighbor)) {
                    dfsHelper(neighbor, seen, visited);
                }
            }
        }

        // ── Detecção de Ciclo ─────────────────────────────────────
        /**
         * Verifica se o grafo contém algum ciclo.
         * Complexidade: O(V + E)
         *
         * Lógica: durante o DFS, se chegarmos a um nó que já
         * está na pilha de recursão atual (gray set), há ciclo.
         */
        public boolean hasCycle() {
            Set<Integer> visited    = new HashSet<>();
            Set<Integer> inProgress = new HashSet<>(); // "cinza" — em processamento

            for (int v : adjList.keySet()) {
                if (!visited.contains(v)) {
                    if (hasCycleHelper(v, visited, inProgress)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean hasCycleHelper(int v, Set<Integer> visited,
                                        Set<Integer> inProgress) {
            visited.add(v);
            inProgress.add(v);

            for (int neighbor : getNeighbors(v)) {
                if (!visited.contains(neighbor)) {
                    if (hasCycleHelper(neighbor, visited, inProgress)) return true;
                } else if (inProgress.contains(neighbor)) {
                    return true; // ← ciclo encontrado!
                }
            }

            inProgress.remove(v);
            return false;
        }

        // ── Componentes Conexos ───────────────────────────────────
        /**
         * Conta e retorna todos os componentes conexos do grafo.
         * Útil para grafos não dirigidos desconectados.
         * Complexidade: O(V + E)
         */
        public List<List<Integer>> connectedComponents() {
            List<List<Integer>> components = new ArrayList<>();
            Set<Integer>        visited    = new HashSet<>();

            for (int v : adjList.keySet()) {
                if (!visited.contains(v)) {
                    List<Integer> component = new ArrayList<>();
                    dfsComponentHelper(v, visited, component);
                    components.add(component);
                }
            }
            return components;
        }

        private void dfsComponentHelper(int v, Set<Integer> visited,
                                         List<Integer> component) {
            visited.add(v);
            component.add(v);
            for (int neighbor : getNeighbors(v)) {
                if (!visited.contains(neighbor)) {
                    dfsComponentHelper(neighbor, visited, component);
                }
            }
        }

        // ── Menor Caminho (BFS) ───────────────────────────────────
        /**
         * Retorna o menor caminho entre dois vértices.
         * Funciona apenas em grafos NÃO ponderados.
         * Complexidade: O(V + E)
         */
        public List<Integer> shortestPath(int start, int end) {
            if (start == end) return List.of(start);

            Map<Integer, Integer> parent = new HashMap<>();
            Queue<Integer>        queue  = new LinkedList<>();
            Set<Integer>          seen   = new HashSet<>();

            seen.add(start);
            queue.offer(start);
            parent.put(start, -1);

            while (!queue.isEmpty()) {
                int current = queue.poll();

                if (current == end) {
                    return buildPath(parent, start, end);
                }

                for (int neighbor : getNeighbors(current)) {
                    if (!seen.contains(neighbor)) {
                        seen.add(neighbor);
                        parent.put(neighbor, current);
                        queue.offer(neighbor);
                    }
                }
            }
            return Collections.emptyList(); // sem caminho
        }

        private List<Integer> buildPath(Map<Integer, Integer> parent,
                                         int start, int end) {
            LinkedList<Integer> path = new LinkedList<>();
            int current = end;

            while (current != -1) {
                path.addFirst(current);
                current = parent.get(current);
            }
            return path;
        }

        // ── toString ──────────────────────────────────────────────
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(
                directed ? "Grafo Dirigido:\n" : "Grafo Não Dirigido:\n"
            );
            for (Map.Entry<Integer, List<Integer>> entry : adjList.entrySet()) {
                sb.append("  ").append(entry.getKey())
                  .append(" → ").append(entry.getValue()).append("\n");
            }
            return sb.toString();
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  2. Caso de Uso Real: Mapa de Cidades (grafo não dirigido)
    // ─────────────────────────────────────────────────────────────
    static void simularMapaCidades() {
        System.out.println("╔══ CASO DE USO: Mapa de Rotas entre Cidades ══╗");

        Graph mapa = new Graph(false); // não dirigido

        // Cidades como vértices (IDs)
        // 1=SP | 2=RJ | 3=BH | 4=Curitiba | 5=Salvador | 6=Brasília
        mapa.addEdge(1, 2); // SP ↔ RJ
        mapa.addEdge(1, 3); // SP ↔ BH
        mapa.addEdge(1, 4); // SP ↔ Curitiba
        mapa.addEdge(2, 3); // RJ ↔ BH
        mapa.addEdge(3, 6); // BH ↔ Brasília
        mapa.addEdge(5, 6); // Salvador ↔ Brasília

        System.out.println("\n" + mapa);

        System.out.println("BFS a partir de SP (1): " + mapa.bfs(1));
        System.out.println("DFS a partir de SP (1): " + mapa.dfs(1));

        List<Integer> caminho = mapa.shortestPath(1, 5);
        System.out.println("Menor caminho SP(1) → Salvador(5): " + caminho);

        System.out.println("Componentes conexos: " + mapa.connectedComponents());
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────────────────────────
    //  3. Caso de Uso Real: Dependências de Módulos (dirigido + ciclo)
    // ─────────────────────────────────────────────────────────────
    static void simularDependencias() {
        System.out.println("\n╔══ CASO DE USO: Dependências entre Módulos ══╗");

        Graph deps = new Graph(true); // dirigido

        // Módulos: 1=Auth | 2=User | 3=Order | 4=Payment | 5=Notification
        deps.addEdge(3, 4); // Order  → Payment
        deps.addEdge(3, 2); // Order  → User
        deps.addEdge(4, 1); // Payment → Auth
        deps.addEdge(2, 1); // User   → Auth
        deps.addEdge(3, 5); // Order  → Notification

        System.out.println(deps);

        System.out.println("Tem ciclo? " + (deps.hasCycle() ? "❌ Sim" : "✅ Não"));

        // Simulando dependência circular
        deps.addEdge(1, 3); // Auth → Order (cria ciclo!)
        System.out.println("Após Auth→Order, tem ciclo? " +
                (deps.hasCycle() ? "❌ Sim — dependência circular detectada!" : "✅ Não"));

        System.out.println("╚══════════════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────────────────────────
    //  4. Main
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("════════════════════════════════════════════════");
        System.out.println("   Graph — Lista de Adjacência | Ep.08          ");
        System.out.println("════════════════════════════════════════════════\n");

        // ── Operações básicas ─────────────────────────────────────
        Graph g = new Graph(false);

        System.out.println("▶ Adicionando vértices e arestas:");
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        System.out.println(g);

        System.out.println("▶ hasEdge(1, 2): " + g.hasEdge(1, 2));
        System.out.println("▶ hasEdge(1, 5): " + g.hasEdge(1, 5));

        System.out.println("\n▶ BFS a partir do vértice 1: " + g.bfs(1));
        System.out.println("▶ DFS iterativo a partir de 1: " + g.dfs(1));
        System.out.println("▶ DFS recursivo a partir de 1: " + g.dfsRecursive(1));

        System.out.println("\n▶ Menor caminho 1 → 5: " + g.shortestPath(1, 5));
        System.out.println("▶ Tem ciclo? " + (g.hasCycle() ? "Sim" : "Não"));
        System.out.println("▶ Componentes conexos: " + g.connectedComponents());

        // ── Casos de uso reais ────────────────────────────────────
        System.out.println();
        simularMapaCidades();
        simularDependencias();

        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("   Graph implementado com sucesso! 🚀            ");
        System.out.println("════════════════════════════════════════════════");
    }
}