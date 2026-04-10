import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**

- Implementação completa de Heap (Min-Heap e Max-Heap) em Java.
- 
- <p>Estrutura de dados baseada em árvore binária completa armazenada
- internamente como array. A propriedade do heap garante que o elemento
- de maior ou menor prioridade esteja sempre na raiz (índice 0).
- 
- <p>Relações de índice no array:
- <ul>
- <li>Pai de i       → (i - 1) / 2</li>
- <li>Filho esquerdo → 2 * i + 1</li>
- <li>Filho direito  → 2 * i + 2</li>
- </ul>
- 
- <p>Complexidades:
- <ul>
- <li>insert (offer) → O(log n)</li>
- <li>remove (poll)  → O(log n)</li>
- <li>peek           → O(1)</li>
- <li>busca          → O(n)</li>
- <li>build heap     → O(n)</li>
- </ul>
- 
- @param <T> tipo dos elementos armazenados no heap
- @author Anderson Nogueira
  */
  public class Heap<T> {
  
  // ─────────────────────────────────────────────
  // Campos internos
  // ─────────────────────────────────────────────
  
  private final List<T> data;
  private final Comparator<T> comparator;
  
  // ─────────────────────────────────────────────
  // Construtores
  // ─────────────────────────────────────────────
  
  /**
  - Cria um heap vazio com o comparador fornecido.
  - 
  - <p>Para Min-Heap: {@code Comparator.naturalOrder()}
  - <br>Para Max-Heap: {@code Comparator.reverseOrder()}
  - 
  - @param comparator define a ordem de prioridade
    */
    public Heap(Comparator<T> comparator) {
    this.data = new ArrayList<>();
    this.comparator = comparator;
    }
  
  /**
  - Cria um heap a partir de uma lista existente — Build Heap.
  - 
  - <p>Utiliza o algoritmo de heapify bottom-up, que é O(n),
  - mais eficiente do que inserir elemento por elemento O(n log n).
  - 
  - @param items      lista de elementos iniciais
  - @param comparator define a ordem de prioridade
  - @complexity O(n) — build heap bottom-up
    */
    public Heap(List<T> items, Comparator<T> comparator) {
    this.data = new ArrayList<>(items);
    this.comparator = comparator;
    buildHeap();
    }
  
  // ─────────────────────────────────────────────
  // API pública
  // ─────────────────────────────────────────────
  
  /**
  - Insere um elemento no heap, mantendo a propriedade heap.
  - 
  - @param item elemento a ser inserido
  - @throws IllegalArgumentException se item for null
  - @complexity O(log n) — sift up
    */
    public void offer(T item) {
    if (item == null) throw new IllegalArgumentException(“Elemento não pode ser null.”);
    data.add(item);
    siftUp(data.size() - 1);
    }
  
  /**
  - Remove e retorna o elemento de maior prioridade (raiz).
  - 
  - @return elemento de maior prioridade
  - @throws NoSuchElementException se o heap estiver vazio
  - @complexity O(log n) — sift down
    */
    public T poll() {
    if (isEmpty()) throw new NoSuchElementException(“Heap está vazio.”);
    
    T top = data.get(0);
    int last = data.size() - 1;
    
    // Move o último elemento para a raiz e faz sift down
    swap(0, last);
    data.remove(last);
    
    if (!isEmpty()) siftDown(0);
    
    return top;
    }
  
  /**
  - Retorna (sem remover) o elemento de maior prioridade.
  - 
  - @return elemento no topo do heap
  - @throws NoSuchElementException se o heap estiver vazio
  - @complexity O(1)
    */
    public T peek() {
    if (isEmpty()) throw new NoSuchElementException(“Heap está vazio.”);
    return data.get(0);
    }
  
  /**
  - Retorna o número de elementos no heap.
  - 
  - @return tamanho atual
  - @complexity O(1)
    */
    public int size() {
    return data.size();
    }
  
  /**
  - Verifica se o heap está vazio.
  - 
  - @return true se não houver elementos
  - @complexity O(1)
    */
    public boolean isEmpty() {
    return data.isEmpty();
    }
  
  /**
  - Verifica se o elemento está presente no heap.
  - 
  - @param item elemento a buscar
  - @return true se encontrado
  - @complexity O(n) — busca linear (sem ordem garantida exceto na raiz)
    */
    public boolean contains(T item) {
    return data.contains(item);
    }
  
  /**
  - Retorna todos os elementos em ordem de prioridade (destrói o heap).
  - 
  - @return lista ordenada por prioridade
  - @complexity O(n log n)
    */
    public List<T> drainSorted() {
    List<T> sorted = new ArrayList<>();
    while (!isEmpty()) sorted.add(poll());
    return sorted;
    }
  
  // ─────────────────────────────────────────────
  // Operações internas
  // ─────────────────────────────────────────────
  
  /**
  - Build Heap bottom-up — O(n).
  - Começa no último nó não-folha e aplica siftDown em cada um.
    */
    private void buildHeap() {
    int lastNonLeaf = (data.size() / 2) - 1;
    for (int i = lastNonLeaf; i >= 0; i–) {
    siftDown(i);
    }
    }
  
  /**
  - Sobe o elemento no índice {@code i} até restaurar a propriedade heap.
  - Usado após insert.
  - 
  - @complexity O(log n)
    */
    private void siftUp(int i) {
    while (i > 0) {
    int parent = (i - 1) / 2;
    if (hasHigherPriority(i, parent)) {
    swap(i, parent);
    i = parent;
    } else {
    break;
    }
    }
    }
  
  /**
  - Desce o elemento no índice {@code i} até restaurar a propriedade heap.
  - Usado após poll (remoção da raiz).
  - 
  - @complexity O(log n)
    */
    private void siftDown(int i) {
    int size = data.size();
    while (true) {
    int left  = 2 * i + 1;
    int right = 2 * i + 2;
    int target = i;
    
    ```
     if (left < size && hasHigherPriority(left, target))  target = left;
     if (right < size && hasHigherPriority(right, target)) target = right;
    
     if (target == i) break;
    
     swap(i, target);
     i = target;
    ```
    
    }
    }
  
  /**
  - Retorna true se o elemento em {@code a} tem maior prioridade que o em {@code b},
  - conforme o comparator definido.
    */
    private boolean hasHigherPriority(int a, int b) {
    return comparator.compare(data.get(a), data.get(b)) < 0;
    }
  
  private void swap(int a, int b) {
  T temp = data.get(a);
  data.set(a, data.get(b));
  data.set(b, temp);
  }
  
  // ─────────────────────────────────────────────
  // Factories estáticas
  // ─────────────────────────────────────────────
  
  /**
  - Cria um Min-Heap para tipos Comparable (menor = maior prioridade).
  - 
  - @param <T> tipo Comparable
  - @return Min-Heap vazio
    */
    public static <T extends Comparable<T>> Heap<T> minHeap() {
    return new Heap<>(Comparator.<T>naturalOrder());
    }
  
  /**
  - Cria um Max-Heap para tipos Comparable (maior = maior prioridade).
  - 
  - @param <T> tipo Comparable
  - @return Max-Heap vazio
    */
    public static <T extends Comparable<T>> Heap<T> maxHeap() {
    return new Heap<>(Comparator.<T>reverseOrder());
    }
  
  // ─────────────────────────────────────────────
  // toString
  // ─────────────────────────────────────────────
  
  @Override
  public String toString() {
  return “Heap” + data.toString();
  }
  
  // ══════════════════════════════════════════════
  //  SIMULAÇÃO — Triagem Hospitalar por Prioridade
  // ══════════════════════════════════════════════
  
  /**
  - Representa um paciente com nome e nível de criticidade.
  - Quanto MENOR o nível, MAIS crítico o paciente.
  - 
  - <p>Níveis:
  - <ul>
  - <li>1 — Vermelho (emergência)</li>
  - <li>2 — Laranja  (muito urgente)</li>
  - <li>3 — Amarelo  (urgente)</li>
  - <li>4 — Verde    (pouco urgente)</li>
  - <li>5 — Azul     (não urgente)</li>
  - </ul>
  
  */
  static class Paciente implements Comparable<Paciente> {
  
  ```
   private final String nome;
   private final int    criticidade;
   private final String cor;
  
   Paciente(String nome, int criticidade) {
       this.nome        = nome;
       this.criticidade = criticidade;
       this.cor         = switch (criticidade) {
           case 1 -> "🔴 Vermelho";
           case 2 -> "🟠 Laranja";
           case 3 -> "🟡 Amarelo";
           case 4 -> "🟢 Verde";
           default -> "🔵 Azul";
       };
   }
  
   @Override
   public int compareTo(Paciente outro) {
       return Integer.compare(this.criticidade, outro.criticidade);
   }
  
   @Override
   public String toString() {
       return String.format("%-20s | Criticidade: %d | %s", nome, criticidade, cor);
   }
  ```
  
  }
  
  // ─────────────────────────────────────────────
  // main — demonstração completa
  // ─────────────────────────────────────────────
  
  public static void main(String[] args) {
  
  ```
   System.out.println("══════════════════════════════════════════════");
   System.out.println("  DEMO 1 — Min-Heap com inteiros");
   System.out.println("══════════════════════════════════════════════");
  
   Heap<Integer> minHeap = Heap.minHeap();
   int[] valores = {15, 3, 22, 8, 1, 42, 7};
  
   System.out.print("Inserindo: ");
   for (int v : valores) {
       System.out.print(v + " ");
       minHeap.offer(v);
   }
  
   System.out.println("\nTopo (peek): " + minHeap.peek());
   System.out.print("Removendo em ordem: ");
   while (!minHeap.isEmpty()) System.out.print(minHeap.poll() + " ");
  
   System.out.println("\n");
  
   // ──────────────────────────────────────────
  
   System.out.println("══════════════════════════════════════════════");
   System.out.println("  DEMO 2 — Max-Heap com inteiros");
   System.out.println("══════════════════════════════════════════════");
  
   Heap<Integer> maxHeap = Heap.maxHeap();
   for (int v : valores) maxHeap.offer(v);
  
   System.out.println("Topo (peek): " + maxHeap.peek());
   System.out.print("Removendo em ordem: ");
   while (!maxHeap.isEmpty()) System.out.print(maxHeap.poll() + " ");
  
   System.out.println("\n");
  
   // ──────────────────────────────────────────
  
   System.out.println("══════════════════════════════════════════════");
   System.out.println("  DEMO 3 — Build Heap O(n) a partir de lista");
   System.out.println("══════════════════════════════════════════════");
  
   List<Integer> lista = List.of(50, 20, 80, 10, 60, 30, 70);
   Heap<Integer> heapFromList = new Heap<>(lista, Comparator.naturalOrder());
  
   System.out.println("Build heap a partir de: " + lista);
   System.out.print("Drenando ordenado:      ");
   heapFromList.drainSorted().forEach(v -> System.out.print(v + " "));
  
   System.out.println("\n");
  
   // ──────────────────────────────────────────
  
   System.out.println("══════════════════════════════════════════════");
   System.out.println("  DEMO 4 — Triagem Hospitalar (Min-Heap)");
   System.out.println("══════════════════════════════════════════════");
  
   Heap<Paciente> triagem = Heap.minHeap();
  
   // Pacientes chegando em ordem aleatória de entrada
   triagem.offer(new Paciente("Carlos Silva",    4));
   triagem.offer(new Paciente("Ana Beatriz",     2));
   triagem.offer(new Paciente("Roberto Farias",  5));
   triagem.offer(new Paciente("Mariana Costa",   1));
   triagem.offer(new Paciente("Felipe Souza",    3));
   triagem.offer(new Paciente("Juliana Melo",    1));
   triagem.offer(new Paciente("Pedro Almeida",   2));
  
   System.out.println("Pacientes na fila: " + triagem.size());
   System.out.println("Próximo a ser atendido: " + triagem.peek());
   System.out.println("\nOrdem de atendimento:");
   System.out.println("─".repeat(55));
  
   int posicao = 1;
   while (!triagem.isEmpty()) {
       System.out.printf("%d. %s%n", posicao++, triagem.poll());
   }
  
   System.out.println("─".repeat(55));
   System.out.println("\nTodos os pacientes foram atendidos! ✅");
  
   // ──────────────────────────────────────────
  
   System.out.println("\n══════════════════════════════════════════════");
   System.out.println("  DEMO 5 — Top K maiores elementos (K=3)");
   System.out.println("══════════════════════════════════════════════");
  
   int[] stream = {4, 2, 9, 7, 5, 6, 1, 3, 8};
   int k = 3;
  
   // Min-Heap de tamanho K: mantém os K maiores
   Heap<Integer> topK = Heap.minHeap();
  
   System.out.print("Stream de entrada: ");
   for (int n : stream) System.out.print(n + " ");
   System.out.println();
  
   for (int n : stream) {
       topK.offer(n);
       if (topK.size() > k) topK.poll(); // remove o menor
   }
  
   System.out.print("Top " + k + " maiores: ");
   topK.drainSorted().forEach(v -> System.out.print(v + " "));
   System.out.println("\n(LeetCode #215 e #347 — padrão Top K)\n");
  ```
  
  }
  }