package com.github.lotutovici.tagcloud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Build;

public class TagCloud {

	private static final int DEFAULT_RADIUS = 3;

	private static final int TEXT_SIZE_MAX = 30, TEXT_SIZE_MIN = 4;

	private final List<TagView> tagCloud;
	private int radius;
	private final int textSizeMax, textSizeMin;
	private float sin_mAngleX, cos_mAngleX, sin_mAngleY, cos_mAngleY,
			sin_mAngleZ, cos_mAngleZ;
	private final float mAngleZ = 0;
	private float mAngleX = 0;
	private float mAngleY = 0;
	private int size = 0;
	private int smallest, largest; // used to find spectrum for tag colors

	/**
	 * Default is to distribute tags evenly on the Cloud
	 */
	private boolean distrEven = true;

	/**
	 * 
	 */
	public TagCloud() {
		this(new ArrayList<TagView>());
	}

	/**
	 * 
	 * @param tags
	 */
	public TagCloud(final List<TagView> tags) {
		this(tags, DEFAULT_RADIUS);
	}

	/**
	 * Constructor just copies the existing tags in its List
	 * 
	 * @param tags
	 * @param radius
	 */
	public TagCloud(final List<TagView> tags, final int radius) {
		this(tags, radius, TEXT_SIZE_MIN, TEXT_SIZE_MAX);
	}

	/**
	 * 
	 * @param tags
	 * @param radius
	 * @param tagColor1
	 * @param tagColor2
	 * @param textSizeMin
	 * @param textSizeMax
	 */
	public TagCloud(final List<TagView> tags, final int radius,
			final int textSizeMin, final int textSizeMax) {
		tagCloud = new ArrayList<TagView>(tags); // Java does the initialization
													// and
													// deep copying
		this.radius = radius;
		this.textSizeMax = textSizeMax;
		this.textSizeMin = textSizeMin;
	}

	// create method calculates the correct initial location of each tag
	public void create(final boolean distrEven, final float lcenterX,
			final float lcenterY, final int shiftLeft) {
		this.distrEven = distrEven;
		// calculate and set the location of each Tag
		positionAll(distrEven);
		sineCosine(mAngleX, mAngleY, mAngleZ);
		updateAll(lcenterX, lcenterY, shiftLeft);
		// Now, let's calculate and set the color for each tag:
		// first loop through all tags to find the smallest and largest
		// populariteies
		// largest popularity gets tcolor2, smallest gets tcolor1, the rest in
		// between
		smallest = 9999;
		largest = 0;

		for (final TagView tag : tagCloud) {
			final int popularity = tag.getPopularity();
			largest = Math.max(largest, popularity);
			smallest = Math.min(smallest, popularity);
		}

		// figuring out and assigning the colors/ textsiz

		for (final TagView tag : tagCloud) {
			final int popularity = tag.getPopularity();

			final float percentage = (smallest == largest) ? 1.0f
					: ((float) popularity - smallest)
							/ ((float) largest - smallest);

			final int tempTextSize = getTextSizeGradient(percentage);

			tag.setTextSize((int) (tempTextSize * tag.getScale()));

			tag.getParams().setMargins(
					(int) ((lcenterX - shiftLeft) + tag.getLoc2DX()),
					(int) (lcenterY + tag.getLoc2DY()), 0, 0);
		}

		size = tagCloud.size();
	}

	public void reset(final float lcenterX, final float lcenterY,
			final int shiftLeft) {
		create(distrEven, lcenterX, lcenterY, shiftLeft);
	}

	// updates the transparency/scale of all elements
	public void update(final float lcenterX, final float lcenterY,
			final int shiftLeft) {
		// if mAngleX and mAngleY under threshold, skip motion calculations for
		// performance
		if ((Math.abs(mAngleX) > .1) || (Math.abs(mAngleY) > .1)) {
			sineCosine(mAngleX, mAngleY, mAngleZ);
			updateAll(lcenterX, lcenterY, shiftLeft);
		}
	}

