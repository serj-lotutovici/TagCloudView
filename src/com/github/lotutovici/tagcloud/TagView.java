package com.github.lotutovici.tagcloud;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagView extends TextView implements Comparable<TagView> {

	/**
	 * The Default bundle, to avoid lint warnings
	 */
	private static final TagBundle DEFAULT_BUNDLE = new TagBundle("Tag", 1,
			Color.BLACK);

	/**
	 * The tag importance/popularity
	 */
	private final int popularity;

	/**
	 * The Tag's center coordinates
	 */
	private float centerX, centerY, centerZ;

	/**
	 * Location coordinates of the tag
	 */
	private float loc2DX, loc2DY;

	/**
	 * The Tag's scale
	 */
	private float scale;

	private RelativeLayout.LayoutParams params;

	/**
	 * Constructs a {@link TagView} object. <br>
	 * <br>
	 * <b>Note: </b> This method is deprecated, use
	 * {@link TagView#TagView(Context, TagBundle)} instead.
	 * 
	 * @param context
	 */
	@Deprecated
	public TagView(final Context context) {
		this(context, DEFAULT_BUNDLE);
	}

	/**
	 * Constructs the {@link TagView} object with specified parameters
	 * 
	 * @param context
	 *            Context for the view inflation
	 * @param bundle
	 *            The {@link TagBundle} with specified parameters
	 */
	public TagView(final Context context, final TagBundle bundle) {
		super(context);

		/*
		 * Set the TextView parameters
		 */
		this.setText(bundle.text);
		this.setTextColor(bundle.color);

		/*
		 * Set the TagView popularity/priority
		 */
		popularity = bundle.popularity;

		/*
		 * Initialize the tag coordinates
		 */
		init(0f, 0f, 0f, 1.0f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final TagView another) {
		int diff = -1;

		if (another != null) {
			diff = another.getText().toString().compareTo(getText().toString());
		}

		return diff;
	}

	private void init(final float centerX, final float centerY,
			final float centerZ, final float scale) {

		/*
		 * Set the Tag's center position
		 */
		this.centerX = centerX;
		this.centerY = centerX;
		this.centerZ = centerZ;

		/*
		 * Set 2D coordinates
		 */
		loc2DX = 0;
		loc2DY = 0;

		/*
		 * Set scale
		 */
		this.scale = scale;

	}

	public int getPopularity() {
		return popularity;
	}

	public float getCenterX() {
		return centerX;
	}

	public void setCenterX(final float centerX) {
		this.centerX = centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public void setCenterY(final float centerY) {
		this.centerY = centerY;
	}

	public float getCenterZ() {
		return centerZ;
	}

	public void setCenterZ(final float centerZ) {
		this.centerZ = centerZ;
	}

	public float getLoc2DX() {
		return loc2DX;
	}

	public void setLoc2DX(final float loc2dx) {
		loc2DX = loc2dx;
	}

	public float getLoc2DY() {
		return loc2DY;
	}

	public void setLoc2DY(final float loc2dy) {
		loc2DY = loc2dy;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(final float scale) {
		this.scale = scale;
	}

	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	public void setParams(final RelativeLayout.LayoutParams params) {
		this.params = params;
		setLayoutParams(params);
	}

	/**
	 * {@link TagView} parameters bundle. This class creates an immutable object
	 * for the Tag Cloud View.
	 * 
	 * @author Yami
	 * 
	 */
	public static class TagBundle {
		private final String text;
		private final int popularity;
		private final int color;

		/**
		 * Constructs a the parameters bundle
		 * 
		 * @param text
		 *            The Tag's text
		 * @param popularity
		 *            The Tag's popularity
		 * @param color
		 *            The Tags text color
		 */
		public TagBundle(final String text, final int popularity,
				final int color) {
			super();
			this.text = text;
			this.popularity = popularity;
			this.color = color;
		}
	}
}
