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

import fr.orsay.lri.varna.models.treealign.*;
import org.biojava.nbio.structure.contact.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Structural RNA/Protein's tree builder based on a given RNA/Protein's
 * tertiary structure. A structural tree is a tree description of the
 * structural part of an RNA/Protein tertiary structure that may contain any
 * kind of pseudo knot. It is used to align tertiary structures and calculate
 * the distance between them.
 *
 * @author Filippo Lampa
 *
 */
public class TERSAlignTree {

    private TertiaryStructure tertiaryStructure;
    private SecondaryStructure secondaryStructure;
    private Tree<String> structuralTree;
    private int sequenceLength;

    /**
     * Initializes a new structural tree builder for the tertiary structure
     * given as parameter
     * 
     * @param tertiaryStructure a Protein/RNA tertiary structure
     */
    public TERSAlignTree(TertiaryStructure tertiaryStructure) {
	this.tertiaryStructure = tertiaryStructure;
	this.sequenceLength = this.tertiaryStructure.getSequence().length();
	this.structuralTree = null;
    }

    /**
     * Initializes a new structural tree builder for the secondary structure
     * given as parameter
     * 
     * @param secondaryStructure a Protein/RNA secondary structure
     */
    public TERSAlignTree(SecondaryStructure secondaryStructure) {
	this.secondaryStructure = secondaryStructure;
	this.sequenceLength = this.secondaryStructure.getSecStrucState()
		.size();
	this.structuralTree = null;
    }

    /**
     * @return the Protein/RNA tertiary structure associated to this builder
     */
    public TertiaryStructure getTertiaryStructure() {
	if (tertiaryStructure == null)
	    throw new NullPointerException(
		    "Tertiary structure not initialized");
	return this.tertiaryStructure;
    }

    /**
     * @return the calculated tertiary structure structural tree
     */
    public Tree<String> getStructuralTree() {
	if (this.structuralTree == null)
	    buildStructural();
	return this.structuralTree;
    }

    /**
     * Construct the structural tree root children and finds the outermost
     * pseudoloop. Then the recursive builder is started on this pseudoloop to
     * construct the full structural tree recursively.
     */
    private void buildStructural() {

	int[] m = new int[this.sequenceLength + 1];
	int[] c = new int[this.sequenceLength + 1];
	ArrayList<Integer>[] p;
	// If there is a secondary structure
	if (this.secondaryStructure != null) {
	    p = secondaryStructure.getBondsList();
	} else {
	    // There is a tertiary structure
	    p = new ArrayList[this.sequenceLength + 1];
	    // initialize the pointers array
	    initp(p);
	}

	// initialize counting and meets array
	initmc(m, c, p);

	// init indexes for later recursion call
	int l = 1; // left index
	int r = this.sequenceLength; // right index

	// move l to the start of the structure
	while (c[l] == 0)
	    l++;

	// move r to the tail of the structure
	while (c[r] == 0)
	    r--;
	r++; // last closing loop has 0 count, but belongs to the loop, so
	     // it's not part of
	// the tail

	// the largest pseudoloop is now identified by the interval [l,r]
	assert c[l] >= 1 && c[r] == 0 : "Largest pseudoloop at [" + l + ","
		+ r + "]\nCounting array: " + Arrays.toString(c);

	// create an empty list of zero intervals to detect concatenations
	ArrayList<Interval> zi = new ArrayList<>();

	// find zero intervals in the outermost pseudoloop, if any
	detectZeroIntervals(c, zi, l, r);

	// create an empty list of meets indexes to detect meetings
	ArrayList<Integer> meetIndexesList = new ArrayList<>();

	// find meets in the new pseudoloop, if any
	getMeetsInInterval(meetIndexesList, l, r, c, m, p);

	// create the root node of the structural RNA tree
	Tree<String> t = new Tree<>();

	// start the recursive construction of the structural RNA Tree on the
	// node ct
	recBuildStructural(t, meetIndexesList, zi, c, p, m, l, r);

	// assign to the root of this tree
	this.structuralTree = t;

    }

