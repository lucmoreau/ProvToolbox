package org.openprovenance.prov.validation.matrix;
import java.util.List;

public interface Matrix
{

    public Double g(int row, int col);


    /**
     * Sets the value of the matrix at the specified row and column.
     * @param row Object
     * @param col Object
     * @param value a value
     */
    public void set(int row, int col, Double value);  

    public List<Pair> getRow(int row);
    public void floydWarshall (Matrix next);

    public double getMaximum();




}
