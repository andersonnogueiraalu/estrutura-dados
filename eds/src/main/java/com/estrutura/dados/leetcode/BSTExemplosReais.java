package com.estrutura.dados.leetcode;

/**
 * ════════════════════════════════════════════════════════════
 *  BST — Exemplos Reais em Java
 *
 *  Exemplo 1: Ranking de Jogadores (Insert + InOrder)
 *  Exemplo 2: Autocompletar de Produtos (Search + Range)
 * ════════════════════════════════════════════════════════════
 */
public class BSTExemplosReais {

    // ── Node genérico por score (int) ─────────────────────
    static class Node {
        int    score;
        String name;
        Node   left, right;

        Node(int score, String name) {
            this.score = score;
            this.name  = name;
        }
    }

    // ── BST base reutilizada nos dois exemplos ─────────────
    static class BST {
        Node root;

        // insert — O(log n) balanceada | O(n) pior caso
        void insert(int score, String name) {
            root = insert(root, score, name);
        }

        private Node insert(Node node, int score, String name) {
            if (node == null) return new Node(score, name);
            if (score < node.score) node.left  = insert(node.left,  score, name);
            else if (score > node.score) node.right = insert(node.right, score, name);
            return node;
        }

        // search — O(log n) balanceada | O(n) pior caso
        Node search(int score) {
            return search(root, score);
        }

        private Node search(Node node, int score) {
            if (node == null || node.score == score) return node;
            return score < node.score
                    ? search(node.left,  score)
                    : search(node.right, score);
        }

        // inOrder — O(n): percorre em ordem crescente de score
        void inOrder(Node node, java.util.List<Node> result) {
            if (node == null) return;
            inOrder(node.left,  result);
            result.add(node);
            inOrder(node.right, result);
        }

        // range query — O(n): retorna nós entre [min, max]
        void range(Node node, int min, int max, java.util.List<Node> result) {
            if (node == null) return;
            if (node.score > min) range(node.left,  min, max, result);
            if (node.score >= min && node.score <= max) result.add(node);
            if (node.score < max) range(node.right, min, max, result);
        }
    }

    // ══════════════════════════════════════════════════════
    //  EXEMPLO 1 — Ranking de Jogadores
    //  Cenário: placar de um game online.
    //  Inserimos jogadores por score e o InOrder nos devolve
    //  o ranking completo em ordem crescente — O(n).
    //  Para o Top-3, percorremos o lado direito — O(log n).
    // ══════════════════════════════════════════════════════
    static void exemploRankingJogadores() {
        System.out.println("══════════════════════════════════════════");
        System.out.println(" EXEMPLO 1 — Ranking de Jogadores (BST)  ");
        System.out.println("══════════════════════════════════════════");

        BST ranking = new BST();

        // Inserindo jogadores com seus scores — O(log n) cada
        ranking.insert(1500, "Ana");
        ranking.insert(3200, "Bruno");
        ranking.insert(980,  "Carlos");
        ranking.insert(4750, "Diana");
        ranking.insert(2100, "Eduardo");
        ranking.insert(3900, "Fernanda");
        ranking.insert(670,  "Gabriel");

        // Ranking completo via InOrder — O(n)
        java.util.List<Node> todos = new java.util.ArrayList<>();
        ranking.inOrder(ranking.root, todos);

        System.out.println("\n📊 Ranking completo (score crescente):");
        System.out.println("  Pos | Jogador     | Score");
        System.out.println("  ────────────────────────────");
        for (int i = 0; i < todos.size(); i++) {
            Node n = todos.get(i);
            System.out.printf("  %3d | %-11s | %d%n",
                    todos.size() - i, n.name, n.score);
        }

        // Busca direta por score — O(log n)
        System.out.println("\n🔍 Busca por score 3200:");
        Node encontrado = ranking.search(3200);
        if (encontrado != null) {
            System.out.printf("  Jogador encontrado: %s (score: %d)%n",
                    encontrado.name, encontrado.score);
        }

        // Top 3 via range query — O(log n + k)
        java.util.List<Node> top3 = new java.util.ArrayList<>();
        ranking.range(ranking.root, 3500, 9999, top3);
        System.out.println("\n🏆 Top jogadores (score >= 3500):");
        top3.forEach(n ->
                System.out.printf("  %-11s → %d pts%n", n.name, n.score));
    }

