package it.unicam.cs.bdslab.stalign;

import fr.orsay.lri.varna.models.treealign.*;

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
        public AlignmentResult(Tree<String> t1, Tree<String> t2, ScoringFunction f) throws TreeAlignException {
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
