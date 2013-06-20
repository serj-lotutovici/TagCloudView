package com.github.lotutovici.tagcloud;

public class Tag implements Comparable<Tag> {

	private static final int DEFAULT_POPULARITY = 1;

	/**
	 * The tag text and tag url's
	 */
	private String text, url;

	/**
	 * The tag importance/popularity
	 */
	private int popularity;

	/**
	 * The tag text size
	 */
	private int textSize;

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

	private float alpha;

	private int colorRes;

	/**
	 * The Tag's parameters/settings
	 */
	private int paramNo;

	private boolean isModified = false;

	/**
	 * Construct's a Tag object filing it with default parameters
	 */
	public Tag() {
		this("", 0f, 0f, 0f, 1.0f, 0, "");
	}

	/**
	 * Constructs a tag object with text and priority, other values are set to
	 * default values
	 * 
	 * @param text
	 *            The Tag's text
	 * @param popularity
	 *            The Tag's popularity
	 */
	public Tag(final String text, final int popularity) {
		this(text, 0f, 0f, 0f, 1.0f, popularity, "");
	}

	public Tag(final String text, final int popularity, final int colorRes) {
		this(text, 0f, 0f, 0f, 1.0f, popularity, "");
		this.colorRes = colorRes;
	}

	/**
	 * Constructs a tag object with text, priority and link URL, other values
	 * are set to default values
	 * 
	 * @param text
	 *            The Tag's text
	 * @param popularity
	 *            The Tag's popularity
	 * @param url
	 *            The Tag's link url
	 */
	public Tag(final String text, final int popularity, final String url) {
		this(text, 0f, 0f, 0f, 1.0f, popularity, url);
	}

	/**
	 * Constructs a Tag object with text and Tag center coordinates, other
	 * values are set to default values
	 * 
	 * @param text
	 *            The Tag's text
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 */
	public Tag(final String text, final float centerX, final float centerY,
			final float centerZ) {
		this(text, centerX, centerY, centerZ, 1.0f, DEFAULT_POPULARITY, "");
	}

	/**
	 * Constructs a Tag object with text, Tag center coordinates and scale flag,
	 * other values are set to default values
	 * 
	 * @param text
	 *            The Tag's text
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param scale
	 *            The Tag's scale flag
	 */
	public Tag(final String text, final float centerX, final float centerY,
			final float centerZ, final float scale) {
		this(text, centerX, centerY, centerZ, scale, DEFAULT_POPULARITY, "");
	}

	/**
	 * Constructs a Tag object.
	 * 
	 * @param text
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param scale
	 * @param popularity
	 * @param url
	 */
	public Tag(final String text, final float centerX, final float centerY,
			final float centerZ, final float scale, final int popularity,
			final String url) {
		this.text = text;
		this.centerX = centerX;
		this.centerY = centerX;
		this.centerZ = centerZ;

		loc2DX = 0;
		loc2DY = 0;

		alpha = 1.0f;

		this.scale = scale;
		this.popularity = popularity;
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Tag another) {
		int diff = 0;

		if (another != null) {
			diff = (int) (another.centerZ - centerZ);
		}

		return diff;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(final float scale) {
		if (this.scale != scale) {
			isModified = true;
			this.scale = scale;
		}
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	// public float getColorR() {
	// return colorR;
	// }
	//
	// public void setColorR(final float colorR) {
	// if (this.colorR != colorR) {
	// isModified = true;
	// this.colorR = colorR;
	// }
	// }
	//
	// public float getColorG() {
	// return colorG;
	// }
	//
	// public void setColorG(final float colorG) {
	// if (this.colorG != colorG) {
	// isModified = true;
	// this.colorG = colorG;
	// }
	// }
	//
	// public float getColorB() {
	// return colorB;
	// }
	//
	// public void setColorB(final float colorB) {
	// if (this.colorB != colorB) {
	// isModified = true;
	// this.colorB = colorB;
	// }
	// }

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(final float alpha) {
		if (this.alpha != alpha) {
			isModified = true;
			this.alpha = alpha;
		}
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(final int popularity) {
		this.popularity = popularity;
	}

	public float getCenterX() {
		return centerX;
	}

	public void setCenterX(final float centerX) {
		if (this.centerX != centerX) {
			isModified = true;
			this.centerX = centerX;
		}
	}

	public float getCenterY() {
		return centerY;
	}

	public void setCenterY(final float centerY) {
		if (this.centerY != centerY) {
			isModified = true;
			this.centerY = centerY;
		}
	}

	public float getCenterZ() {
		return centerZ;
	}

	public void setCenterZ(final float centerZ) {
		if (this.centerZ != centerZ) {
			isModified = true;
			this.centerZ = centerZ;
		}
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(final int textSize) {
		this.textSize = textSize;
	}

	public float getLoc2DX() {
		return loc2DX;
	}

	public void setLoc2DX(final float loc2dx) {
		if (loc2DX != loc2dx) {
			isModified = true;
			loc2DX = loc2dx;
		}
	}

	public float getLoc2DY() {
		return loc2DY;
	}

	public void setLoc2DY(final float loc2dy) {
		if (loc2DY != loc2dy) {
			isModified = true;
			loc2DY = loc2dy;
		}
	}

	public int getParamNo() {
		return paramNo;
	}

	public void setParamNo(final int paramNo) {
		this.paramNo = paramNo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public boolean isModified() {
		return isModified;
	}

	public void resetModified() {
		isModified = false;
	}

	public int getColorRes() {
		return colorRes;
	}

	public void setColorRes(final int clorRes) {
		colorRes = clorRes;
	}
}
