package com.treelogic.proteus.core.incops.statistics;

import java.util.List;
import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.resources.model.DataSerie;
import com.treelogic.proteus.resources.states.Stateful;
import com.treelogic.proteus.resources.states.StatefulCovariance;

/**
 * Incremental covariance using the updating formula described in
 * http://prod.sandia.gov/techlib/access-control.cgi/2008/086212.pdf
 *
 * @param <IN>
 *            Pojo type that contains the field to be analysed
 */
public class IncrementalCovariance<IN> extends IncrementalOperation<IN> {

	private static final long serialVersionUID = 1L;

	public IncrementalCovariance(IncrementalConfiguration configuration) {
		super(configuration, new StatefulCovariance());
	}

	@Override
	protected int numberOfRequiredDataSeries() {
		return 2;
	}

	@Override
	protected void updateWindow(String field, List<DataSerie> dataSeries, Stateful status) {
		status.apply(dataSeries);
	}


}
