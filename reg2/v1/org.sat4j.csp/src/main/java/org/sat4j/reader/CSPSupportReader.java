/*******************************************************************************
* SAT4J: a SATisfiability library for Java Copyright (C) 2004-2008 Daniel Le Berre
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
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
*******************************************************************************/
package org.sat4j.reader;

import org.sat4j.csp.constraints.GentSupports;
import org.sat4j.specs.ISolver;

public class CSPSupportReader extends CSPReader {

    public CSPSupportReader(ISolver solver,boolean allDiffCard) {
        super(solver,allDiffCard);
    }

    @Override
    protected void manageAllowedTuples(int relnum, int arity, int nbtuples) {
        relations[relnum] = new GentSupports(arity, nbtuples);
    }
}
