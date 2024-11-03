package org.example;

import database.dbConnection;
import database.impl.ContatoImpl;
import domain.model.Contato;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = dbConnection.getConnection();
            ContatoImpl contatoImpl = new ContatoImpl(connection);
            Scanner scanner = new Scanner(System.in);
            int option;

            do {
                System.out.println("\n--- Menu de Contatos ---");
                System.out.println("1 - Adicionar Contato");
                System.out.println("2 - Remover Contato");
                System.out.println("3 - Buscar Contato por ID");
                System.out.println("4 - Editar Contato");
                System.out.println("5 - Listar Todos os Contatos");
                System.out.println("6 - Abrir Interface Gráfica (Swing)");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                switch (option) {
                    case 1 -> {
                        Contato contato = new Contato();
                        System.out.print("Nome: ");
                        contato.setNome(scanner.nextLine());
                        System.out.print("Email: ");
                        contato.setEmail(scanner.nextLine());
                        System.out.print("Telefone: ");
                        contato.setTelefone(scanner.nextLine());
                        contatoImpl.insert(contato);
                        System.out.println("Contato adicionado com sucesso!");
                    }
                    case 2 -> {
                        System.out.print("ID do Contato a ser removido: ");
                        int id = scanner.nextInt();
                        Contato contato = contatoImpl.findById(id);
                        if (contato != null) {
                            contatoImpl.delete(contato);
                            System.out.println("Contato removido com sucesso!");
                        } else {
                            System.out.println("Contato não encontrado.");
                        }
                    }
                    case 3 -> {
                        System.out.print("ID do Contato: ");
                        int id = scanner.nextInt();
                        Contato contato = contatoImpl.findById(id);
                        if (contato != null) {
                            System.out.println("ID: " + contato.getId());
                            System.out.println("Nome: " + contato.getNome());
                            System.out.println("Email: " + contato.getEmail());
                            System.out.println("Telefone: " + contato.getTelefone());
                        } else {
                            System.out.println("Contato não encontrado.");
                        }
                    }
                    case 4 -> {
                        System.out.print("ID do Contato a ser editado: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consumir a nova linha
                        Contato contato = contatoImpl.findById(id);
                        if (contato != null) {
                            System.out.print("Novo Nome: ");
                            contato.setNome(scanner.nextLine());
                            System.out.print("Novo Email: ");
                            contato.setEmail(scanner.nextLine());
                            System.out.print("Novo Telefone: ");
                            contato.setTelefone(scanner.nextLine());
                            contatoImpl.update(contato);
                            System.out.println("Contato atualizado com sucesso!");
                        } else {
                            System.out.println("Contato não encontrado.");
                        }
                    }
                    case 5 -> {
                        List<Contato> contatos = contatoImpl.findAll();
                        if (contatos.isEmpty()) {
                            System.out.println("Nenhum contato encontrado.");
                        } else {
                            for (Contato contato : contatos) {
                                System.out.println("ID: " + contato.getId() + ", Nome: " + contato.getNome() +
                                        ", Email: " + contato.getEmail() + ", Telefone: " + contato.getTelefone());
                            }
                        }
                    }
                    case 6 -> {
                        // Abrir a interface gráfica
                        SwingUtilities.invokeLater(() -> new ContatoSwingInterface(connection, contatoImpl));
                    }
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } while (option != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