    private void recBuildStructural(Tree<String> ct,
	    ArrayList<Integer> meetsInInterval, ArrayList<Interval> zi,
	    int[] c, ArrayList<Integer>[] p, int[] m, int l, int r) {

	assert c[l] >= 1 && c[r] == 0
		: "Pseudoloop bounds error while parsing at [" + l + "," + r
			+ "]\nCounting array: " + Arrays.toString(c);

	if ((!meetsInInterval.isEmpty() && meetsInInterval
		.get(meetsInInterval.size() - 1).equals(p[r].get(0)))
		|| !zi.isEmpty()) {
	    int rl = 0;
	    int lr = 0;
	    // value inside c array's last position is different between left
	    // and right substructures in case of a meet, so we proceed to
	    // clone it
	    int[] meetConcatC = c.clone();
	    ArrayList<Integer>[] leftP = p;
	    ArrayList<Integer>[] rightP = p;
	    if (!meetsInInterval.isEmpty()
		    && meetsInInterval.contains(p[r].get(0))) {
		// meet case

		int meetPoint = p[r].get(0);

		ct.setValue(Operators.MEETING_LABEL);

		leftP = new ArrayList[p.length];
		rightP = new ArrayList[p.length];
		copyArrayOfArrayList(p, leftP);
		copyArrayOfArrayList(p, rightP);
		// filter every parent of l, m which is not between l and m
		this.filterPartnerList(l, meetPoint, leftP);
		// filter every parent of l, m which is not between m and r
		this.filterPartnerList(meetPoint, r, rightP);

		// set boundaries of the right pseudoloop and of the left
		// pseudoloop
		lr = meetPoint;
		rl = meetPoint;

		// when two pseudoloops are split by a meet, the last value of
		// the left pseudoloop must be set to 0 since the hairpins
		// starting from the meet point
		// are not considered within the left pseudoloop context
		meetConcatC[meetPoint] = 0;

	    } else if (!zi.isEmpty()) {
		// concat case
		ct.setValue(Operators.CONCATENATION_LABEL);

		// get rightmost zero interval
		Interval rmzi = zi.get(zi.size() - 1);
		zi.remove(zi.size() - 1);

		// find boundaries of the right pseudoloop and of the left
		// pseudoloop
		lr = rmzi.i - 1;
		rl = rmzi.j;
	    }
	    // the new right pseudoloop to consider has bounds [rl,rr]
	    assert c[rl] >= 1 && c[r] == 0
		    : "Determined wrong pseudoloop at [" + rl + "," + r
			    + "]\nCounting array: " + Arrays.toString(c);

	    // create the node for building the left part
	    Tree<String> left = new Tree<>();

	    // create node for building the right part
	    Tree<String> right = new Tree<>();

	    // update tree
	    ArrayList<Tree<String>> meetConcChilds = new ArrayList<>();
	    meetConcChilds.add(left);
	    meetConcChilds.add(right);
	    ct.replaceChildrenListBy(meetConcChilds);

	    // create two empty lists of zero intervals to detect
	    // concatenations in the left and right pseudoloops
	    ArrayList<Interval> zirRight = new ArrayList<>();
	    ArrayList<Interval> zirLeft = new ArrayList<>();

	    // find zero intervals in the right and left pseudoloops, if any
	    detectZeroIntervals(c, zirRight, rl, r);
	    detectZeroIntervals(meetConcatC, zirLeft, l, lr);

	    // create two empty lists of meet indexes to detect meetings in
	    // the right and left pseudoloops
	    ArrayList<Integer> meetIndexesListRight = new ArrayList<>();
	    ArrayList<Integer> meetIndexesListLeft = new ArrayList<>();

	    // find meets in the right and left pseudoloops, if any
	    getMeetsInInterval(meetIndexesListRight, rl, r, c, m, rightP);
	    getMeetsInInterval(meetIndexesListLeft, l, lr, meetConcatC, m,
		    leftP);

	    // recursive construction of the structural subTree on the right
	    recBuildStructural(right, meetIndexesListRight, zirRight, c,
		    rightP, m, rl, r);
	    // recursive construction of the structural subTree on the left
	    recBuildStructural(left, meetIndexesListLeft, zirLeft,
		    meetConcatC, leftP, m, l, lr);
	} else {
	    if (p[r].get(0) > l) {
		// cross case

		// determine number of crossings and set label
		int numberOfCrossings = determineNumberOfCrossings(p,
			p[r].get(0));
		ct.setValue("(" + Operators.CROSSING_LABEL + ","
			+ numberOfCrossings + ")");

		// left end of the rightmost crossing hairpin
		int lpp = p[r].get(0);
		// index for the right end of the new pseudoloop
		int rp = r;

		// decrease counting array according to the elimination of
		// this hairpin
		for (int i = lpp; i < r; i++) {
		    c[i]--;
		}

		// determine the ending of the last loop on the right
		while (c[rp] == 0)
		    rp--;
		rp++; // last closing loop has 0 count, but belongs to the
		      // loop

		// the new pseudoloop to consider has bounds [l,rp]
		assert c[l] >= 1 && c[rp] == 0
			: "Determined wrong pseudoloop at [" + l + "," + rp
				+ "]\nCounting array: " + Arrays.toString(c);

		// create the empty node for building the rest of the tree on
		// the left
		Tree<String> rest = new Tree<>();

		// create hairpin subtree
		Tree<String> h = new Tree<>();
		h.setValue(Operators.HAIRPIN_LABEL + "(" + p[r].get(0) + ","
			+ r + ")");

		// update tree
		ArrayList<Tree<String>> crossChilds = new ArrayList<>();
		crossChilds.add(rest);
		crossChilds.add(h);
		ct.replaceChildrenListBy(crossChilds);

		// create an empty list of zero intervals to detect
		// concatenations
		ArrayList<Interval> zip = new ArrayList<>();

		// find zero intervals in the new pseudoloop, if any
		detectZeroIntervals(c, zip, l, rp);

		// remove partner indexes from both hairpin ending's p arrays
		p[p[r].get(0)].remove(Integer.valueOf(r));
		p[r].remove(p[r].get(0));

		// create an empty list of meet indexes to detect meetings
		ArrayList<Integer> meetIndexesList = new ArrayList<>();

		// find meets in the new pseudoloop, if any
		getMeetsInInterval(meetIndexesList, l, rp, c, m, p);

		// recursive construction of the structural subTree on the
		// node rest
		recBuildStructural(rest, meetIndexesList, zip, c, p, m, l,
			rp);

	    } else {
		if (p[r].size() > 1 && p[p[r].get(0)].size() == 1
			&& p[r].get(0) == l) {
		    // ending case

		    ct.setValue(Operators.ENDING_LABEL);

		    // decrease counting array according to the elimination of
		    // this hairpin
		    for (int i = l; i < r; i++)
			c[i]--;

		    // init indexes for later recursion call
		    int lp = l + 1;

		    // determine the starting of the next loop on the left
		    while (c[lp] == 0 && lp < r)
			lp++;
		    if (lp == r) {
			// no subloops of this ending, there will be no more
			// complex subtrees
			// revert to just a single hairpin
			ct.setValue(Operators.HAIRPIN_LABEL + "("
				+ p[r].get(0) + "," + r + ")");

			// end recursion
			return;
		    }

		    // the new pseudoloop to consider has bounds [lp,rp]
		    assert c[lp] >= 1 && c[r] == 0
			    : "Determined wrong pseudoloop at [" + lp + ","
				    + r + "]\nCounting array: "
				    + Arrays.toString(c);

		    // create the empty node for building the rest of the tree
		    // on the left
		    Tree<String> rest = new Tree<>();

		    // create hairpin subtree
		    Tree<String> h = new Tree<>();
		    h.setValue(Operators.HAIRPIN_LABEL + "(" + p[r].get(0)
			    + "," + r + ")");

		    // update tree
		    ArrayList<Tree<String>> endChild = new ArrayList<>();
		    endChild.add(rest);
		    endChild.add(h);
		    ct.replaceChildrenListBy(endChild);

		    // create an empty list of zero intervals to detect
		    // concatenations
		    ArrayList<Interval> zip = new ArrayList<>();

		    // find zero intervals in the new pseudoloop, if any
		    detectZeroIntervals(c, zip, lp, r);

		    // remove partner indexes from both hairpin ending's p
		    // arrays
		    p[p[r].get(0)].remove(Integer.valueOf(r));
		    p[r].remove(p[r].get(0));

		    // create an empty list of meet indexes to detect meetings
		    ArrayList<Integer> meetIndexesList = new ArrayList<>();

		    // find meets in the new pseudoloop, if any
		    getMeetsInInterval(meetIndexesList, lp, r, c, m, p);

		    // recursive construction of the structural subTree on the
		    // node rest
		    recBuildStructural(rest, meetIndexesList, zip, c, p, m,
			    lp, r);

		} else if (p[r].size() == 1 && p[p[r].get(0)].size() > 1
			&& p[r].get(0) == l) {
		    // starting case

		    ct.setValue(Operators.STARTING_LABEL);

		    // decrease counting array according to the elimination of
		    // this hairpin
		    for (int i = l; i < r; i++)
			c[i]--;

		    // init indexes for later recursion call
		    int rp = r;

		    // determine the ending of the last loop on the right
		    while (c[rp] == 0)
			rp--;
		    rp++; // last closing loop has 0 count, but belongs to the
			  // loop

		    // the new pseudoloop to consider has bounds [lp,rp]
		    assert c[l] >= 1 && c[rp] == 0
			    : "Determined wrong pseudoloop at [" + l + ","
				    + rp + "]\nCounting array: "
				    + Arrays.toString(c);

		    // create the empty node for building the rest of the tree
		    // on the left
		    Tree<String> rest = new Tree<>();

		    // create hairpin subtree
		    Tree<String> h = new Tree<>();
		    h.setValue(Operators.HAIRPIN_LABEL + "(" + p[r].get(0)
			    + "," + r + ")");

		    // update tree
		    ArrayList<Tree<String>> startChids = new ArrayList<>();
		    startChids.add(rest);
		    startChids.add(h);
		    ct.replaceChildrenListBy(startChids);

		    // create an empty list of zero intervals to detect
		    // concatenations
		    ArrayList<Interval> zip = new ArrayList<>();

		    // find zero intervals in the new pseudoloop, if any
		    detectZeroIntervals(c, zip, l, rp);

		    // remove partner indexes from both hairpin ending's p
		    // arrays
		    p[p[r].get(0)].remove(Integer.valueOf(r));
		    p[r].remove(p[r].get(0));

		    // create an empty list of meet indexes to detect meetings
		    ArrayList<Integer> meetIndexesList = new ArrayList<>();

		    // find meets in the new pseudoloop, if any
		    getMeetsInInterval(meetIndexesList, l, rp, c, m, p);

		    // recursive construction of the structural subTree on the
		    // node rest
		    recBuildStructural(rest, meetIndexesList, zip, c, p, m, l,
			    rp);

		} else if (p[r].size() > 1 && p[p[r].get(0)].size() > 1
			&& p[r].get(0) == l) {
		    // diamond case

		    ct.setValue(Operators.DIAMOND_LABEL);

		    // decrease counting array according to the elimination of
		    // this hairpin
		    for (int i = l; i < r; i++)
			c[i]--;

		    // indexes for later recursion call are l and r

		    // starting and ending of the loop are l and r

		    // the new pseudoloop to consider has bounds [lp,rp]
		    assert c[l] >= 1 && c[r] == 0
			    : "Determined wrong pseudoloop at [" + l + "," + r
				    + "]\nCounting array: "
				    + Arrays.toString(c);

		    // create the empty node for building the rest of the tree
		    // on the left
		    Tree<String> rest = new Tree<>();

		    // create hairpin subtree
		    Tree<String> h = new Tree<>();
		    h.setValue(Operators.HAIRPIN_LABEL + "(" + p[r].get(0)
			    + "," + r + ")");

		    // update tree
		    ArrayList<Tree<String>> diamondChilds = new ArrayList<>();
		    diamondChilds.add(rest);
		    diamondChilds.add(h);
		    ct.replaceChildrenListBy(diamondChilds);

		    // create an empty list of zero intervals to detect
		    // concatenations
		    ArrayList<Interval> zip = new ArrayList<>();

		    // find zero intervals in the new pseudoloop, if any
		    detectZeroIntervals(c, zip, l, r);

		    // remove partner indexes from both hairpin ending's p
		    // arrays
		    p[p[r].get(0)].remove(Integer.valueOf(r));
		    p[r].remove(p[r].get(0));

		    // create an empty list of meet indexes to detect meetings
		    ArrayList<Integer> meetIndexesList = new ArrayList<>();

		    // find meets in the new pseudoloop, if any
		    getMeetsInInterval(meetIndexesList, l, r, c, m, p);

		    // recursive construction of the structural subTree on the
		    // node rest
		    recBuildStructural(rest, meetIndexesList, zip, c, p, m, l,
			    r);

		} else {
		    // nest case
		    ct.setValue(Operators.NESTING_LABEL);

		    // decrease counting array according to the elimination of
		    // this hairpin
		    for (int i = l; i < r; i++)
			c[i]--;

		    // init indexes for later recursion call
		    int lp = l + 1;
		    int rp = r;

		    // determine the starting of the next loop on the left and
		    // increment k
		    while (c[lp] == 0 && lp < rp)
			lp++;
		    if (lp == rp) {
			// no subloops of this nesting, there will be no more
			// complex subtrees

			// revert to just a single hairpin
			ct.setValue(Operators.HAIRPIN_LABEL + "("
				+ p[r].get(0) + "," + r + ")");

			// end recursion
			return;
		    }

		    // determine the ending of the last loop on the right
		    while (c[rp] == 0)
			rp--;
		    rp++; // last closing loop has 0 count, but belongs to the
			  // loop

		    // the new pseudoloop to consider has bounds [lp,rp]
		    assert c[lp] >= 1 && c[rp] == 0
			    : "Determined wrong pseudoloop at [" + lp + ","
				    + rp + "]\nCounting array: "
				    + Arrays.toString(c);

		    // create the empty node for building the rest of the tree
		    // on the left
		    Tree<String> rest = new Tree<>();

		    // create hairpin subtree
		    Tree<String> h = new Tree<>();
		    h.setValue(Operators.HAIRPIN_LABEL + "(" + p[r].get(0)
			    + "," + r + ")");

		    // update tree
		    ArrayList<Tree<String>> nestChilds = new ArrayList<>();
		    nestChilds.add(rest);
		    nestChilds.add(h);
		    ct.replaceChildrenListBy(nestChilds);

		    // create an empty list of zero intervals to detect
		    // concatenations
		    ArrayList<Interval> zip = new ArrayList<>();

		    // find zero intervals in the new pseudoloop, if any
		    detectZeroIntervals(c, zip, lp, rp);

		    // remove partner indexes from both hairpin ending's p
		    // arrays
		    p[p[r].get(0)].remove(Integer.valueOf(r));
		    p[r].remove(p[r].get(0));

		    // create an empty list of meet indexes to detect meetings
		    ArrayList<Integer> meetIndexesList = new ArrayList<>();

		    // find meets in the new pseudoloop, if any
		    getMeetsInInterval(meetIndexesList, lp, rp, c, m, p);

		    // recursive construction of the structural subTree on the
		    // node rest
		    recBuildStructural(rest, meetIndexesList, zip, c, p, m,
			    lp, rp);

		}
	    }
	}
    }

