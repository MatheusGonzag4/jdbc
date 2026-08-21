package br.edu.fatec.curso.view;

import br.edu.fatec.curso.model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CursoView {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection conn = Curso.getConnection()) {
            int opcao;

            do {
                exibirMenuPrincipal();
                opcao = lerInteiro(scanner, "Escolha uma opção: ");

                switch (opcao) {
                    case 1:
                        menuCursos(conn, scanner);
                        break;
                    case 2:
                        menuTarefas(conn, scanner);
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } while (opcao != 0);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n===== MENU PRINCIPAL =====");
        System.out.println("1 - Gerenciar cursos");
        System.out.println("2 - Gerenciar tarefas");
        System.out.println("0 - Sair");
    }

    private static void menuCursos(Connection conn, Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n===== MENU CURSOS =====");
            System.out.println("1 - Inserir curso");
            System.out.println("2 - Listar cursos");
            System.out.println("3 - Atualizar curso");
            System.out.println("4 - Deletar curso");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(scanner, "Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        inserirCurso(conn, scanner);
                        break;
                    case 2:
                        listarCursos(conn);
                        break;
                    case 3:
                        atualizarCurso(conn, scanner);
                        break;
                    case 4:
                        deletarCurso(conn, scanner);
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void inserirCurso(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nome do curso: ");
        String nome = scanner.nextLine();
        System.out.print("Período: ");
        String periodo = scanner.nextLine();

        String sql = "INSERT INTO curso (nome, periodo) VALUES (?, ?)";
        try (PreparedStatement inserir = conn.prepareStatement(sql)) {
            inserir.setString(1, nome);
            inserir.setString(2, periodo);
            inserir.executeUpdate();
            System.out.println("Curso inserido com sucesso.");
        }
    }

    private static void listarCursos(Connection conn) throws SQLException {
        String sql = "SELECT id, nome, periodo FROM curso ORDER BY id";
        try (PreparedStatement listar = conn.prepareStatement(sql);
             ResultSet rs = listar.executeQuery()) {
            System.out.println("\n===== CURSOS CADASTRADOS =====");
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println(
                        "ID: " + rs.getInt("id")
                                + " | Nome: " + rs.getString("nome")
                                + " | Período: " + rs.getString("periodo"));
            }

            if (!encontrou) {
                System.out.println("Nenhum curso cadastrado.");
            }
        }
    }

    private static void atualizarCurso(Connection conn, Scanner scanner) throws SQLException {
        int id = lerInteiro(scanner, "ID do curso a atualizar: ");
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo período: ");
        String periodo = scanner.nextLine();

        String sql = "UPDATE curso SET nome = ?, periodo = ? WHERE id = ?";
        try (PreparedStatement atualizar = conn.prepareStatement(sql)) {
            atualizar.setString(1, nome);
            atualizar.setString(2, periodo);
            atualizar.setInt(3, id);

            int linhasAlteradas = atualizar.executeUpdate();
            if (linhasAlteradas > 0) {
                System.out.println("Curso atualizado com sucesso.");
            } else {
                System.out.println("Nenhum curso encontrado com esse ID.");
            }
        }
    }

    private static void deletarCurso(Connection conn, Scanner scanner) throws SQLException {
        int id = lerInteiro(scanner, "ID do curso a deletar: ");

        String sql = "DELETE FROM curso WHERE id = ?";
        try (PreparedStatement deletar = conn.prepareStatement(sql)) {
            deletar.setInt(1, id);

            int linhasRemovidas = deletar.executeUpdate();
            if (linhasRemovidas > 0) {
                System.out.println("Curso deletado com sucesso.");
            } else {
                System.out.println("Nenhum curso encontrado com esse ID.");
            }
        }
    }

    private static void menuTarefas(Connection conn, Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n===== MENU TAREFAS =====");
            System.out.println("1 - Inserir tarefa");
            System.out.println("2 - Listar tarefas com filtros");
            System.out.println("3 - Atualizar tarefa");
            System.out.println("4 - Marcar ou desmarcar como concluída");
            System.out.println("5 - Deletar tarefa");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(scanner, "Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        inserirTarefa(conn, scanner);
                        break;
                    case 2:
                        listarTarefas(conn, scanner);
                        break;
                    case 3:
                        atualizarTarefa(conn, scanner);
                        break;
                    case 4:
                        alterarStatusTarefa(conn, scanner);
                        break;
                    case 5:
                        deletarTarefa(conn, scanner);
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void inserirTarefa(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Descrição da tarefa: ");
        String descricao = scanner.nextLine();
        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        String sql = "INSERT INTO tarefa (descricao, categoria) VALUES (?, ?)";
        try (PreparedStatement inserir = conn.prepareStatement(sql)) {
            inserir.setString(1, descricao);
            inserir.setString(2, categoria);
            inserir.executeUpdate();
            System.out.println("Tarefa inserida com sucesso.");
        }
    }

    private static void listarTarefas(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Categoria (pressione Enter para todas): ");
        String categoria = scanner.nextLine().trim();
        Boolean concluida = lerFiltroStatus(scanner);

        StringBuilder sql = new StringBuilder(
                "SELECT id, descricao, categoria, concluida, criada_em FROM tarefa");
        if (!categoria.isEmpty() || concluida != null) {
            sql.append(" WHERE ");
        }
        if (!categoria.isEmpty()) {
            sql.append("LOWER(categoria) = LOWER(?)");
            if (concluida != null) {
                sql.append(" AND ");
            }
        }
        if (concluida != null) {
            sql.append("concluida = ?");
        }
        sql.append(" ORDER BY id");

        try (PreparedStatement listar = conn.prepareStatement(sql.toString())) {
            int parametro = 1;
            if (!categoria.isEmpty()) {
                listar.setString(parametro++, categoria);
            }
            if (concluida != null) {
                listar.setBoolean(parametro, concluida);
            }

            try (ResultSet rs = listar.executeQuery()) {
                System.out.println("\n===== TAREFAS =====");
                boolean encontrou = false;

                while (rs.next()) {
                    encontrou = true;
                    String status = rs.getBoolean("concluida") ? "Concluída" : "Pendente";
                    System.out.println(
                            "ID: " + rs.getInt("id")
                                    + " | Descrição: " + rs.getString("descricao")
                                    + " | Categoria: " + rs.getString("categoria")
                                    + " | Status: " + status
                                    + " | Criada em: " + rs.getTimestamp("criada_em"));
                }

                if (!encontrou) {
                    System.out.println("Nenhuma tarefa encontrada com esses filtros.");
                }
            }
        }
    }

    private static Boolean lerFiltroStatus(Scanner scanner) {
        while (true) {
            System.out.println("Status: 0 - Todos | 1 - Pendentes | 2 - Concluídas");
            int opcao = lerInteiro(scanner, "Escolha o status: ");

            switch (opcao) {
                case 0:
                    return null;
                case 1:
                    return false;
                case 2:
                    return true;
                default:
                    System.out.println("Opção de status inválida.");
            }
        }
    }

    private static void atualizarTarefa(Connection conn, Scanner scanner) throws SQLException {
        int id = lerInteiro(scanner, "ID da tarefa a atualizar: ");
        System.out.print("Nova descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Nova categoria: ");
        String categoria = scanner.nextLine();

        String sql = "UPDATE tarefa SET descricao = ?, categoria = ? WHERE id = ?";
        try (PreparedStatement atualizar = conn.prepareStatement(sql)) {
            atualizar.setString(1, descricao);
            atualizar.setString(2, categoria);
            atualizar.setInt(3, id);

            int linhasAlteradas = atualizar.executeUpdate();
            if (linhasAlteradas > 0) {
                System.out.println("Tarefa atualizada com sucesso.");
            } else {
                System.out.println("Nenhuma tarefa encontrada com esse ID.");
            }
        }
    }

    private static void alterarStatusTarefa(Connection conn, Scanner scanner) throws SQLException {
        int id = lerInteiro(scanner, "ID da tarefa: ");
        boolean concluida = lerSimNao(scanner, "Deseja marcar como concluída? (s/n): ");

        String sql = "UPDATE tarefa SET concluida = ? WHERE id = ?";
        try (PreparedStatement atualizar = conn.prepareStatement(sql)) {
            atualizar.setBoolean(1, concluida);
            atualizar.setInt(2, id);

            int linhasAlteradas = atualizar.executeUpdate();
            if (linhasAlteradas > 0) {
                System.out.println(concluida
                        ? "Tarefa marcada como concluída."
                        : "Tarefa marcada como pendente.");
            } else {
                System.out.println("Nenhuma tarefa encontrada com esse ID.");
            }
        }
    }

    private static void deletarTarefa(Connection conn, Scanner scanner) throws SQLException {
        int id = lerInteiro(scanner, "ID da tarefa a deletar: ");

        String sql = "DELETE FROM tarefa WHERE id = ?";
        try (PreparedStatement deletar = conn.prepareStatement(sql)) {
            deletar.setInt(1, id);

            int linhasRemovidas = deletar.executeUpdate();
            if (linhasRemovidas > 0) {
                System.out.println("Tarefa deletada com sucesso.");
            } else {
                System.out.println("Nenhuma tarefa encontrada com esse ID.");
            }
        }
    }

    private static boolean lerSimNao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            }
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não")) {
                return false;
            }
            System.out.println("Digite S para sim ou N para não.");
        }
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }
}