	// if a single tag needed to be added
	public void add(final TagView newTag) {
		final int j = newTag.getPopularity();
		final float percentage = (smallest == largest) ? 1.0f
				: ((float) j - smallest) / ((float) largest - smallest);

		final int tempTextSize = getTextSizeGradient(percentage);

		newTag.setTextSize((int) (tempTextSize * newTag.getScale()));
		position(distrEven, newTag);
		// now add the new tag to the tagCloud
		tagCloud.add(newTag);
		size = tagCloud.size();
		// updateAll(); TODO refactor
	}

	// to replace an existing tag with a new one
	// it returns the location of the replacement, if not found=> returns -1
	public int replace(TagView newTag, final String oldTagText) {
		int result = -1;
		// let's go over all elements of tagCloud list and see if the oldTagText
		// exists:
		for (final TagView tag : tagCloud) {
			if (oldTagText.equalsIgnoreCase(tag.getText().toString())) {
				result = tagCloud.indexOf(tag);

				// tag.setPopularity(newTag.getPopularity());
				tag.setText(newTag.getText());
				final int popularity = newTag.getPopularity();
				final float percentage = (smallest == largest) ? 1.0f
						: ((float) popularity - smallest)
								/ ((float) largest - smallest);

				final int tempTextSize = getTextSizeGradient(percentage);
				// tag.setColorR(tempColor[0]);
				// tag.setColorG(tempColor[1]);
				// tag.setColorB(tempColor[2]);
				tag.setTextSize(tempTextSize);
				newTag = tag;
				break;
			}
		}

		return result;
	}

	private void position(final boolean distrEven, final TagView newTag) {
		double phi = 0;
		double theta = 0;
		// when adding a new tag, just place it at some random location
		// this is in fact why adding too many elements make TagCloud ugly
		// after many add, do one reset to rearrange all tags
		phi = Math.random() * (Math.PI);
		theta = Math.random() * (2 * Math.PI);
		// coordinate conversion:
		newTag.setCenterX((int) (radius * Math.cos(theta) * Math.sin(phi)));
		newTag.setCenterY((int) (radius * Math.sin(theta) * Math.sin(phi)));
		newTag.setCenterZ((int) (radius * Math.cos(phi)));
	}

	private void positionAll(final boolean distrEven) {
		double phi = 0;
		double theta = 0;
		final int max = tagCloud.size();
		// distribute: (disrtEven is used to specify whether distribute random
		// or even
		for (int i = 1; i < (max + 1); i++) {
			if (distrEven) {
				phi = Math.acos(-1.0 + (((2.0 * i) - 1.0) / max));
				theta = Math.sqrt(max * Math.PI) * phi;
			} else {
				phi = Math.random() * (Math.PI);
				theta = Math.random() * (2 * Math.PI);
			}

			// coordinate conversion:
			tagCloud.get(i - 1).setCenterX(
					(int) ((radius * Math.cos(theta) * Math.sin(phi))));
			tagCloud.get(i - 1).setCenterY(
					(int) (radius * Math.sin(theta) * Math.sin(phi)));
			tagCloud.get(i - 1).setCenterZ((int) (radius * Math.cos(phi)));
		}
	}

