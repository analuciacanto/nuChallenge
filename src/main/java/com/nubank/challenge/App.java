package com.nubank.challenge;

import java.io.*;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.challenge.model.Operation;
import com.nubank.challenge.model.TaxResult;
import com.nubank.challenge.service.TaxCalculator;


public class App {

    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        // Entrada: arquivo txt
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        // Saída: arquivo txt
        BufferedWriter writer = new BufferedWriter(new FileWriter("saida.txt"));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Converte a linha JSON em lista de Operation
            List<Operation> operations = mapper.readValue(line, new TypeReference<List<Operation>>() {});
            TaxCalculator calculator = new TaxCalculator();
            List<TaxResult> taxResults = calculator.calculateTax(operations);
            
            // Converte a lista de TaxResult para JSON
            String jsonOutput = mapper.writeValueAsString(taxResults);

            // Escreve no arquivo
            writer.write(jsonOutput);
            writer.write("\n"); // se quiser separar cada linha de simulação
        }

        reader.close();
        writer.close();

        System.out.println("Processamento finalizado! Verifique o arquivo saida.txt");
    }
}
