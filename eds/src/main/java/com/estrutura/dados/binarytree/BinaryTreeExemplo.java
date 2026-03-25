package com.estrutura.dados.binarytree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 *  Binary Tree (Árvore Binária) — Implementação Didática em Java
 *  Estrutura de Dados | BST: Binary Search Tree
 *
 *  Complexidade das Operações (BST balanceada):
 *  ┌─────────────┬────────────┬────────────┐
 *  │  Operação   │  Melhor    │   Pior     │
 *  ├─────────────┼────────────┼────────────┤
 *  │  insert()   │  O(log n)  │   O(n)     │
 *  │  search()   │  O(log n)  │   O(n)     │
 *  │  delete()   │  O(log n)  │   O(n)     │
 *  │  height()   │  O(n)      │   O(n)     │
 *  │  inOrder()  │  O(n)      │   O(n)     │
 *  │  BFS()      │  O(n)      │   O(n)     │
 *  └─────────────┴────────────┴────────────┘
 *  Espaço: O(n)
 *
 *  ⚠️ O pior caso O(n) ocorre quando a árvore está
 *  completamente desbalanceada (inserção em ordem crescente),
 *  degenerando em uma lista encadeada.
 *
 *  Série: Estruturas de Dados em Java — Ep.07
 *  Autor: @seulinkedin
 * ╚══════════════════════════════════════════════════════════════════╝
 */
public class BinaryTreeExemplo {

