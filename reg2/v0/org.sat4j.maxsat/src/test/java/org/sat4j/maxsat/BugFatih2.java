/*******************************************************************************
 * SAT4J: a SATisfiability library for Java Copyright (C) 2004, 2012 Artois University and CNRS
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU Lesser General Public License Version 2.1 or later (the
 * "LGPL"), in which case the provisions of the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of the LGPL, and not to allow others to use your version of
 * this file under the terms of the EPL, indicate your decision by deleting
 * the provisions above and replace them with the notice and other provisions
 * required by the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of the EPL or the LGPL.
 *
 * Based on the original MiniSat specification from:
 *
 * An extensible SAT solver. Niklas Een and Niklas Sorensson. Proceedings of the
 * Sixth International Conference on Theory and Applications of Satisfiability
 * Testing, LNCS 2919, pp 502-518, 2003.
 *
 * See www.minisat.se for the original solver in C++.
 *
 * Contributors:
 *   CRIL - initial API and implementation
 *******************************************************************************/
package org.sat4j.maxsat;

import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;
import org.sat4j.tools.OptToSatAdapter;

public class BugFatih2 {

    @Test
    public void testBugReport() throws ContradictionException, TimeoutException {
        // ModelIterator solver = new ModelIterator(new OptToSatAdapter(
        // new MaxSatDecorator(SolverFactory.newDefault())));
        WeightedMaxSatDecorator maxSatSolver = new WeightedMaxSatDecorator(
                org.sat4j.maxsat.SolverFactory.newDefault());
        ModelIterator solver = new ModelIterator(new OptToSatAdapter(
                new PseudoOptDecorator(maxSatSolver)));
        System.out.println("Taille de voc : " + solver.nVars());
        solver.newVar(13);
        solver.setExpectedNumberOfClauses(24);
        maxSatSolver.addHardClause(new VecInt(new int[] { -1 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -2 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, 4 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, 5 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, 6 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -1, 7 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -2, 6 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -4, 3 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -5, 3 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -6, 3, 2 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -7, 1 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { 3, -1, 8 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, 1, 8 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, -1, 9 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -9 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { 1, -2, 10 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -1, 2, 10 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -1, -2, 11 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -10 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -11 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { 3, -1, 12 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, 1, 12 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -3, -1, 13 }));
        maxSatSolver.addHardClause(new VecInt(new int[] { -13 }));
        System.out.println("Taille de voc : " + solver.nVars());
        while (solver.isSatisfiable()) {
            System.out.println("Taille du modèle : " + solver.model().length);
            for (int i = 1; i <= solver.model().length; i++) {
                System.out.print(solver.model(i) + " ");
            }
            System.out.println();
        }
    }
}
