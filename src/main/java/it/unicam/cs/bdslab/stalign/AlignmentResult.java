/**
 * STAlign - Structural Tree Alignment
 * 
 * Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino, Italy - 
 * http://www.emanuelamerelli.eu/bigdata/
 *  
 * This file is part of STAlign.
 * 
 * STAlign is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * STAlign is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with STAlign. If not, see <http://www.gnu.org/licenses/>.
 */
package it.unicam.cs.bdslab.stalign;

import fr.orsay.lri.varna.models.treealign.*;

/**
 * 
 * @author Luca Tesei
 *
 */
public class AlignmentResult {

    private final Tree<String> t1;
    private final Tree<String> t2;
    private final TreeAlignResult<String, String> result;
    private final double distance;

    /**
     * Align two structural RNA/Protein trees and construct the result.
     *
     * @param t1 first structural RNA/Protein tree to align
     * @param t2 second structural RNA/Protein tree to align
     *
     * @throws TreeAlignException alignment exception
     */
    public AlignmentResult(Tree<String> t1, Tree<String> t2,
	    ScoringFunction f) throws TreeAlignException {
	this.t1 = t1;
	this.t2 = t2;
	TreeAlign<String, String> al = new TreeAlign<>(f);
	this.result = al.align(t1, t2);
	this.distance = result.getDistance();
    }

    /**
     * @return the first structural RNA/Protein tree
     */
    public Tree<String> getT1() {
	return t1;
    }

    /**
     * @return the second structural RNA/Protein tree
     */
    public Tree<String> getT2() {
	return t2;
    }

    /**
     *
     * Return the distance of the aligned trees, i.e. the minimum cost of the
     * operations to align them.
     *
     * @return the distance
     */
    public double getDistance() {
	return distance;
    }

    /**
     *
     * @return the alignment of the original structural RNA trees
     */
    public Tree<AlignedNode<String, String>> getAlignedTree() {
	return this.result.getAlignment();
    }

}
