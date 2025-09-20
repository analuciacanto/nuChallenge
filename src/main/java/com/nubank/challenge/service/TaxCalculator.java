package com.nubank.challenge.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.nubank.challenge.model.Operation;
import com.nubank.challenge.model.TaxResult;

public class TaxCalculator {

    public List<TaxResult> calculateTax(List <Operation> operations) {

        List <TaxResult> taxResults = new ArrayList<>();

        for(Operation operation : operations) {
            BigDecimal averagePrice = weightedAveragePrice(operations, 0, 0, null);
            if(operation.getOperation().equals("sell") && averagePrice.compareTo(operation.getUnitCost()) < 0) 
            {             
              taxResults.add(new TaxResult(getTax(operation, averagePrice).setScale(1, RoundingMode.HALF_UP)));                    
            }
            else
            {
              taxResults.add(new TaxResult(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP))); 
            }
        }
        return taxResults;               
    }

    /**
     nova-media-ponderada = ((quantidade-de-acoes-atual * media-ponderada-
        atual) + (quantidade-de-acoes-compradas * valor-de-compra)) / (quantidade-de-acoes-atual + 
                quantidade-de-acoes-compradas)
     */
    
    private BigDecimal weightedAveragePrice(List<Operation> operations, int position, int currentQuantity, BigDecimal currentWeightedAverage){
                
                if (currentWeightedAverage == null) {
                    currentWeightedAverage = BigDecimal.ZERO;
                }
                if (position >= operations.size()) {                  
                    return currentWeightedAverage;
                }         
                    Operation operation = operations.get(position);
                    position++;
        
                    if (operation.getOperation().equals("buy")) {

                        currentWeightedAverage = currentWeightedAverage.multiply(new BigDecimal(currentQuantity))
                        .add(operation.getUnitCost().multiply(new BigDecimal(operation.getQuantity())))
                        .divide(new BigDecimal(currentQuantity + operation.getQuantity()), RoundingMode.HALF_UP);   
                        currentQuantity += operation.getQuantity();
                  
                    }
                    else if(operation.getOperation().equals("sell")) {
                        currentQuantity -= operation.getQuantity();
                    }
                    return weightedAveragePrice(operations, position, currentQuantity, currentWeightedAverage);                     
            }

        
    
private BigDecimal getTax(Operation operation, BigDecimal averagePrice) {
    /** O percentual de imposto pago é de 20% sobre o lucro obtido na operação. Ou seja, o imposto vai ser
        pago quando há uma operação de venda cujo preço é superior ao preço médio ponderado de compra.
    */

    if (averagePrice == null) {
        return BigDecimal.ZERO;
    }

    BigDecimal profit = operation.getUnitCost()
                        .subtract(averagePrice)
                        .multiply(new BigDecimal(operation.getQuantity()));

    BigDecimal tax = profit.multiply(new BigDecimal("0.20"))
                           .setScale(2, RoundingMode.HALF_UP);

    return tax;
    }

}