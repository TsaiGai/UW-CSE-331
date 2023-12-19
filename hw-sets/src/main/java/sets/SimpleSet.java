/*
 * Copyright (C) 2022 Kevin Zatloukal and James Wilcox.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package sets;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {

  // TODO: fill in and document the representation
  //       Make sure to include the representation invariant (RI)
  //       and the abstraction function (AF).

  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    // TODO: implement this
    this.points = FiniteSet.of(vals);
    this.noComplement = false;
  }

  // Vals, which stores points, is filtered into a SimpleSet.
  //
  // RI: if the complement is false, -infinity = vals[0] < vals[1] < ... < vals[vals.length-1] = +infinity.
  // Otherwise, R \ (vals[0] < vals[1] < ... < vals[vals.length-1])
  // AF(this) = {vals[1], vals[2], ..., vals[vals.length-2]} or R \ ({vals[1], vals[2], ..., vals[vals.length-2]})

  // HINT: feel free to create other constructors!
  private final FiniteSet points;
  private final boolean noComplement;

  private SimpleSet(FiniteSet fs, boolean nc) {
    this.points = fs;
    this.noComplement = nc;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;
    return this.points.equals(other.points) && this.noComplement == other.noComplement;  // TODO: replace this with a correct check
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    // TODO: implement this
    return points.size();  // TODO: you should replace this value
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers. These
   *     floats will be turned into strings in the standard manner (the same as
   *     done by, e.g., String.valueOf(float)).
   */
  public String toString() {
    // TODO: implement this with a loop. document its invariant
    //       a StringBuilder may be useful for creating the string
    StringBuilder string = new StringBuilder();
    if (noComplement && points.getPoints().size() == 0) {
      return "R";
    } else if (noComplement) {
      string.append("R \\ ");
    }

    string.append("{");
    int i = 1;

    // Inv: string = str(p[1]), str(p[2]), ..., str(p[i])
    for (float point : points.getPoints()) {
      string.append(point);
      if (i < points.getPoints().size()) {
        string.append(", ");
      }
      i++;
    }

    string.append("}");
    return string.toString(); // TODO: you should replace this value
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct (hint: cases)

    // case 1 (finite set) - a SimpleSet with a finite amount of points will have a complement of
    // an infinite set, so we return a new SimpleSet with the opposite complement condition.
    // case 2 (infinite set) - a SimpleSet with an infinite amount of points will have a complement
    // of a finite set, so we return a new SimpleSet with the opposite complement condition.

    return new SimpleSet(this.points, !noComplement);  // TODO: you should replace this value
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct (hint: cases)

    // case 1 (this and other are finite) - a new SimpleSet composed of the union of the two
    // sets will be returned due to them both being finite. The new SimpleSet will have a false complement condition.
    // ex - this = {1, 2}, other = {2, 3}, union = {1, 2, 3}
    // case 2 (this is finite but other is infinite) - a new SimpleSet of the difference between the
    // complement of this and other will be returned. The new SimpleSet will have a true complement condition.
    // ex - this = {1, 2}, other = R \ {2, 3}, union = R \ {3}
    // case 3 (this is infinite but other is finite) - a new SimpleSet of the difference between the
    // complement of other and this will be returned. The new SimpleSet will have a true complement condition.
    // ex - this = R \ {1, 2}, other = {2, 3}, union = R \ {1}
    // case 4 (this and other are infinite) - a new SimpleSet composed of the intersection
    // between the complements of this and other will be returned. The new SimpleSet will have
    // a true complement condition.
    // ex - this = R \ {1, 2}, other = R \ {2, 3}, union = R \ {2}

    if (!this.noComplement && !other.noComplement) {
      return new SimpleSet(this.points.union(other.points), false);
    } else if (this.noComplement && !other.noComplement) {
      return new SimpleSet(this.complement().points.difference(other.points), true);
    } else if (!this.noComplement && other.noComplement) {
      return new SimpleSet(other.complement().points.difference(this.points), true);
    } else {
      return new SimpleSet(this.points.intersection(other.points), true);
    }

    // TODO: you should replace this value
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersected with other
   */
  public SimpleSet intersection(SimpleSet other) {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct
    // NOTE: There is more than one correct way to implement this.

    // case 1 (this and other are finite) - a new SimpleSet composed of the intersection of the two
    // sets will be returned due to them both being finite. The new SimpleSet will have a false complement condition.
    // ex - this = {1, 2}, other = {2, 3}, intersection = {2}
    // case 2 (this is finite but other is infinite) - a new SimpleSet of the difference between the
    // complement of other and this will be returned. The new SimpleSet will have a false complement condition.
    // ex - this = {1, 2, 3}, other = R \ {2, 3}, intersection = {1}
    // case 3 (this is infinite but other is finite) - a new SimpleSet of the difference between the
    // complement of this and other will be returned. The new SimpleSet will have a false complement condition.
    // ex - this = R \ {1, 2}, other = {1, 2, 3}, intersection = {1, 2}
    // case 4 (this and other are infinite) - a new SimpleSet composed of the union between
    // the complements of this and other will be returned. The new SimpleSet will have
    // a true complement condition.
    // ex - this = R \ {1, 2}, other = R \ {2, 3}, intersection = {2}

    if (!this.noComplement && !other.noComplement) {
      return new SimpleSet(this.points.intersection(other.points), false);
    } else if (this.noComplement && !other.noComplement) {
      return new SimpleSet(other.complement().points.difference(this.points), false);
    } else if (!this.noComplement && other.noComplement) {
      return new SimpleSet(this.complement().points.difference(other.points), false);
    } else {
      return new SimpleSet(this.points.union(other.points), true);
    }

    // TODO: you should replace this value
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    // TODO: implement this method
    //       include sufficient comments to see why it is correct
    // NOTE: There is more than one correct way to implement this.

    // case 1 (this and other are finite) - returning a set of the complement of this
    // and the complement of the union of other will return the difference.
    // ex - this = {1, 2, 3}, other = {2, 3}, difference = {1}
    // case 2 (this is finite but other is infinite) - returning a set of the complement of this
    // and the complement of the union of other will return the difference.
    // ex - this = {1, 2, 3}, other = R \ {2, 3}, difference = {2, 3}
    // case 3 (this is infinite but other is finite) - returning a set of the complement of this
    // and the complement of the union of other will return the difference.
    // ex - this = R \ {1, 2, 3}, other = {2, 3, 4}, difference = R \ {4}
    // case 4 (this and other are infinite) - returning a set of the complement of this
    // and the complement of the union of other will return the difference.
    // ex - this = R \ {1, 2, 3}, other = R \ {2, 3}, difference = R \ {1}

    return this.complement().union(other).complement(); // TODO: you should replace this value
  }
}
