package org.openprovenance.prov.validation;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


//import no.uib.cipr.matrix.DenseMatrix;
//import no.uib.cipr.matrix.Matrix;


//import no.uib.cipr.matrix.sparse.CompDiagMatrix;
//import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
//import no.uib.cipr.matrix.Matrices;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.validation.matrix.Pair;
import org.openprovenance.prov.validation.matrix.SparseMatrix;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;

public class EventMatrix {

	static Logger logger = LogManager.getLogger(EventMatrix.class);

	final public SparseMatrix m;
	final SparseMatrix next;
	final int size;
	final Vector<String> eventKinds;

	public EventMatrix(int size, Vector<String> eventKinds) {
		this.size = size;
		this.eventKinds = eventKinds;
		m = new SparseMatrix(size, size);
		next = new SparseMatrix(size, size);
		logger.debug("created matrix");
	}

	final public static double epsilon = 0.001;
	final public static double minus_one = -1.00;

	public void floydWarshall() {
		m.floydWarshall(next);
	}

	/*
	 * public void floydWarshall_original () { for (int k=0; k<size; k++) { for
	 * (int i=0; i<size; i++) { for (int j=0; j< size; j++) { Double m_i_k;
	 * Double m_k_j; Double m_i_j; if ((m_i_k=m.g(i,k))!=null &&
	 * (m_k_j=m.g(k,j))!=null && m_i_k > epsilon && m_k_j > epsilon &&
	 * ((m_i_j=m.g(i,j))==null || m_i_j <= epsilon || m_i_k+m_k_j < m_i_j )) {
	 * m.set(i,j,m_i_k+m_k_j); next.set(i,j,(double) k); } } } } }
	 */