    /*
     * Creates a deep copy of Array of ArrayLists P
     * 
     * @param array source p array
     * 
     * @param copyArray destination p array
     */
    private void copyArrayOfArrayList(ArrayList<Integer>[] array,
	    ArrayList<Integer>[] copyArray) {
	for (int i = 0; i < array.length; i++) {
	    if (!(array[i] == null))
		copyArray[i] = new ArrayList<>(array[i]);
	}
    }

    /*
     * Filters every parent of l and r which is not inside the interval l - r
     * 
     * @param l interval's starting index
     * 
     * @param r interval's ending index
     * 
     * @param p1 partners array
     */
    private void filterPartnerList(int l, int r, ArrayList<Integer>[] p1) {
	for (int i = 0; i < p1[l].size(); i++) {
	    Integer partnerIndex = p1[l].get(i);
	    if (partnerIndex < l || partnerIndex > r) {
		p1[l].remove(partnerIndex);
		p1[partnerIndex].remove(Integer.valueOf(l));
		i--;
	    }
	}
	for (int i = 0; i < p1[r].size(); i++) {
	    Integer partnerIndex = p1[r].get(i);
	    if (partnerIndex < l || partnerIndex > r) {
		p1[r].remove(partnerIndex);
		p1[partnerIndex].remove(Integer.valueOf(r));
		i--;
	    }
	}
    }

