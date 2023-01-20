/**
 * STAlign - Structural Tree Alignment
 * 
 * Copyright (C) 2022 BioShape and Data Science Lab at the University of Camerino, Italy - 
 * http://www.emanuelamerelli.eu/bigdata/
 * https://github.com/bdslab
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

public interface Operators {

    String CONCATENATION_LABEL = "CONC";
    String CROSSING_LABEL = "CROS";
    String NESTING_LABEL = "NEST";
    String MEETING_LABEL = "MEET";
    String ENDING_LABEL = "END";
    String STARTING_LABEL = "START";
    String DIAMOND_LABEL = "DIAMOND";
    String ALGEBRAIC_TREE_ROOT_LABEL = "ROOT";
    String HAIRPIN_LABEL = "L";

    String ALGEBRAIC_TREE_ROOT_LABEL_LATEX = "\\leftrightarrows";
    String CONCATENATION_LABEL_LATEX = "\\odot";
    String CROSSING_LABEL_LATEX = "\\Join";
    String NESTING_LABEL_LATEX = "\\Cap";
    String MEETING_LABEL_LATEX = "\\curlyvee";
    String ENDING_LABEL_LATEX = "\\rhd";
    String STARTING_LABEL_LATEX = "\\lhd";
    String DIAMOND_LABEL_LATEX = "\\diamond";
}