	private void updateAll(final float lcenterX, final float lcenterY,
			final int shiftLeft) {

		// update transparency/scale for all tags:
		for (final TagView tag : tagCloud) {
			// There exists two options for this part:
			// multiply positions by a x-rotation matrix
			final float rx1 = (tag.getCenterX());
			final float ry1 = ((tag.getCenterY()) * cos_mAngleX)
					+ (tag.getCenterZ() * -sin_mAngleX);
			final float rz1 = ((tag.getCenterY()) * sin_mAngleX)
					+ (tag.getCenterZ() * cos_mAngleX);
			// multiply new positions by a y-rotation matrix
			final float rx2 = (rx1 * cos_mAngleY) + (rz1 * sin_mAngleY);
			final float ry2 = ry1;
			final float rz2 = (rx1 * -sin_mAngleY) + (rz1 * cos_mAngleY);
			// multiply new positions by a z-rotation matrix
			final float rx3 = (rx2 * cos_mAngleZ) + (ry2 * -sin_mAngleZ);
			final float ry3 = (rx2 * sin_mAngleZ) + (ry2 * cos_mAngleZ);
			final float rz3 = rz2;
			// set arrays to new positions
			tag.setCenterX(rx3);
			tag.setCenterY(ry3);
			tag.setCenterZ(rz3);

			// add perspective
			final int diameter = 2 * radius;
			final float per = diameter / (diameter + rz3);
			// let's set position, scale, alpha for the tag;
			tag.setLoc2DX((int) (rx3 * per));
			tag.setLoc2DY((int) (ry3 * per));
			tag.setScale(per);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tag.setAlpha(per / 2);
			}

			tag.getParams().setMargins(
					(int) ((lcenterX - shiftLeft) + tag.getLoc2DX()),
					(int) (lcenterY + tag.getLoc2DY()), 0, 0);

			final int popularity = tag.getPopularity();
			final float percentage = (smallest == largest) ? 1.0f
					: ((float) popularity - smallest)
							/ ((float) largest - smallest);

			final int tempTextSize = getTextSizeGradient(percentage);

			tag.setTextSize((int) (tempTextSize * tag.getScale()));

			tag.bringToFront();
		}
		depthSort();
	}

	// /now let's sort all tags in the tagCloud based on their z coordinate
	// this way, when they are finally drawn, upper tags will be drawn on top of
	// lower tags
	private void depthSort() {
		Collections.sort(tagCloud);
	}

	// private float[] getColorFromGradient(final float perc) {
	// final float[] tempRGB = new float[4];
	// tempRGB[0] = (perc * (tagColor1[0])) + ((1 - perc) * (tagColor2[0]));
	// tempRGB[1] = (perc * (tagColor1[1])) + ((1 - perc) * (tagColor2[1]));
	// tempRGB[2] = (perc * (tagColor1[2])) + ((1 - perc) * (tagColor2[2]));
	// tempRGB[3] = 1;
	// return tempRGB;
	// }

	private int getTextSizeGradient(final float perc) {
		int size;
		size = (int) ((perc * textSizeMax) + ((1 - perc) * textSizeMin));
		return size;
	}

	private void sineCosine(final float mAngleX, final float mAngleY,
			final float mAngleZ) {
		final double degToRad = (Math.PI / 180);
		sin_mAngleX = (float) Math.sin(mAngleX * degToRad);
		cos_mAngleX = (float) Math.cos(mAngleX * degToRad);
		sin_mAngleY = (float) Math.sin(mAngleY * degToRad);
		cos_mAngleY = (float) Math.cos(mAngleY * degToRad);
		sin_mAngleZ = (float) Math.sin(mAngleZ * degToRad);
		cos_mAngleZ = (float) Math.cos(mAngleZ * degToRad);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(final int radius) {
		this.radius = radius;
	}

	// public float[] getTagColor1() {
	// return tagColor1;
	// }
	//
	// public void setTagColor1(final float[] tagColor) {
	// tagColor1 = tagColor;
	// }
	//
	// public float[] getTagColor2() {
	// return tagColor2;
	// }
	//
	// public void setTagColor2(final float[] tagColor2) {
	// this.tagColor2 = tagColor2;
	// }

	public float getRvalue(final float[] color) {
		if (color.length > 0) {
			return color[0];
		} else {
			return 0;
		}
	}

	public float getGvalue(final float[] color) {
		if (color.length > 0) {
			return color[1];
		} else {
			return 0;
		}
	}

	public float getBvalue(final float[] color) {
		if (color.length > 0) {
			return color[2];
		} else {
			return 0;
		}
	}

	public float getAlphaValue(final float[] color) {
		if (color.length >= 4) {
			return color[3];
		} else {
			return 0;
		}
	}

	public float getAngleX() {
		return mAngleX;
	}

	public void setAngleX(final float mAngleX) {
		this.mAngleX = mAngleX;
	}

	public float getAngleY() {
		return mAngleY;
	}

	public void setAngleY(final float mAngleY) {
		this.mAngleY = mAngleY;
	}

	public int getSize() {
		return size;
	}

	public List<TagView> getTags() {
		return tagCloud;
	}
}