    /*
     * @param p partners array
     * 
     * @param index index to check
     * 
     * @return number of hairpins starting from the given index
     */
    private int countExitingHairpins(ArrayList<Integer>[] p, int index) {
	int count = 0;
	for (Integer partner : p[index]) {
	    if (partner > index)
		count++;
	}
	return count;
    }

    /*
     * Finds all the (possibly empty) meets inside the pseudoloop [l,r]. A
     * meet is an index of the primary sequence that separates two
     * pseudoloops. A meet happens when two or more hairpins meet in the same
     * index (i), the value of c[i] > m[i] and the value of c[i] is less or
     * equal than the number of hairpins starting in the said index. All the
     * found meets are put into the list meetList.
     */
    private void getMeetsInInterval(ArrayList<Integer> meetList, int l, int r,
	    int[] c, int[] m, ArrayList<Integer>[] p) {
	for (int i = l + 1; i < r; i++) {
	    if (m[i] != 0) {
		m[i] = p[i].size();
		if (c[i] < m[i]) {
		    if (!(c[i] > countExitingHairpins(p, i)))
			meetList.add(i);
		}
	    }
	}
    }

    private int determineNumberOfCrossings(ArrayList<Integer>[] p,
	    int bondStart) {
	int n = 0;
	for (int i = 0; i < p.length; i++) {
	    if (p[i] != null) {
		for (Integer currentPartner : p[i]) {
		    if (i < bondStart && bondStart < currentPartner)
			n++;
		}
	    }
	}
	assert n > 0 : "Crossing number equal to zero!";
	return n;
    }

