import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private final List<LineSegment> collinearLines = new ArrayList<>();
  final List<List<Point>> collinearSegments = new ArrayList<>();
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

    final Point[] points = Arrays.copyOf(tpoints, tpoints.length);
    final int N = points.length;

    for (int i = 0; i < N; i++) {
      final Point[] dpoints = Arrays.copyOf(points, N);
      Arrays.sort(dpoints, dpoints[i].slopeOrder());

      int count = 0;
      int current = 0;
      final Point pivot = dpoints[current];

      double slope1 = pivot.slopeTo(dpoints[1]);
      for (int j = 2; j < N; j++) {
        double slope2 = pivot.slopeTo(dpoints[j]);

        if (slope1 != slope2) {
          if (count >= 2) {
            // collinear points
            addCollinear(dpoints, current, j - 1, slope1);
          }
          count = 1;
          current = j - 1;
          slope1 = slope2;
        } else {
          count++;
        }
      }

      if (count >= 2) {
        addCollinear(dpoints, current, N - 1, slope1);
      }
    }

  }

  private void addCollinear(Point[] pointsCopy, int from, int to, double slope) {
    final Point[] copies = Arrays.copyOfRange(pointsCopy, from + 1, to + 1);
    Arrays.sort(copies);

    final List<Point> subsegments = new ArrayList<>();
    subsegments.add(pointsCopy[0]);
    subsegments.addAll(Arrays.asList(copies));

    if (!alreadyExists(subsegments)) {
      collinearLines.add(new LineSegment(pointsCopy[0], copies[copies.length - 1]));
      collinearSegments.add(subsegments);
      size++;
    }
  }

  private boolean alreadyExists(List<Point> subsegments) {
    if (collinearSegments.isEmpty()) {
      System.out.println(String.format("New segment %s, Lines: %s", subsegments, collinearLines));
      return false;
    }
    boolean allExists = false;
    for (List<Point> sg : collinearSegments) {
      allExists = true;
      // all subsegments should pre-exist
      for (Point p : subsegments) {
        if (!sg.contains(p)) {
          allExists = false;
          break;
        }
      }
      if (allExists) {
        System.out.println(String.format("%s already exists in %s, Lines: %s", subsegments, sg, collinearLines));
        return true;
      } else {
        System.out.println(String.format("New segment %s, Lines: %s", subsegments, collinearLines));
      }
    }
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
    In in = new In("./samples/input8.txt");
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

//System.out.println(String.format("Slope between %s -> %s = %f", pivot, pointsCopy[j], slope2));