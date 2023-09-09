package org.openprovenance.prov.validation.matrix;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


/**
 * Generic Class for sparsely populated matrices. The size of the matrix is dynamic, i.e.
 * you can specify arbitrary row and column parameters.
 *
 * Another way to look at this class is as a "two-dimensional" map. Instead of a single key
 * it uses a row and a column element to store and retrieve data elements.
 *
 * The public Interface Traverser allows to traverse the matrix generically. The Traverser object is passed
 * into <code>traverse(Traverser)</code> and is given the opportunity to work with each element of the matrix.
 */
public final class SparseMatrix //implements Serializable
{

	private final int size1;
	private final int size2;
	final private List<Pair> [] rows;
	final private List<Pair> [] cols;



	public SparseMatrix(int size1, int size2) {
		this.size1=size1;
		this.size2=size2;
		rows = new List[size1];
		cols = new List[size2];
		for (int i=0; i<size1; i++) {
			rows[i]=new ArrayList<Pair>();
		}
		for (int j=0; j<size1; j++) {
			cols[j]=new ArrayList<Pair>();
		}
	}

	public List<Pair> getRow(int row) {
		return rows[row];
	}
	public List<Pair> getCol(int col) {
		return cols[col];
	}


	// The map where all matrix elements are stored.
	private final Map<Pair,Pair> matrixMap = new HashMap<Pair, Pair>();

	/**
	 * Returns the value found at the specified row and column of the matrix.
	 * Returns null if there's no value for the specified row and column.
	 * @param row Object
	 * @param col Object
	 * @return Object
	 */
	public Pair g(int row, int col)
	{
		Pair res=matrixMap.get(new Pair(row, col));
		return res;
	}

	/**
	 * Sets the value of the matrix at the specified row and column.
	 * @param row Object
	 * @param col Object
	 * @param value a value
	 */
	public void set(int row, int col, int value)
	{
		Pair p=new Pair(row, col,value);
		matrixMap.put(p, p);
		rows[row].add(p);
		cols[col].add(p);
	}

	public void setNoUpdate(int row, int col, int value)
	{
		Pair p=new Pair(row, col,value);
		matrixMap.put(p, p);

	}


	public void remove(int row, int col)
	{
		Pair p=new Pair(row, col);
		matrixMap.remove(p);
		rows[row].remove(p);
		cols[col].remove(p);
	}

	/**
	 * Returns a Set of all used "columns" in the matrix.
	 * @return Set
	 */
	public Set<Integer> colSet()
	{
		Set<Integer> colSet = new HashSet<Integer>();

		for (Iterator<Pair> iterator = matrixMap.keySet().iterator(); iterator.hasNext();)
		{
			Pair pair = iterator.next();
			colSet.add(pair.getSecond());
		}

		return colSet;
	}

	/**
	 * Returns a Set of all used "rows" in the matrix.
	 * @return Set
	 */
	public Set<Integer> rowSet()
	{
		Set<Integer> rowSet = new HashSet<Integer>();

		for (Iterator<Pair> iterator = matrixMap.keySet().iterator(); iterator.hasNext();)
		{
			Pair pair =  iterator.next();
			rowSet.add(pair.getFirst());
		}

		return rowSet;
	}

