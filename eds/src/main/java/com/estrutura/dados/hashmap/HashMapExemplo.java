package com.estrutura.dados.hashmap;

import java.util.ArrayList;
import java.util.List;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 *  HashMap — Implementação Didática em Java
 *  Estrutura de Dados | Tabela Hash com Encadeamento Separado
 *
 *  Complexidade das Operações:
 *  ┌─────────────┬────────────┬────────────┐
 *  │  Operação   │  Melhor    │   Pior     │
 *  ├─────────────┼────────────┼────────────┤
 *  │  put()      │   O(1)     │   O(n)     │
 *  │  get()      │   O(1)     │   O(n)     │
 *  │  remove()   │   O(1)     │   O(n)     │
 *  │  containsKey│   O(1)     │   O(n)     │
 *  │  size()     │   O(1)     │   O(1)     │
 *  └─────────────┴────────────┴────────────┘
 *  Espaço: O(n)
 *
 *  O pior caso O(n) ocorre quando há muitas colisões e todos os
 *  elementos caem no mesmo bucket (lista encadeada degenerada).
 *  Na prática, com boa função hash e load factor adequado, as
 *  operações se comportam como O(1) amortizado.
 *
 *  Série: Estruturas de Dados em Java — Ep.05
 *  Autor: @seulinkedin
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class HashMapExemplo {

    // ─────────────────────────────────────────
    //  1. Entry: par chave-valor armazenado
    //     em cada bucket (nó da lista encadeada)
    // ─────────────────────────────────────────
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next; // encadeamento para colisões

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // ─────────────────────────────────────────
    //  2. HashMap com Separate Chaining
    // ─────────────────────────────────────────
    static class HashMap<K, V> {

        private static final int    DEFAULT_CAPACITY   = 16;
        private static final double DEFAULT_LOAD_FACTOR = 0.75;

        private Entry<K, V>[] buckets;
        private int size;
        private final double loadFactor;

        @SuppressWarnings("unchecked")
        public HashMap() {
            this.buckets    = new Entry[DEFAULT_CAPACITY];
            this.size       = 0;
            this.loadFactor = DEFAULT_LOAD_FACTOR;
        }

        // ── Função hash: distribui chaves pelos buckets ──
        private int hash(K key) {
            if (key == null) return 0;
            int h = key.hashCode();
            // Reduz colisões espalhando bits altos e baixos
            h = h ^ (h >>> 16);
            return Math.abs(h % buckets.length);
        }

        /**
         * Insere ou atualiza um par chave-valor.
         * Complexidade: O(1) amortizado | O(n) pior caso (colisões)
         */
        public void put(K key, V value) {
            if ((double) size / buckets.length >= loadFactor) {
                resize();
            }

            int index = hash(key);
            Entry<K, V> current = buckets[index];

            // Verifica se a chave já existe → atualiza valor
            while (current != null) {
                if (keyEquals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }

            // Insere no início do bucket (O(1))
            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next  = buckets[index];
            buckets[index] = newEntry;
            size++;
        }

        /**
         * Retorna o valor associado à chave, ou null se não encontrado.
         * Complexidade: O(1) amortizado | O(n) pior caso
         */
        public V get(K key) {
            int index = hash(key);
            Entry<K, V> current = buckets[index];

            while (current != null) {
                if (keyEquals(current.key, key)) return current.value;
                current = current.next;
            }
            return null;
        }

        /**
         * Remove a entrada com a chave fornecida.
         * Complexidade: O(1) amortizado | O(n) pior caso
         */
        public V remove(K key) {
            int index = hash(key);
            Entry<K, V> current = buckets[index];
            Entry<K, V> prev    = null;

            while (current != null) {
                if (keyEquals(current.key, key)) {
                    if (prev == null) buckets[index] = current.next;
                    else              prev.next = current.next;
                    size--;
                    return current.value;
                }
                prev    = current;
                current = current.next;
            }
            return null;
        }

        /**
         * Verifica se a chave existe no mapa.
         * Complexidade: O(1) amortizado | O(n) pior caso
         */
        public boolean containsKey(K key) {
            return get(key) != null;
        }

        /** Complexidade: O(1) */
        public int size()      { return size; }

        /** Complexidade: O(1) */
        public boolean isEmpty() { return size == 0; }

        /**
         * Retorna todas as chaves presentes no mapa.
         * Complexidade: O(n) — percorre todos os buckets
         */
        public List<K> keys() {
            List<K> result = new ArrayList<>();
            for (Entry<K, V> bucket : buckets) {
                Entry<K, V> current = bucket;
                while (current != null) {
                    result.add(current.key);
                    current = current.next;
                }
            }
            return result;
        }

        // ── Redimensiona quando load factor é atingido ──
        @SuppressWarnings("unchecked")
        private void resize() {
            Entry<K, V>[] oldBuckets = buckets;
            buckets = new Entry[oldBuckets.length * 2];
            size    = 0;

            for (Entry<K, V> bucket : oldBuckets) {
                Entry<K, V> current = bucket;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }

        private boolean keyEquals(K a, K b) {
            if (a == null) return b == null;
            return a.equals(b);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{\n");
            for (Entry<K, V> bucket : buckets) {
                Entry<K, V> current = bucket;
                while (current != null) {
                    sb.append("  ").append(current.key)
                            .append(" → ").append(current.value).append("\n");
                    current = current.next;
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }

    // ─────────────────────────────────────────
    //  3. Caso de Uso Real #1:
    //     Contador de frequência de palavras
    //     (ex: análise de logs, NLP básico)
    // ─────────────────────────────────────────
    static void contarFrequencia() {
        System.out.println("\n╔══ CASO DE USO: Frequência de Palavras ══╗");

        String[] palavras = {"java", "spring", "java", "kafka", "spring",
                "java", "redis", "kafka", "java", "docker"};

        HashMap<String, Integer> frequencia = new HashMap<>();

        for (String palavra : palavras) {
            Integer count = frequencia.get(palavra);
            frequencia.put(palavra, count == null ? 1 : count + 1);
        }

        System.out.println(frequencia);
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────
    //  4. Caso de Uso Real #2:
    //     Cache simples (ex: memoization)
    // ─────────────────────────────────────────
    static HashMap<Integer, Long> memo = new HashMap<>();

    static long fibonacci(int n) {
        if (n <= 1) return n;
        Long cached = memo.get(n);
        if (cached != null) return cached;

        long result = fibonacci(n - 1) + fibonacci(n - 2);
        memo.put(n, result);
        return result;
    }

    static void demonstrarCache() {
        System.out.println("\n╔══ CASO DE USO: Memoization com HashMap ══╗");
        int[] ns = {10, 20, 30, 40, 50};
        for (int n : ns) {
            System.out.printf("  Fibonacci(%2d) = %d%n", n, fibonacci(n));
        }
        System.out.println("╚═══════════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────
    //  5. Main — demonstração completa
    // ─────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════════");
        System.out.println("   HashMap — Tabela Hash com Chaining       ");
        System.out.println("═══════════════════════════════════════════\n");

        HashMap<String, Integer> mapa = new HashMap<>();

        // put — O(1) amortizado
        System.out.println("▶ Put: linguagens e anos de criação");
        mapa.put("Java",   1995);
        mapa.put("Kotlin", 2011);
        mapa.put("Python", 1991);
        mapa.put("Go",     2009);
        mapa.put("Rust",   2010);
        System.out.println(mapa);

        // get — O(1) amortizado
        System.out.println("▶ Get 'Kotlin': " + mapa.get("Kotlin"));

        // atualização
        mapa.put("Java", 1996); // lançamento oficial
        System.out.println("▶ Atualiza 'Java' para 1996: " + mapa.get("Java"));

        // containsKey
        System.out.println("\n▶ ContainsKey 'Go': "    + mapa.containsKey("Go"));
        System.out.println("▶ ContainsKey 'Swift': " + mapa.containsKey("Swift"));

        // remove
        System.out.println("\n▶ Remove 'Python': " + mapa.remove("Python"));
        System.out.println("▶ Tamanho: " + mapa.size());

        // chaves
        System.out.println("\n▶ Chaves: " + mapa.keys());

        // casos de uso
        contarFrequencia();
        demonstrarCache();

        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  HashMap implementada com sucesso! 🚀      ");
        System.out.println("═══════════════════════════════════════════");
    }
}