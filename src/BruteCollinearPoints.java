import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private final Point[] points;
	private List<LineSegment> collinear;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
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
		Point p = points[0];
		for (int i = 1; i < points.length; i++) {
			if (p.compareTo(points[i]) == 0) {
				throw new IllegalArgumentException();
			}
		}

		this.points = points;

		final int N = points.length;

		final Point p = points[0];
		final Point q = points[1];
		final Point r = points[2];
		final Point s = points[3];

		double pqSlope = p.slopeTo(q);
		double prSlope = p.slopeTo(r);
		double psSlope = p.slopeTo(s);

		if (pqSlope == prSlope && prSlope == psSlope) {
			collinear.add(new LineSegment(p, s));
		}
	}

	// the number of line segments
	public int numberOfSegments() {
		return collinear.size();
	}

	// the line segments
	public LineSegment[] segments() {
		return collinear.toArray(new LineSegment[0]);
	}

	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
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
