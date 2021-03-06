package com.treelogic.proteus.resources.states;

import java.util.List;

import com.treelogic.proteus.resources.model.DataSerie;
import com.treelogic.proteus.resources.model.Pair;


/**
 * Function with state that calculates the variance for a global window and
 * keeps it's state so the operator can be used by incremental streams. <br>
 * This functions uses the Youngs and Crammer formula to calculate window's S
 * and T and the General Updating Formula to merge values from different
 * windows. <br>
 * For detailed information see
 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
 *
 */
public class StatefulVariance extends Stateful {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6944757930811209505L;
	private double S1 = 0, T1 = 0;
	private int windowCount = 0;

	@Override
	public Double value() {
		return this.value;
	}

	public void apply(DataSerie serie){
		List<Double> values = serie.values();
		Pair<Double, Double> t = youngsCrammerFormula(values);
		int elemsSize = values.size();

		windowCount++;

		if (windowCount == 1) {
			// First window
			S1 = t.key;
			T1 = t.value;

			this.value = t.key / (elemsSize - 1);
		} else {
			// Subsequent windows, general variance update formula 2.1
			double m = (windowCount - 1) * elemsSize, n = elemsSize;

			double S12 = generalUpdatingFormula(m, n, S1, t.key, T1, t.value);

			S1 = S12;
			T1 = T1 + t.value;

			this.value = S12 / (m + n - 1);
		}
	}
	@Override
	public void apply(List<DataSerie> series) {
		DataSerie serie = series.get(0);
		apply(serie);		
	}

	/**
	 * Implementation of the formula 2.1 from
	 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf.
	 * 
	 * @param m
	 * @param n
	 * @param S1
	 * @param S2
	 * @param T1
	 * @param T2
	 * @return
	 */
	private double generalUpdatingFormula(double m, double n, double S1, double S2, double T1, double T2) {

		double pow, f;

		pow = Math.pow(((n / m) * T1) - T2, 2);
		f = m / (n * (m + n));
		return S1 + S2 + (f * pow);
	}

	/**
	 * Computes the sum of squares of deviations from the mean using the Youngs
	 * and Crammer formula described in
	 * http://i.stanford.edu/pub/cstr/reports/cs/tr/79/773/CS-TR-79-773.pdf
	 * formula 1.5
	 * 
	 * @param elems
	 * @return S and T
	 */
	private Pair<Double, Double> youngsCrammerFormula(List<Double> elems) {
		double T = elems.get(0);
		double S = 0;

		for (int i = 1; i < elems.size(); i++) {
			T += elems.get(i);
			S = S + (Math.pow(((i + 1) * elems.get(i)) - T, 2) / ((double) i * (i + 1)));
		}

		return new Pair<>(S, T);
	}
}