    // ══════════════════════════════════════════════════════
    //  EXEMPLO 2 — Busca de Produtos por Faixa de Preço
    //  Cenário: e-commerce precisa filtrar produtos entre
    //  R$ 50 e R$ 200. Com BST ordenada por preço, a range
    //  query descarta subárvores inteiras — O(log n + k).
    //  Com um array seria necessário varrer tudo — O(n).
    // ══════════════════════════════════════════════════════
    static class ProdutoNode {
        double preco;
        String nome;
        ProdutoNode left, right;

        ProdutoNode(double preco, String nome) {
            this.preco = preco;
            this.nome  = nome;
        }
    }

    static class ProdutoBST {
        ProdutoNode root;

        void insert(double preco, String nome) {
            root = insert(root, preco, nome);
        }

        private ProdutoNode insert(ProdutoNode node, double preco, String nome) {
            if (node == null) return new ProdutoNode(preco, nome);
            if (preco < node.preco) node.left  = insert(node.left,  preco, nome);
            else                    node.right = insert(node.right, preco, nome);
            return node;
        }

        // range query por preço — O(log n + k)
        void rangePreco(ProdutoNode node, double min, double max,
                        java.util.List<ProdutoNode> result) {
            if (node == null) return;
            if (node.preco > min) rangePreco(node.left,  min, max, result);
            if (node.preco >= min && node.preco <= max) result.add(node);
            if (node.preco < max) rangePreco(node.right, min, max, result);
        }
    }

    static void exemploProdutosPorPreco() {
        System.out.println("\n══════════════════════════════════════════════");
        System.out.println(" EXEMPLO 2 — Busca de Produtos por Faixa de Preço");
        System.out.println("══════════════════════════════════════════════");

        ProdutoBST catalogo = new ProdutoBST();

        // Cadastrando produtos — O(log n) cada insert
        catalogo.insert(29.90,  "Caneta Pilot");
        catalogo.insert(149.99, "Teclado Mecânico");
        catalogo.insert(89.90,  "Mouse Gamer");
        catalogo.insert(499.00, "Monitor 24'");
        catalogo.insert(59.90,  "Mousepad XL");
        catalogo.insert(199.90, "Headset USB");
        catalogo.insert(320.00, "Webcam Full HD");
        catalogo.insert(39.90,  "Hub USB");
        catalogo.insert(179.90, "SSD 240GB");
        catalogo.insert(750.00, "GPU RTX 3060");

        double min = 50.0, max = 200.0;

        // Range query — descarta subárvores fora do intervalo
        java.util.List<ProdutoNode> resultado = new java.util.ArrayList<>();
        catalogo.rangePreco(catalogo.root, min, max, resultado);

        System.out.printf("%n🛒 Produtos entre R$ %.2f e R$ %.2f:%n", min, max);
        System.out.println("  Produto              | Preço");
        System.out.println("  ─────────────────────────────────");
        resultado.forEach(p ->
                System.out.printf("  %-20s | R$ %.2f%n", p.nome, p.preco));

        System.out.printf("%n  Total encontrado: %d produto(s)%n", resultado.size());
        System.out.println("\n  Complexidade da busca: O(log n + k)");
        System.out.println("  onde k = número de resultados encontrados");
        System.out.println("  Com array seria necessário varrer tudo → O(n)");
    }

    // ── Main ──────────────────────────────────────────────
    public static void main(String[] args) {
        exemploRankingJogadores();
        exemploProdutosPorPreco();

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  BST Exemplos Reais — concluído! 🚀      ");
        System.out.println("══════════════════════════════════════════");
    }
}