	@SuppressWarnings("unused")
	public String displayMatrix2() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		for (int i = 0; i < size; i++) {
			sb.append(eventKinds.get(i));
		}
		sb.append("\n");
		for (int i = 0; i < size; i++) {
			sb.append(eventKinds.get(i));
			for (int j = 0; j < size; j++) {
				Pair p;
				if ((p = m.g(i, j)) != null) {
					if (p.getValue() > 0) {
						sb.append("X");
					} else {
						if (i == j) {
							sb.append("\\");
						} else {
							sb.append(".");
						}
					}
				} else {
					if (i == j) {
						sb.append("\\");
					} else {
						sb.append(".");
					}
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	@SuppressWarnings("unused")
	public String displayMatrix3() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		for (int i = 0; i < size; i++) {
			sb.append(eventKinds.get(i));
		}
		sb.append("\n");
		for (int i = 0; i < size; i++) {
			sb.append(eventKinds.get(i));
			for (int j = 0; j < size; j++) {
				Pair p;
				if ((p = m.g(i, j)) != null) {
					int vv=p.getValue();
					if (vv > 0) {
						if (vv<10) {
							sb.append(vv);
						} else {
							sb.append("X");
						}
					} else {
						if (i == j) {
							sb.append("\\");
						} else {
							sb.append(".");
						}
					}
				} else {
					if (i == j) {
						sb.append("\\");
					} else {
						sb.append(".");
					}
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	public void generateImage1(String filename) throws IOException {
		logger.debug("--- generatedImage1 ");
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = writers.next();
		File f = new File(filename);
		ImageOutputStream ios = ImageIO.createImageOutputStream(f);
		writer.setOutput(ios);

		generateImage(writer, ios);

	}
	@SuppressWarnings("unused")
	public void generateImage1(OutputStream os) throws IOException {
		logger.debug("--- generatedImage1 ");
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = writers.next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		writer.setOutput(ios);
		generateImage(writer, ios);

	}

	public void generateImage(ImageWriter writer, ImageOutputStream ios) throws IOException {
		if (size <= 1) return;

		BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		int maxValue = 7 - 1;

		logger.debug("--- generatedImage1 ");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Pair p;
				if ((p = m.g(i, j)) != null && p.getValue() > 0) {
					int val = p.getValue();
					bi.setRGB(j, i, 255 * (maxValue + 1 - val) / maxValue * 256
							* 256 + 255 * (val - 1) / maxValue);
					// System.out.println(" value is ------ " + m.g(i,j));
				} else {
					if (i == j) {
						bi.setRGB(j, i, 70 * 256);
					} else {
						bi.setRGB(j, i, 0);
					}
				}
			}
		}

		logger.debug("--- generatedImage1 ");
		writer.write(bi);
		ios.flush();
		ios.close();

		logger.debug("--- generatedImage1 (end) ");
	}

	public List<Object> diagonal() {
		List<Object> res = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			Pair p;
			res.add((p = m.g(i, i)) == null ? null : p.getValue());
		}
		return res;
	}

	public List<Integer> getPath(int i, int j) {
		try {
			List<Integer> result = getPath2(i, j);
			if (result != null) {
				result.add(0, i);
				result.add(j);
				return result;
			} else {
				return new LinkedList<>();
			}
		} catch (UnsupportedOperationException e) {
			return new LinkedList<>();
		}
	}

	private List<Integer> getPath2(int i, int j) {
		//System.out.println("getpath2 " + i + " " + j);
		Pair pair_i_j = m.g(i, j);
		if (pair_i_j == null) {
			//System.out.println("getpath2 " + i + " " + j + " " + pair_i_j);


			// throw new UnsupportedOperationException();
		} else {
			int v_i_j = pair_i_j.getValue();
			int r_i_j=m.roundedOrder(v_i_j);
			//System.out.println("getpath2  (r_i_j) " + r_i_j);

			if (r_i_j == 2) {

				return new LinkedList<Integer>();
			}
			//System.out.println("getpath2  (row " + i + ") " + m.getRow(i));

			for (Pair pair_i_k : m.getRow(i)) {
				int k = pair_i_k.getSecond();
				int v_i_k = pair_i_k.getValue();
				int r_i_k=m.roundedOrder(v_i_k);

				//System.out.println("getpath2  (r_i_k) m(" + i + "," + k + ")" + r_i_k);


				Pair pair_k_j = m.g(k, j);
				if (pair_k_j != null) {
					int v_k_j = pair_k_j.getValue();
					int r_k_j=m.roundedOrder(v_k_j);

					//System.out.println("getpath2  (r_k_j) m(" + k + "," + j + ")" + r_k_j);

					if ((r_i_k + r_k_j) == r_i_j) {
						// found a plausible candidate
						List<Integer> path1 = getPath2(i, k);
						if (path1 != null) {
							List<Integer> path2 = getPath2(k, j);
							if (path2 != null) {
								List<Integer> result = new LinkedList<>();
								result.addAll(path1);
								result.add(k);
								result.addAll(path2);
								return result;
							}
						}
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	public List<Integer> getPath2_old(int i, int j) {
		if (i == j) return new LinkedList<>();
		if (m.g(i, j) == null || m.g(i, j).getValue() <= 0)
			throw new UnsupportedOperationException();

		if (next.g(i, j) == null) return new LinkedList<>();
		Pair p = next.g(i, j);
		int intermediate = p.getValue();
		if (intermediate == -1) return new LinkedList<>();
		List<Integer> result = getPath2(i, intermediate);
		result.add(intermediate);
		result.addAll(getPath2(intermediate, j));
		return result;
	}

	public List<Integer> orderEvents() {
		List<Integer> res = new LinkedList<Integer>();
		for (int i = 0; i < size; i++) {
			res.add(i);
		}
		res.sort(new EventComparator());
		return res;
	}

	public class EventComparator implements java.util.Comparator<Integer> {
		public int compare(Integer i1, Integer i2) {
			if (Objects.equals(i1, i2))
				return 0;
			Pair p_1_2;
			if (((p_1_2 = m.g(i1, i2)) == null) && ((m.g(i1, i2)) == null)) {
				return 0;
			} else if (m.g(i1, i2) == null) {
				return -1;
			} else if (p_1_2 == null) {
				return +1;
			} else {
				// i1 and i2 are unrelated
				// comparator requires a total order ...
				// fine, let's than order by event number
				return i1.compareTo(i2);
			}
		}

	}

	public int getMaximum() {
		return m.getMaximum();
	}

}
