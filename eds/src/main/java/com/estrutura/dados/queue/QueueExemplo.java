package com.estrutura.dados.queue;

import java.util.NoSuchElementException;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 *  Queue (Fila) — Implementação Didática em Java
 *  Estrutura de Dados | FIFO: First In, First Out
 *
 *  Complexidade das Operações:
 *  ┌─────────────┬────────────┬────────────┐
 *  │  Operação   │  Melhor    │   Pior     │
 *  ├─────────────┼────────────┼────────────┤
 *  │  enqueue()  │   O(1)     │   O(1)     │
 *  │  dequeue()  │   O(1)     │   O(1)     │
 *  │  peek()     │   O(1)     │   O(1)     │
 *  │  isEmpty()  │   O(1)     │   O(1)     │
 *  │  size()     │   O(1)     │   O(1)     │
 *  └─────────────┴────────────┴────────────┘
 *  Espaço: O(n)
 *
 *  Autor: @seulinkedin
 * ╚══════════════════════════════════════════════════════════════╝
 */
public class QueueExemplo {

    // ─────────────────────────────────────────
    //  1. Node: nó interno da fila encadeada
    // ─────────────────────────────────────────
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // ─────────────────────────────────────────
    //  2. Queue genérica usando LinkedList manual
    // ─────────────────────────────────────────
    static class Queue<T> {

        private Node<T> head; // frente da fila (dequeue aqui)
        private Node<T> tail; // fim da fila   (enqueue aqui)
        private int size;

        public Queue() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        /**
         * Adiciona elemento ao final da fila.
         * Complexidade: O(1) — sempre inserimos no tail.
         */
        public void enqueue(T item) {
            Node<T> newNode = new Node<>(item);

            if (isEmpty()) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        /**
         * Remove e retorna o elemento da frente da fila.
         * Complexidade: O(1) — sempre removemos do head.
         *
         * @throws NoSuchElementException se a fila estiver vazia
         */
        public T dequeue() {
            if (isEmpty()) {
                throw new NoSuchElementException("A fila está vazia!");
            }

            T data = head.data;
            head = head.next;

            if (head == null) {
                tail = null; // fila ficou vazia
            }

            size--;
            return data;
        }

        /**
         * Consulta o elemento da frente SEM removê-lo.
         * Complexidade: O(1)
         */
        public T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("A fila está vazia!");
            }
            return head.data;
        }

        /**
         * Verifica se a fila está vazia.
         * Complexidade: O(1)
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Retorna o tamanho atual da fila.
         * Complexidade: O(1)
         */
        public int size() {
            return size;
        }

        /**
         * Representação visual da fila no console.
         */
        @Override
        public String toString() {
            if (isEmpty()) return "Fila: [ vazia ]";

            StringBuilder sb = new StringBuilder("Fila (frente → fim): [ ");
            Node<T> current = head;
            while (current != null) {
                sb.append(current.data);
                if (current.next != null) sb.append(" → ");
                current = current.next;
            }
            sb.append(" ]");
            return sb.toString();
        }
    }

    // ─────────────────────────────────────────
    //  3. Caso de Uso Real: Fila de Atendimento
    //     (simulação de sistema de tickets)
    // ─────────────────────────────────────────
    record Cliente(int id, String nome) {
        @Override
        public String toString() {
            return "Cliente{id=" + id + ", nome='" + nome + "'}";
        }
    }

    static void simularAtendimento() {
        System.out.println("\n╔══ SIMULAÇÃO: Sistema de Atendimento ══╗");

        Queue<Cliente> filaAtendimento = new Queue<>();

        // Clientes chegando
        filaAtendimento.enqueue(new Cliente(1, "Ana"));
        filaAtendimento.enqueue(new Cliente(2, "Bruno"));
        filaAtendimento.enqueue(new Cliente(3, "Carlos"));
        filaAtendimento.enqueue(new Cliente(4, "Diana"));

        System.out.println(filaAtendimento);
        System.out.println("Próximo da fila → " + filaAtendimento.peek());
        System.out.println("Total aguardando: " + filaAtendimento.size());

        System.out.println("\n── Iniciando atendimentos ──");

        while (!filaAtendimento.isEmpty()) {
            Cliente atendido = filaAtendimento.dequeue();
            System.out.printf("✓ Atendendo: %-30s | Restantes: %d%n",
                    atendido, filaAtendimento.size());
        }

        System.out.println("╚══ Todos atendidos! ══╝");
    }

    // ─────────────────────────────────────────
    //  4. Demonstração das operações básicas
    // ─────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════");
        System.out.println("   Queue (Fila) — Estrutura FIFO        ");
        System.out.println("═══════════════════════════════════════\n");

        Queue<Integer> fila = new Queue<>();

        // enqueue — O(1)
        System.out.println("▶ Enqueue: 10, 20, 30, 40");
        fila.enqueue(10);
        fila.enqueue(20);
        fila.enqueue(30);
        fila.enqueue(40);
        System.out.println(fila);

        // peek — O(1)
        System.out.println("\n▶ Peek (sem remover): " + fila.peek());

        // dequeue — O(1)
        System.out.println("\n▶ Dequeue (removendo da frente):");
        System.out.println("  Removido → " + fila.dequeue());
        System.out.println("  Removido → " + fila.dequeue());
        System.out.println(fila);

        // tamanho
        System.out.println("\n▶ Tamanho atual: " + fila.size());

        // caso de uso real
        simularAtendimento();

        // exceção em fila vazia
        System.out.println("\n▶ Testando exceção em fila vazia:");
        Queue<String> filaVazia = new Queue<>();
        try {
            filaVazia.dequeue();
        } catch (NoSuchElementException e) {
            System.out.println("  Exceção capturada: " + e.getMessage());
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  Queue implementada com sucesso! 🚀   ");
        System.out.println("═══════════════════════════════════════");
    }
}