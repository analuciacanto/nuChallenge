package com.nubank.challenge.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.nubank.challenge.model.Operation;
import com.nubank.challenge.model.TaxResult;

public class TaxCalculator {

    public List<TaxResult> calculateTax(List<Operation> operations) {

        BigDecimal accumulatedLoss = BigDecimal.ZERO; // Prejuízo acumulado
        BigDecimal averagePrice = BigDecimal.ZERO;    // Média ponderada
        int totalQuantity = 0;                        // Quantidade total de ações
        List<TaxResult> taxResults = new ArrayList<>();

        for (Operation operation : operations) {

            BigDecimal quantityBD = BigDecimal.valueOf(operation.getQuantity());
            BigDecimal totalOperationValue = operation.getUnitCost().multiply(quantityBD);

            if ("buy".equalsIgnoreCase(operation.getOperation())) {
                // Atualiza média ponderada apenas nas compras
                averagePrice = weightedAveragePriceForBuy(averagePrice, totalQuantity, operation.getUnitCost(), operation.getQuantity());
                totalQuantity += operation.getQuantity();
                taxResults.add(new TaxResult(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP)));

            } else { // venda

                BigDecimal unitProfit = operation.getUnitCost().subtract(averagePrice);
                BigDecimal totalProfit = unitProfit.multiply(quantityBD);

                if (totalOperationValue.compareTo(BigDecimal.valueOf(20000)) <= 0) {
                    // Venda ≤ 20.000: acumula prejuízo se houver
                    if (totalProfit.compareTo(BigDecimal.ZERO) < 0) {
                        accumulatedLoss = accumulatedLoss.add(totalProfit.abs());
                    }
                    taxResults.add(new TaxResult(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP)));

                } else {
                    // Venda > 20.000: deduz prejuízo acumulado corretamente
                    BigDecimal profitToTax = BigDecimal.ZERO;

                    if (totalProfit.compareTo(BigDecimal.ZERO) <= 0) {
                        // Lucro negativo: acumula prejuízo
                        accumulatedLoss = accumulatedLoss.add(totalProfit.abs());
                    } else {
                        // Lucro positivo: deduz prejuízo acumulado
                        if (accumulatedLoss.compareTo(totalProfit) >= 0) {
                            accumulatedLoss = accumulatedLoss.subtract(totalProfit);
                            profitToTax = BigDecimal.ZERO;
                        } else {
                            profitToTax = totalProfit.subtract(accumulatedLoss);
                            accumulatedLoss = BigDecimal.ZERO;
                        }
                    }

                    taxResults.add(new TaxResult(getTax(profitToTax, averagePrice).setScale(1, RoundingMode.HALF_UP)));
                }

                totalQuantity -= operation.getQuantity(); // atualiza saldo
            }
        }

        return taxResults;
    }

    /**
     * Atualiza a média ponderada incremental para compras
     * nova-media-ponderada = ((quantidade-atual * media-atual) + (quantidade-compra * valor-compra)) / (quantidade-atual + quantidade-compra)
     */
    private BigDecimal weightedAveragePriceForBuy(BigDecimal currentAverage, int currentQuantity, BigDecimal buyPrice, int buyQuantity) {
        if (currentQuantity + buyQuantity == 0) return BigDecimal.ZERO;
        return currentAverage.multiply(BigDecimal.valueOf(currentQuantity))
                .add(buyPrice.multiply(BigDecimal.valueOf(buyQuantity)))
                .divide(BigDecimal.valueOf(currentQuantity + buyQuantity), 10, RoundingMode.HALF_UP);
    }

    private BigDecimal getTax(BigDecimal profit, BigDecimal averagePrice) {
        if (averagePrice == null || profit.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;
        return profit.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP);
    }
}
    