    // ─────────────────────────────────────────
    //  1. Node: nó da árvore binária
    // ─────────────────────────────────────────
    static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.left  = null;
            this.right = null;
        }
    }

    // ─────────────────────────────────────────
    //  2. Binary Search Tree (BST)
    //     Regra: esquerda < raiz < direita
    // ─────────────────────────────────────────
    static class BST {

        private Node root;

        public BST() {
            this.root = null;
        }

        // ── INSERT ─────────────────────────────────────
        /**
         * Insere um valor na BST.
         * Complexidade: O(log n) balanceada | O(n) desbalanceada
         */
        public void insert(int value) {
            root = insertRecursive(root, value);
        }

        private Node insertRecursive(Node node, int value) {
            if (node == null) return new Node(value);

            if (value < node.value)
                node.left  = insertRecursive(node.left,  value);
            else if (value > node.value)
                node.right = insertRecursive(node.right, value);
            // valor duplicado: ignoramos

            return node;
        }

        // ── SEARCH ─────────────────────────────────────
        /**
         * Busca um valor na BST.
         * Complexidade: O(log n) balanceada | O(n) desbalanceada
         */
        public boolean search(int value) {
            return searchRecursive(root, value);
        }

        private boolean searchRecursive(Node node, int value) {
            if (node == null)          return false;
            if (value == node.value)   return true;
            if (value < node.value)    return searchRecursive(node.left,  value);
            return                            searchRecursive(node.right, value);
        }

        // ── DELETE ─────────────────────────────────────
        /**
         * Remove um valor da BST.
         * Complexidade: O(log n) balanceada | O(n) desbalanceada
         *
         * 3 casos:
         *  1. Nó folha          → remove diretamente
         *  2. Nó com 1 filho    → substitui pelo filho
         *  3. Nó com 2 filhos   → substitui pelo sucessor in-order (menor do lado direito)
         */
        public void delete(int value) {
            root = deleteRecursive(root, value);
        }

        private Node deleteRecursive(Node node, int value) {
            if (node == null) return null;

            if (value < node.value) {
                node.left  = deleteRecursive(node.left,  value);
            } else if (value > node.value) {
                node.right = deleteRecursive(node.right, value);
            } else {
                // Caso 1 e 2: 0 ou 1 filho
                if (node.left  == null) return node.right;
                if (node.right == null) return node.left;

                // Caso 3: 2 filhos — encontra o menor da subárvore direita
                node.value = findMin(node.right);
                node.right = deleteRecursive(node.right, node.value);
            }
            return node;
        }

        private int findMin(Node node) {
            while (node.left != null) node = node.left;
            return node.value;
        }

        // ── TRAVERSALS ─────────────────────────────────
        /**
         * In-Order: esquerda → raiz → direita
         * Resultado: valores em ordem crescente na BST
         * Complexidade: O(n)
         */
        public List<Integer> inOrder() {
            List<Integer> result = new ArrayList<>();
            inOrderRecursive(root, result);
            return result;
        }

        private void inOrderRecursive(Node node, List<Integer> result) {
            if (node == null) return;
            inOrderRecursive(node.left,  result);
            result.add(node.value);
            inOrderRecursive(node.right, result);
        }

        /**
         * Pre-Order: raiz → esquerda → direita
         * Útil para: copiar/serializar a árvore
         * Complexidade: O(n)
         */
        public List<Integer> preOrder() {
            List<Integer> result = new ArrayList<>();
            preOrderRecursive(root, result);
            return result;
        }

        private void preOrderRecursive(Node node, List<Integer> result) {
            if (node == null) return;
            result.add(node.value);
            preOrderRecursive(node.left,  result);
            preOrderRecursive(node.right, result);
        }

        /**
         * Post-Order: esquerda → direita → raiz
         * Útil para: deletar a árvore, calcular expressões
         * Complexidade: O(n)
         */
        public List<Integer> postOrder() {
            List<Integer> result = new ArrayList<>();
            postOrderRecursive(root, result);
            return result;
        }

        private void postOrderRecursive(Node node, List<Integer> result) {
            if (node == null) return;
            postOrderRecursive(node.left,  result);
            postOrderRecursive(node.right, result);
            result.add(node.value);
        }

        /**
         * BFS — Level-Order: percorre nível por nível
         * Usa internamente uma Queue
         * Complexidade: O(n)
         */
        public List<List<Integer>> bfs() {
            List<List<Integer>> levels = new ArrayList<>();
            if (root == null) return levels;

            Queue<Node> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                List<Integer> level = new ArrayList<>();

                for (int i = 0; i < levelSize; i++) {
                    Node node = queue.poll();
                    level.add(node.value);
                    if (node.left  != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                levels.add(level);
            }
            return levels;
        }

        // ── ALTURA ─────────────────────────────────────
        /**
         * Retorna a altura da árvore (número de níveis - 1).
         * Complexidade: O(n) — precisa visitar todos os nós
         */
        public int height() {
            return heightRecursive(root);
        }

        private int heightRecursive(Node node) {
            if (node == null) return -1;
            return 1 + Math.max(
                    heightRecursive(node.left),
                    heightRecursive(node.right)
            );
        }

        // ── VISUALIZAÇÃO ───────────────────────────────
        /**
         * Imprime a árvore de forma visual no console.
         */
        public void printTree() {
            printTreeRecursive(root, "", true);
        }

        private void printTreeRecursive(Node node, String prefix, boolean isLeft) {
            if (node == null) return;
            System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.value);
            printTreeRecursive(node.left,  prefix + (isLeft ? "│   " : "    "), true);
            printTreeRecursive(node.right, prefix + (isLeft ? "│   " : "    "), false);
        }
    }

    // ─────────────────────────────────────────
    //  3. Caso de Uso Real:
    //     Sistema de autocompletar (prefix search)
    // ─────────────────────────────────────────
    static void demonstrarAutocompletar() {
        System.out.println("\n╔══ CASO DE USO: Range Query com BST ══╗");
        System.out.println("  Buscar todos os valores entre 20 e 60");

        BST bst = new BST();
        int[] dados = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
        for (int d : dados) bst.insert(d);

        List<Integer> inOrder = bst.inOrder();
        List<Integer> range   = new ArrayList<>();

        // BST in-order já vem ordenado → range query eficiente
        for (int v : inOrder) {
            if (v >= 20 && v <= 60) range.add(v);
        }

        System.out.println("  Todos os valores (in-order): " + inOrder);
        System.out.println("  Valores entre 20 e 60:       " + range);
        System.out.println("╚═══════════════════════════════════════╝");
    }

    // ─────────────────────────────────────────
    //  4. Main — demonstração completa
    // ─────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("════════════════════════════════════════════");
        System.out.println("   Binary Search Tree — Ep.07               ");
        System.out.println("   Regra: esquerda < raiz < direita          ");
        System.out.println("════════════════════════════════════════════\n");

        BST bst = new BST();
        int[] valores = {50, 30, 70, 20, 40, 60, 80};

        System.out.println("▶ Inserindo: 50, 30, 70, 20, 40, 60, 80\n");
        for (int v : valores) bst.insert(v);

        System.out.println("▶ Estrutura da árvore:");
        bst.printTree();

        System.out.println("\n▶ Travessias:");
        System.out.println("  In-Order   (asc):  " + bst.inOrder());
        System.out.println("  Pre-Order  (raiz): " + bst.preOrder());
        System.out.println("  Post-Order (folha):" + bst.postOrder());
        System.out.println("  BFS (níveis):      " + bst.bfs());

        System.out.println("\n▶ Altura da árvore: " + bst.height());

        System.out.println("\n▶ Search:");
        System.out.println("  search(40) → " + bst.search(40));
        System.out.println("  search(99) → " + bst.search(99));

        System.out.println("\n▶ Delete do nó 30 (2 filhos):");
        bst.delete(30);
        bst.printTree();
        System.out.println("  In-Order após delete: " + bst.inOrder());

        demonstrarAutocompletar();

        System.out.println("\n════════════════════════════════════════════");
        System.out.println("  Binary Tree implementada com sucesso! 🚀  ");
        System.out.println("════════════════════════════════════════════");
    }
}