    /*
     * Finds all the (possibly empty) zero intervals inside the pseudoloop
     * [l,r]. A zero interval is a section of the primary sequence that
     * separates two concatenated pseudoloops. It is called zero interval
     * because in the counting array the count goes to zero before the end of
     * the pseudoloop. A zero interval always starts at the first position
     * after the count went to zero. It stops at the first position in which
     * the counting raises to one again. If these positions coincide the zero
     * interval is empty. All the found intervals are put into the list zi,
     * which is assumed to be empty at the calling time.
     */
    private void detectZeroIntervals(int[] c, ArrayList<Interval> zi, int l,
	    int r) {
	assert l < r : "Empty pseudoloop while detecting zero intervals at ["
		+ l + "," + r + "]";
	assert c[l] >= 1 && c[r] == 0
		: "Pseudoloop bounds error while detecting zero intervals at ["
			+ l + "," + r + "]\nCounting array: "
			+ Arrays.toString(c);
	assert zi.isEmpty()
		: "Not empty zero interval list while detecting zero intervals: "
			+ zi;
	int i = l;
	do {
	    // search for the next zero interval
	    while (c[i] != 0)
		i++;
	    if (i == r)
		break; // reached end of the interval, stop searching more
		       // zero intervals
	    i++;
	    // determine start of the zero interval
	    int start = i;
	    // search for the end of the zero interval
	    while (c[i] == 0)
		i++;
	    // determine the stop of the zero interval, if start == stop, the
	    // zero interval
	    // is empty
	    int stop = i;
	    // create the interval and add it to the list
	    Interval interval = new Interval(start, stop);
	    zi.add(interval);
	} while (true);
    }