	public void clear()
	{
		matrixMap.clear();
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof SparseMatrix))
		{
			return false;
		}

		SparseMatrix other = (SparseMatrix) obj;

		return this.matrixMap.equals(other.matrixMap);
	}

	public int hashCode()
	{
		return matrixMap.hashCode();
	}

	public String toString()
	{
		return "SparseMatrix (" + size1 + "," + size2 + " (" + matrixMap.size() + " ," + matrixMap + ") elements)";
	}

	public void floydWarshall (SparseMatrix next) {
		floydWarshallWithIterator (next);
		//System.out.println(" ---------------> maximum " + maximum);
	}


	/*
    
    public void floydWarshallWithoutIterator (SparseMatrix next) {
    for (int k=0; k<size1; k++)  {
            for (int i=0; i<size1; i++)  {
                for (int j=0; j< size2; j++)  {
            Pair m_i_k;
            Pair m_k_j;
            Pair m_i_j;
                    if ((m_i_k=g(i,k))!=null) {
            double d_i_k=m_i_k;
            if ((m_k_j=g(k,j))!=null) {
                double d_k_j=m_k_j;
                if ((m_i_j=g(i,j))==null
                || d_i_k+d_k_j < m_i_j )
                {
                    double value=d_i_k+d_k_j;
                set(i,j,value);
                maximum=Math.max(maximum,value);
                next.set(i,j,(double) k);
                }
            }
            }
        }
        }
    }
    }
    */
	public int getMaximum() {
		return maximum;
	}

	private int maximum=0;

	/*
	 * The length is recorded as
	 * - 2*n if there is no strict ordering
	 * - 2*n-1 if there is some strict ordering
	 */

	final boolean isStrictOrdering(int n) {
		return n % 2==1;
	}
	public static final boolean isNonStrictOrdering(int n) {
		return n % 2==0;
	}
	final public int addAndPreserveStrictOrdering(int n1, int n2) {
		boolean strict1=isStrictOrdering(n1);
		boolean strict2=isStrictOrdering(n2);
		int val1=(strict1) ? n1+1 : n1;
		int val2=(strict2) ? n2+1 : n2;
		return (strict1 || strict2)? val1+val2-1 : val1+val2;

	}

	final public int roundedOrder(int n) {
		boolean strict=isStrictOrdering(n);
		int val=(strict) ? n+1 : n;
		return val;
	}

	final public void floydWarshallWithIterator (SparseMatrix next) {
		int[] col= new int[size1];
		int[] val= new int[size1];


		for (int k=0; k<size1; k++)  {
			List<Pair> col_k=getCol(k);

			for (Pair pair_i_k: col_k) {  // new ArrayList<Pair>(col_k)

				int i=pair_i_k.getFirst();

				int count=0;

				int d_i_k=pair_i_k.getValue();

				List<Pair> row_k=getRow(k);
				for (Pair pair_k_j: row_k) {
					int j=pair_k_j.getSecond();
					int d_k_j=pair_k_j.getValue();

					if ((i==k) || (k==j)) break;

					int d_i_j=addAndPreserveStrictOrdering(d_i_k, d_k_j);


					Pair m_i_j;

					/* The following test is a deviation of the original Floyd-Warshall algorithm.
					 * Originally, one updates the matrix if it contains nothing in g(i,j), or if the new
					 * values is smaller (this allows to find the shortest path).
					 * Here instead, we update if we have a strict path d_i_j than can replace a non strict path.
					 *  It is not guaranteed to be the shortest.
					 */
					if ((m_i_j=g(i,j))==null
						//|| d_i_k+d_k_j < m_i_j   // original algorithm: no need to find the optimum
					)
					{
						// only remember we did an update, if we have have actually added something.
						setNoUpdate(i,j,d_i_j);  // avoid invalidating with row iterators
						col[count]=j;
						val[count]=d_i_j;
						count++;
						maximum=Math.max(maximum,d_i_j);

					} else {

						if (isStrictOrdering(d_i_j)) {
							if (!(isStrictOrdering(m_i_j.getValue()))) {
								m_i_j.value=d_i_j;  //update the value in the matrix directly
								maximum=Math.max(maximum,d_i_j);
							}
						}

					}
				}

				for (int c=0; c<count; c++) {
					Pair pair=new Pair(i,col[c],val[c]);  // I could search the pair in the table, instead of allocating
					rows[i].add(pair);
					cols[col[c]].add(pair);
					// I am guaranteed not to overwrite column(k)
					// if col[c]=k, then j had value k. Therefore, g(i,j)=get(i,k) was known to be not null
				}
			}
		}

		//System.out.println(" ---------------> maximum " + maximum);
	}


}
 
