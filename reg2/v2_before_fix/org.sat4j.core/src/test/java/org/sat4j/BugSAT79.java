package org.sat4j;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.GateTranslator;

public class BugSAT79 {

    private ISolver solver;

    @Before
    public void setUp() throws Exception {
        solver = SolverFactory.newDefault();
        solver.addClause(new VecInt(new int[] { 1, -2, 3 }));
    }

    @Test
    public void testSuccessiveCallsInGlobalTimeout() throws TimeoutException,
            InterruptedException {
        int nbthreads = Thread.activeCount();
        for (int i = 0; i < 10; i++) {
            solver.isSatisfiable(true);
            Thread.sleep(500);
            assertEquals(nbthreads + 1, Thread.activeCount());
        }
    }

    // starting from here, the Timer thread exists, so it should not increase
    // the number of available threads.

    @Test
    public void testSuccessiveCallsInLocalTimeout() throws TimeoutException,
            InterruptedException {
        int nbthreads = Thread.activeCount();
        for (int i = 0; i < 10; i++) {
            solver.isSatisfiable(false);
            Thread.sleep(500);
            assertEquals(nbthreads, Thread.activeCount());
        }
    }

    @Test
    public void testSuccessiveCallsWithDifferentSolvers()
            throws TimeoutException, ContradictionException,
            InterruptedException {
        int nbthreads = Thread.activeCount();
        ISolver localSolver;
        for (int i = 0; i < 10; i++) {
            localSolver = SolverFactory.newDefault();
            localSolver.addClause(new VecInt(new int[] { 1, -2, 3 }));
            localSolver.isSatisfiable(false);
            Thread.sleep(500);
            assertEquals(nbthreads, Thread.activeCount());
        }
    }

    @Test
    public void testSuccessiveCallsWithDifferentSolversInsideDecorators()
            throws TimeoutException, ContradictionException,
            InterruptedException {
        int nbthreads = Thread.activeCount();
        ISolver localSolver;
        for (int i = 0; i < 10; i++) {
            localSolver = new GateTranslator(SolverFactory.newDefault());
            localSolver.addClause(new VecInt(new int[] { 1, -2, 3 }));
            localSolver.isSatisfiable(false);
            Thread.sleep(500);
            assertEquals(nbthreads, Thread.activeCount());
        }
    }
}
