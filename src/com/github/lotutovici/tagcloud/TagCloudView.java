package com.github.lotutovici.tagcloud;

/**
 * Komodo Lab: Tagin! Project: 3D Tag Cloud
 * Google Summer of Code 2011
 * @authors Reza Shiftehfar, Sara Khosravinasr and Jorge Silva
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class TagCloudView extends RelativeLayout {

	public TagCloudView(final Context mContext, final int width,
			final int height, final List<TagView> tagList) {
		this(mContext, width, height, tagList, 6, 30, 5); // default for min/max
															// text size
	}

	public TagCloudView(final Context mContext, final int width,
			final int height, final List<TagView> tagList,
			final int textSizeMin, final int textSizeMax, final int scrollSpeed) {

		super(mContext);
		this.mContext = mContext;
		setBackgroundColor(Color.WHITE);

		tspeed = scrollSpeed;

		// set the center of the sphere on center of our screen:
		centerX = width / 2;
		centerY = height / 2;
		radius = Math.min(centerX * 0.85f, centerY * 0.85f); // use 95% of
																// screen
		// since we set tag margins from left of screen, we shift the whole tags
		// to left so that
		// it looks more realistic and symmetric relative to center of screen in
		// X direction
		shiftLeft = (int) (Math.min(centerX * 0.15f, centerY * 0.15f));

		// initialize the TagCloud from a list of tags
		// Filter() func. screens tagList and ignores Tags with same text (Case
		// Insensitive)
		mTagCloud = new TagCloud(filter(tagList), (int) radius, textSizeMin,
				textSizeMax);
		// final float[] tempColor1 = { 0.77254f, 0.85098f, 0.94509f, 1 }; //
		// rgb
		// // Alpha
		// // {1f,0f,0f,1} red {0.3882f,0.21568f,0.0f,1} orange
		// // {0.9412f,0.7686f,0.2f,1} light orange
		// final float[] tempColor2 = { 0.75294f, 0f, 0f, 1 }; // rgb Alpha
		// // {0f,0f,1f,1} blue
		// // {0.1294f,0.1294f,0.1294f,1}
		// // grey
		// // {0.9412f,0.7686f,0.2f,1}
		// // light orange
		// mTagCloud.setTagColor1(tempColor1);// higher color
		// mTagCloud.setTagColor2(tempColor2);// lower color

		// Now Draw the 3D objects: for all the tags in the TagCloud

		for (final TagView tempTag : mTagCloud.getTags()) {

			// tempTag.setParamNo(i); // store the parameter No. related to this
			// // tag

			final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.setMargins(
					(int) ((centerX - shiftLeft) + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			tempTag.setParams(params);

			tempTag.setSingleLine(true);

			addView(tempTag);

			// mTextView.get(i).setOnTouchListener(new OnThouch());
			// mTextView.get(i).setOnClickListener(
			// OnTagClickListener(tempTag.getUrl()));
		}

		mTagCloud.setRadius((int) radius);
		mTagCloud.create(true, centerX, centerY, shiftLeft); // to put each Tag
																// at its
																// correct
																// initial
		// location

		// update the transparency/scale of tags
		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update(centerX, centerX, shiftLeft);
	}

	public void addTag(final TagView newTag) {

		// newTag.setParamNo(i);

		mTagCloud.add(newTag);

		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.setMargins((int) ((centerX - shiftLeft) + newTag.getLoc2DX()),
				(int) (centerY + newTag.getLoc2DY()), 0, 0);
		newTag.setParams(params);

		newTag.setSingleLine(true);
		// final int mergedColor = Color.argb((int) (newTag.getAlpha() * 255),
		// (int) (newTag.getColorR() * 255),
		// (int) (newTag.getColorG() * 255),
		// (int) (newTag.getColorB() * 255));
		// mTextViews.get(i).setTextColor(mergedColor);
		newTag.setTextSize((int) (newTag.getTextSize() * newTag.getScale()));

		addView(newTag);
		// mTextViews.get(i).setOnClickListener(
		// onTagClickListener(newTag.getUrl()));
	}

	public int replace(final TagView newTag, final String oldTagText) {
		return mTagCloud.replace(newTag, oldTagText);
	}

	public void reset() {
		mTagCloud.reset(centerX, centerY, shiftLeft);
	}

	@Override
	public boolean onTrackballEvent(final MotionEvent e) {
		final float x = e.getX();
		final float y = e.getY();

		mAngleX = (y) * tspeed * TRACKBALL_SCALE_FACTOR;
		mAngleY = (-x) * tspeed * TRACKBALL_SCALE_FACTOR;

		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update(centerX, centerY, shiftLeft);

		return false;
	}

	private float startX = 0;
	private float startY = 0;

	@Override
	public boolean onTouchEvent(final MotionEvent e) {
		final float x = e.getX();
		final float y = e.getY();

		if (MotionEvent.ACTION_DOWN == e.getAction()) {
			startX = e.getX();
			startY = e.getY();
		} else if (MotionEvent.ACTION_MOVE == e.getAction()) {
			// rotate elements depending on how far the selection point is from
			// center of cloud
			final float dx = x - startX;
			final float dy = y - startY;

			mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
			mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;

			Log.i("TAG_CLOUD", "DX: " + dx + " DY: " + dy + " Radius: "
					+ radius);

			mTagCloud.setAngleX(mAngleX);
			mTagCloud.setAngleY(mAngleY);
			mTagCloud.update(centerX, centerY, shiftLeft);

			// /*
			// * case MotionEvent.ACTION_UP: //now it is clicked!!!! dx = x -
			// centerX;
			// * dy = y - centerY; break;
			// */
			// default: {
			// }
		}

		return true;
	}

	public String urlMaker(final String url) {
		if ((url.substring(0, 7).equalsIgnoreCase("http://"))
				|| (url.substring(0, 8).equalsIgnoreCase("https://"))) {
			return url;
		} else {
			return "http://" + url;
		}
	}

	// the filter function makes sure that there all elements are having unique
	// Text field:
	public List<TagView> filter(final List<TagView> tagList) {
		// current implementation is O(n^2) but since the number of tags are not
		// that many,
		// it is acceptable.
		final List<TagView> tempTagList = new ArrayList<TagView>();

		for (final TagView tag : tagList) {
			boolean found = false;

			for (final TagView tag2 : tempTagList) {
				if (tag2.getText().toString()
						.equalsIgnoreCase(tag.getText().toString())) {
					found = true;
					break;
				}
			}

			if (!found) {
				tempTagList.add(tag);
			}
		}

		return tempTagList;
	}

	// for handling the click on the tags
	// onclick open the tag url in a new window. Back button will bring you back
	// to TagCloud
	public View.OnClickListener onTagClickListener(final String url) {
		return new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				// we now have url from main code
				final Uri uri = Uri.parse(urlMaker(url));
				// just open a new intent and set the content to search for the
				// url
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		};
	}

	private final float TOUCH_SCALE_FACTOR = .8f;
	private final float TRACKBALL_SCALE_FACTOR = 10;
	private final float tspeed;
	private final TagCloud mTagCloud;
	private float mAngleX = 0;
	private float mAngleY = 0;
	private final float centerX, centerY;
	private final float radius;
	private final Context mContext;
	private final int shiftLeft;

	public class OnThouch implements OnTouchListener {

		@Override
		public boolean onTouch(final View v, final MotionEvent event) {
			return onTouchEvent(event);
		}

	}
}
