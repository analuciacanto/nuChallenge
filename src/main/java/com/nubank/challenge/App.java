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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


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

            // Escreve no stdout (uma linha por simulação)
            System.out.println(jsonOutput);
        }

        reader.close(); 
    }
}
