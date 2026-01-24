package iut.tp.strategy;

public class ReducedTaxStrategy implements TaxStrategy{
    private static final double TAX_RATE = 0.055;

    @Override
    public double getTaxRate() {
        return TAX_RATE;
    }

    @Override
    public String getName() {
        return "TVA Réduite 5.5%";
    }
}
