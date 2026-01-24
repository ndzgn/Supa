package iut.tp.strategy;

public class NoTaxStrategy implements TaxStrategy{
    @Override
    public double getTaxRate() {
        return 0.0;
    }

    @Override
    public String getName() {
        return "Sans Taxe";
    }
}
