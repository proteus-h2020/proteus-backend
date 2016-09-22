package com.treelogic.proteus.core.incops.statistics;

import com.treelogic.proteus.core.configuration.IncrementalConfiguration;
import com.treelogic.proteus.resources.model.DataSerie;
import com.treelogic.proteus.resources.states.Stateful;
import com.treelogic.proteus.resources.states.StatefulMaximum;
import com.treelogic.proteus.resources.states.StatefulMinimum;

import java.util.List;

/**
 * Created by pablo.mesa on 20/09/16.
 */
public class IncrementalMinimum<IN> extends IncrementalOperation<IN> {


    private static final long serialVersionUID = 1L;

    public IncrementalMinimum(IncrementalConfiguration configuration) {
        super(configuration, new StatefulMinimum());
    }

    @Override
    protected void updateWindow(String field, List<DataSerie> dataSeries, Stateful status) {
        status.apply(dataSeries);
    }

    @Override
    protected int numberOfRequiredDataSeries() {
        return 1;
    }

}
