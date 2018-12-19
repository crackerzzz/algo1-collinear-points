import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private final List<LineSegment> collinearLines = new ArrayList<>();
  private final List<List<Point>> collinearSegments = new ArrayList<>();
  private int size;

  // finds all line segments containing 4 points
  public FastCollinearPoints(Point[] tpoints) {
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

    Point[] points = Arrays.copyOf(tpoints, tpoints.length);
    final int N = points.length;

    Arrays.sort(points);
    for (int i = 0; i < N - 1; i++) {
      if (points[i].compareTo(points[i + 1]) == 0) {
        throw new IllegalArgumentException();
      }
    }
    if (points.length < 4) {
      return;
    }

    // System.out.println("Sorted: " + Arrays.asList(points));

    for (int i = 0; i < N; i++) {
      final Point[] dpoints = Arrays.copyOf(points, N);
      Arrays.sort(dpoints, dpoints[i].slopeOrder());

      final Point pivot = dpoints[0];
      // System.out.println("Pivot: " + pivot);
      // System.out.println("Sorted: " + Arrays.asList(dpoints));
      int count = 0;
      int current = 1;
      double slope1 = pivot.slopeTo(dpoints[1]);
      for (int j = 2; j < N; j++) {
        double slope2 = pivot.slopeTo(dpoints[j]);

        if (slope1 != slope2) {
          if (count >= 2) {
            // collinear points
            addCollinear(dpoints, pivot, current, j, slope1);
          }
          count = 0;
          current = j;
          slope1 = slope2;
        } else {
          count++;
        }
      }

      if (count >= 2) {
        addCollinear(dpoints, pivot, current, N, slope1);
      }
    }

  }

  /**
   * 
   * @param pointsCopy
   * @param from       inclusive
   * @param to         exclusive
   * @param slope
   */
  private void addCollinear(Point[] pointsCopy, Point pivot, int from, int to, double slope) {
    // System.out.println("SLOPE: " + slope);
    final List<Point> subsegments = new ArrayList<>();
    subsegments.add(pivot);
    for (int i = from; i < to; i++) {
      subsegments.add(pointsCopy[i]);
    }
    // System.out.println("subsegments BEFORE sorting: " + subsegments);
    Collections.sort(subsegments);
    // System.out.println("subsegments AFTER sorting: " + subsegments);

    if (!alreadyExists(subsegments)) {
      collinearLines.add(new LineSegment(subsegments.get(0), subsegments.get(subsegments.size() - 1)));
      collinearSegments.add(subsegments);
      // System.out.println("Collinear: " + collinearLines);

      size++;
    }
  }

  private boolean alreadyExists(List<Point> subsegments) {
    if (collinearSegments.isEmpty()) {
      // System.out.println(String.format("New segment %s", subsegments));
      return false;
    }
    int count = 0;
    for (List<Point> sg : collinearSegments) {
      count = 0;
      // if atleast two points in sub segments exists, it is a sub-segment
      for (Point p : subsegments) {
        if (sg.contains(p) && ++count >= 2) {
          break;
        }
      }
      if (count >= 2) {
        // System.out.println(String.format("%s already exists", subsegments));
        return true;
      }
    }
    // System.out.println(String.format("New segment %s", subsegments));
    return false;
  }

  // the number of line segments
  public int numberOfSegments() {
    return size;
  }

  // the line segments
  public LineSegment[] segments() {
    return collinearLines.toArray(new LineSegment[0]);
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}

////System.out.println(String.format("Slope between %s -> %s = %f", pivot, pointsCopy[j], slope2));