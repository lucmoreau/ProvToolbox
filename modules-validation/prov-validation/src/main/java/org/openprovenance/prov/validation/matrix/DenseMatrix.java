package org.openprovenance.prov.validation.matrix;
import java.util.List;




public class DenseMatrix extends no.uib.cipr.matrix.DenseMatrix implements Matrix
{
    final int size1;
    final int size2;

    public DenseMatrix(int size1, int size2) {
	super(size1, size2);
	this.size1=size1;
	this.size2=size2;
    }

    public Double g(int row, int col) {
	return new Double(super.get(row,col));
    }


    /**
     * Sets the value of the matrix at the specified row and column.
     * @param row Object
     * @param col Object
     * @param value a value
     */
    public void set(int row, int col, Double value) {
	super.set(row,col,(Double)value);
    }

    public List<Pair> getRow(int row) {
	throw new UnsupportedOperationException();
    }
    final public static double epsilon = 0.001;

    //final double epsilon=org.openprovenance.prov.scala.validation.EventMatrix.epsilon;

    public void floydWarshall (Matrix next) {
	for (int k=0; k<size1; k++)  {
            for (int i=0; i<size1; i++)  {
                for (int j=0; j< size2; j++)  {
		    Double m_i_k;
		    Double m_k_j;
		    Double m_i_j;
                    if ((m_i_k=g(i,k))!=null
			&& (m_k_j=g(k,j))!=null
			&& m_i_k > epsilon
			&& m_k_j > epsilon
			&& ((m_i_j=g(i,j))==null
			    || m_i_j <= epsilon
			    || m_i_k+m_k_j < m_i_j ))
			{
			    set(i,j,m_i_k+m_k_j);
			    next.set(i,j,(double) k);
			}
                }
            }
	}
    }


    public double getMaximum() {
	return maximum;
    }

    private double maximum=0;

}