    /*
     * /* Initializes both meets and counter arrays
     */
    private void initmc(int[] m, int[] c, ArrayList<Integer>[] p) {
	int count = 0;
	int currentIndexStartingLoops;
	int currentIndexStoppingLoops;
	for (int i = 1; i <= this.sequenceLength; i++) {
	    if (!(p[i] == null)) {
		currentIndexStartingLoops = getStartingLoopsNumber(i, p);
		currentIndexStoppingLoops = getStoppingLoopsNumber(i, p);

		count = count + (currentIndexStartingLoops
			- currentIndexStoppingLoops);
		if (currentIndexStartingLoops > 0
			&& currentIndexStoppingLoops > 0) {
		    // a loop stops and another starts in the current index,
		    // so this is a meet
		    m[i] = p[i].size();
		}
	    }
	    c[i] = count;
	}
	assert count == 0
		: "Value of count after initialization of counting array: "
			+ count + "\nCounting array: " + Arrays.toString(c);
    }

    /*
     * Initializes partners array
     */
    private void initp(ArrayList<Integer>[] p) {
	ArrayList<Pair<Integer>> bondList = this.tertiaryStructure
		.getBondList();
	if (bondList.size() == 0) {
	    System.err.println(
		    "No bonds detected with the current threshold, can't generate the associated tree");
	    System.exit(1);
	}
	for (Pair<Integer> currentBond : bondList) {
	    if (!currentBond.getFirst().equals(currentBond.getSecond())) {
		if (p[currentBond.getFirst()] == null) {
		    p[currentBond.getFirst()] = new ArrayList<>();
		}
		p[currentBond.getFirst()].add(currentBond.getSecond());
		if (p[currentBond.getSecond()] == null) {
		    p[currentBond.getSecond()] = new ArrayList<>();
		}
		p[currentBond.getSecond()].add(currentBond.getFirst());
	    }
	}
    }

