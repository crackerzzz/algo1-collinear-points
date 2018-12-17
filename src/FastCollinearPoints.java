import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private final List<LineSegment> collinear = new ArrayList<>();
  private int size;

  // finds all line segments containing 4 points
  public FastCollinearPoints(Point[] points) {
    // Throw a java.lang.IllegalArgumentException if the argument to the constructor
    // is null, if any point in the array is null, or if the argument to the
    // constructor contains a repeated point.
    if (points == null) {
      throw new IllegalArgumentException();
    }
    for (Point p : points) {
      if (p == null) {
        throw new IllegalArgumentException();
      }
    }

    Arrays.sort(points);
    for (int i = 0; i < points.length - 1; i++) {
      if (points[i].compareTo(points[i + 1]) == 0) {
        throw new IllegalArgumentException();
      }
    }
    if (points.length < 4) {
      return;
    }

    final int N = points.length;
    for (int i = 0; i < N; i++) {
      final Point[] pointsCopy = Arrays.copyOf(points, N);
      Arrays.sort(pointsCopy, pointsCopy[i].slopeOrder());
//			System.out.println(Arrays.asList(pointsCopy));

      int count = 1;
      int current = 0;
      double slope1 = pointsCopy[current].slopeTo(pointsCopy[1]);
      for (int j = 2; j < N; j++) {
        double slope2 = pointsCopy[current].slopeTo(pointsCopy[j]);
        if (slope1 != slope2) {
          if (count >= 3) {
            addCollinear(pointsCopy, current, j - 1);
            // collinear points
            count = 1;
            current = j - 1;
            slope1 = slope2;
          }
        } else {
          count++;
        }
      }

      if (count >= 3) {
        addCollinear(pointsCopy, current, N - 1);
      }
    }

  }

  private void addCollinear(Point[] pointsCopy, int from, int to) {
    final Point[] copies = Arrays.copyOfRange(pointsCopy, from + 1, to + 1);
    Arrays.sort(copies);
//		System.out.println("Collinear points" + Arrays.asList(copies));
    collinear.add(new LineSegment(pointsCopy[0], copies[copies.length - 1]));

//		System.out.println(collinear);
    size++;
  }

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
    In in = new In("./samples/input9.txt");
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
