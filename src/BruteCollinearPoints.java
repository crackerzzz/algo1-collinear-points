import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
  private final List<LineSegment> collinear = new ArrayList<>();
  private int size;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] tpoints) {

    // Throw a java.lang.IllegalArgumentException if the argument to the constructor
    // is null, if any point in the array is null, or if the argument to the
    // constructor contains a repeated point.
    if (tpoints == null) {
      throw new IllegalArgumentException();
    }
    for (Point p : tpoints) {
      if (p == null) {
        throw new IllegalArgumentException();
      }
    }

    Point[] pointsSorted = Arrays.copyOf(tpoints, tpoints.length);
    Arrays.sort(pointsSorted);
    for (int i = 0; i < pointsSorted.length - 1; i++) {
      if (pointsSorted[i].compareTo(pointsSorted[i + 1]) == 0) {
        throw new IllegalArgumentException();
      }
    }
    if (pointsSorted.length < 4) {
      return;
    }

    Point[] points = Arrays.copyOf(pointsSorted, pointsSorted.length);
    final int N = points.length;

    for (int i = 0; i < N - 3; i++) {
      final Point p = points[i];
      for (int j = i + 1; j < N - 2; j++) {
        final Point q = points[j];
        for (int k = j + 1; k < N - 1; k++) {
          final Point r = points[k];
          for (int m = k + 1; m < N; m++) {
            final Point s = points[m];
            if (isCollinear(p, q, r, s)) {
              LineSegment ls = new LineSegment(p, s);
//							System.out.println("Adding line segment: " + ls);
              collinear.add(ls);
              size++;
            }
          }
        }
      }
    }

  }

  private boolean isCollinear(Point p, Point q, Point r, Point s) {
    double pqSlope = p.slopeTo(q);
//		print(p, q, pqSlope);
    double prSlope = p.slopeTo(r);
//		print(p, r, prSlope);
    double psSlope = p.slopeTo(s);
//		print(p, s, psSlope);

    boolean slope = pqSlope == prSlope && prSlope == psSlope;
//		System.out.println("isCollinear: " + slope);
    return slope;
  }

//  private void print(Point a, Point b, double slope) {
//    System.out.println("Slope between " + a + " & " + b + " : " + slope);
//  }

  // the number of line segments
  public int numberOfSegments() {
    return size;
  }

  // the line segments
  public LineSegment[] segments() {
    return collinear.toArray(new LineSegment[0]);
  }

  public static void main(String[] args) {
    // read the n points from a file
    In in = new In("./samples/input10000.txt");
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}