    /*
     * Tells if at position i there is the starting of an hairpin loop of the
     * secondary structure represented by the original arc annotated sequence.
     */
    private int getStartingLoopsNumber(int i, ArrayList<Integer>[] p) {
	int startingLoopsNumber = 0;
	if (!(p[i] == null))
	    for (Integer partner : p[i])
		if (partner > i)
		    startingLoopsNumber++;
	return startingLoopsNumber;
    }

    /*
     * Tells if at position i there is the ending of an hairpin loop of the
     * secondary structure represented by the original arc annotated sequence.
     */
    private int getStoppingLoopsNumber(int i, ArrayList<Integer>[] p) {
	int stoppingLoopsNumber = 0;
	if (!(p[i] == null))
	    for (Integer partner : p[i])
		if (partner < i)
		    stoppingLoopsNumber++;
	return stoppingLoopsNumber;
    }

    /**
     * FOR TESTS PURPOSES Replace current sequence length with a new one.
     * 
     * @param sequenceLength new sequence length
     */
    public void setSequenceLength(int sequenceLength) {
	this.sequenceLength = sequenceLength;
    }

    public SecondaryStructure getSecondaryStructure() {
	if (secondaryStructure == null)
	    throw new NullPointerException(
		    "Tertiary structure not initialized");
	return this.secondaryStructure;
    }

    /*
     * Service class for holding zero intervals.
     */
    protected class Interval {
	private final int i;
	private final int j;

	protected Interval(int i, int j) {
	    assert i <= j : "Interval [" + i + "," + j + "]";
	    this.i = i;
	    this.j = j;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + getOuterType().hashCode();
	    result = prime * result + i;
	    result = prime * result + j;
	    return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    Interval other = (Interval) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (i != other.i)
		return false;
	    return j == other.j;
	}

	private TERSAlignTree getOuterType() {
	    return TERSAlignTree.this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "Interval [i=" + i + ", j=" + j + "]";
	}

    }

}
