package iut.tp.strategy;

public class StandardTaxStrategy implements TaxStrategy{
    private static final double TAX_RATE = 0.1925;

    @Override
    public double getTaxRate() {
        return TAX_RATE;
    }

    @Override
    public String getName() {
        return "TVA Standard 19.25%";
    }
}
