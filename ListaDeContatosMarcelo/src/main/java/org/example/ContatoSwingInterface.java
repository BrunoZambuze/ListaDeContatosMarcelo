package org.example;

import database.impl.ContatoImpl;
import domain.model.Contato;
import exception.queryFailedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class ContatoSwingInterface extends JFrame {
    private ContatoImpl contatoImpl;
    private JTextArea displayArea;

    public ContatoSwingInterface(Connection connection, ContatoImpl contatoImpl) {
        this.contatoImpl = contatoImpl;
        initUI();
    }

    private void initUI() {
        setTitle("Gerenciador de Contatos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));

        JButton listButton = new JButton("Listar Contatos");
        JButton addButton = new JButton("Adicionar Contato");
        JButton deleteButton = new JButton("Remover Contato");
        JButton searchButton = new JButton("Buscar Contato");

        buttonPanel.add(listButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        add(buttonPanel, BorderLayout.SOUTH);

        listButton.addActionListener(e -> listContacts());
        addButton.addActionListener(e -> {
            try {
                addContact();
            } catch (queryFailedException ex) {
                throw new RuntimeException(ex);
            }
        });
        deleteButton.addActionListener(e -> {
            try {
                deleteContact();
            } catch (queryFailedException ex) {
                throw new RuntimeException(ex);
            }
        });
        searchButton.addActionListener(e -> {
            try {
                searchContact();
            } catch (queryFailedException ex) {
                throw new RuntimeException(ex);
            }
        });

        setVisible(true);
    }

    private void listContacts() {
        List<Contato> contatos = contatoImpl.findAll();
        displayArea.setText("");
        for (Contato contato : contatos) {
            displayArea.append("ID: " + contato.getId() + ", Nome: " + contato.getNome() +
                    ", Email: " + contato.getEmail() + ", Telefone: " + contato.getTelefone() + "\n");
        }
    }

    private void addContact() throws queryFailedException {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome:");
        String email = JOptionPane.showInputDialog(this, "Digite o email:");
        String telefone = JOptionPane.showInputDialog(this, "Digite o telefone:");
        if (nome != null && email != null && telefone != null) {
            Contato contato = new Contato();
            contato.setNome(nome);
            contato.setEmail(email);
            contato.setTelefone(telefone);
            contatoImpl.insert(contato);
            JOptionPane.showMessageDialog(this, "Contato adicionado com sucesso!");
            listContacts();
        }
    }

    private void deleteContact() throws queryFailedException {
        String input = JOptionPane.showInputDialog(this, "Digite o ID do contato a ser removido:");
        if (input != null) {
            int id = Integer.parseInt(input);
            Contato contato = contatoImpl.findById(id);
            if (contato != null) {
                contatoImpl.delete(contato);
                JOptionPane.showMessageDialog(this, "Contato removido com sucesso!");
                listContacts();
            } else {
                JOptionPane.showMessageDialog(this, "Contato não encontrado.");
            }
        }
    }

    private void searchContact() throws queryFailedException {
        String input = JOptionPane.showInputDialog(this, "Digite o ID do contato:");
        if (input != null) {
            int id = Integer.parseInt(input);
            Contato contato = contatoImpl.findById(id);
            if (contato != null) {
                displayArea.setText("ID: " + contato.getId() + ", Nome: " + contato.getNome() +
                        ", Email: " + contato.getEmail() + ", Telefone: " + contato.getTelefone());
            } else {
                JOptionPane.showMessageDialog(this, "Contato não encontrado.");
            }
        }
    }
}
