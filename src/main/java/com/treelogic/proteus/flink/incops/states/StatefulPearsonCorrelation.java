package com.treelogic.proteus.flink.incops.states;

import java.util.List;

import com.treelogic.proteus.flink.incops.pojos.DataSerie;

public class StatefulPearsonCorrelation extends Stateful<Double>{

	private static final long serialVersionUID = 1L;
	private StatefulVariance xVariance = new StatefulVariance(),
			yVariance = new StatefulVariance();
	private StatefulCovariance covariance = new StatefulCovariance();
	

	@Override
	public Double value() {
		return this.value;
	}

	@Override
	public void apply(List<DataSerie> series) {
		DataSerie xSerie = series.get(0);
		DataSerie ySerie = series.get(1);
		
		List<Double> xElems = xSerie.values();
		List<Double> yElems = ySerie.values();
		
		xVariance.apply(xSerie);
		double x = xVariance.value();
		
		yVariance.apply(ySerie);
		double y = yVariance.value;
		
		covariance.apply(series);
		double c = covariance.value();
		
		this.value = ( c / (Math.sqrt(x * y)) );		
	}
	

}