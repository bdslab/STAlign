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

import at.unisalzburg.dbresearch.apted.costmodel.PerEditOperationStringNodeDataCostModel;
import at.unisalzburg.dbresearch.apted.distance.APTED;
import at.unisalzburg.dbresearch.apted.node.Node;
import at.unisalzburg.dbresearch.apted.node.StringNodeData;
import at.unisalzburg.dbresearch.apted.parser.BracketStringInputParser;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;

import java.io.IOException;

/**
 * @author Filippo Lampa
 */
public class TestEditDistance {

    public static void main(String[] args) {
        // Parse the input and transform to Node objects storing node information in MyNodeData.
        BracketStringInputParser parser = new BracketStringInputParser();
        Structure struc1 = null;
        Structure struc2= null;
        try {
            struc1 = StructureIO.getStructure("3mge");
            struc2 = StructureIO.getStructure("1ab1");
        } catch (IOException | StructureException e) {
            e.printStackTrace();
        }
        TertiaryStructure ts1 = new TertiaryStructure(struc1);
        TertiaryStructure ts2 = new TertiaryStructure(struc2);
        TERSAlignTree tat1 = new TERSAlignTree(ts1);
        TERSAlignTree tat2 = new TERSAlignTree(ts2);
        System.out.println("{" + TreeOutputter.treeToAptedInput(tat1.getStructuralTree()) + "}");
        System.out.println(TreeOutputter.treeToAptedInput(tat2.getStructuralTree()));
        Node<StringNodeData> t1 = parser.fromString(TreeOutputter.treeToAptedInput(tat1.getStructuralTree()));
        Node<StringNodeData> t2 = parser.fromString(TreeOutputter.treeToAptedInput(tat2.getStructuralTree()));
        // Initialise APTED.
        APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted = new APTED<>(new PerEditOperationStringNodeDataCostModel(100,100,100));
        // Execute APTED.
        float result = apted.computeEditDistance(t1, t2);
        System.out.println(result);
    }

}
