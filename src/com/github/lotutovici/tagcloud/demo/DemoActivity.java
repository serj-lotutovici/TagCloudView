package com.github.lotutovici.tagcloud.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.github.lotutovici.tagcloud.R;
import com.github.lotutovici.tagcloud.TagCloudView;
import com.github.lotutovici.tagcloud.TagView;
import com.github.lotutovici.tagcloud.TagView.TagBundle;

public class DemoActivity extends Activity {

	private TagCloudView mTagCloudView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Step0: to get a full-screen View:
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Step1: get screen resolution:
		final Display display = getWindowManager().getDefaultDisplay();
		final int width = display.getWidth();
		final int height = display.getHeight();

		// Step2: create the required TagList:
		// notice: All tags must have unique text field
		// if not, only the first occurrence will be added and the rest will be
		// ignored
		final List<TagView> myTagList = createTags();

		// Step3: create our TagCloudview and set it as the content of our
		// MainActivity
		mTagCloudView = new TagCloudView(this, width, height, myTagList); // passing
																			// current
																			// context
		setContentView(mTagCloudView);
		mTagCloudView.requestFocus();
		mTagCloudView.setFocusableInTouchMode(true);

		// Step4: (Optional) adding a createTag and resetting the whole 3D
		// TagCloud
		// you can also add individual tags later:
		// mTagCloudView.addTag(createTag("AAA", 5, "http://www.aaa.com"));
		// .... (several other tasg can be added similarly )
		// indivual tags will be placed along with the previous tags without
		// moving
		// old ones around. Thus, after adding many individual tags, the
		// TagCloud
		// might not be evenly distributed anymore. reset() re-positions all the
		// tags:
		// mTagCloudView.reset();

		// Step5: (Optional) Replacing one of the previous tags with a createTag
		// you have to create a newTag and pass it in together
		// with the Text of the existing Tag that you want to replace
		// Tag newTag=createTag("Illinois", 9, "http://www.illinois.com");
		// in order to replace previous tag with text "Google" with this new
		// one:
		// boolean result=mTagCloudView.Replace(newTag, "google");
		// result will be true if "google" was found and replaced. else result
		// is false
	}

	private List<TagView> createTags() {
		// create the list of tags with popularity values and related url
		final List<TagView> tempList = new ArrayList<TagView>();

		tempList.addAll(decodeTags(R.array.tags_positive,
				getColor(R.color.blue)));
		tempList.addAll(decodeTags(R.array.tags_negative, getColor(R.color.red)));
		tempList.addAll(decodeTags(R.array.tags_various,
				getColor(R.color.green)));

		return tempList;
	}

	private List<TagView> decodeTags(final int resId, final int color) {
		final List<TagView> tags = new ArrayList<TagView>();

		final String[] labels = getResources().getStringArray(resId);
		for (int index = 0; index < labels.length; index++) {
			final String label = labels[index];

			tags.add(createTag(label, labels.length - index, color));
		}

		return tags;
	}

	private TagView createTag(final String text, final int popularity,
			final int color) {
		final TagBundle bundle = new TagBundle(text, popularity, color);
		return new TagView(this, bundle);
	}

	private int getColor(final int colorRes) {
		return getResources().getColor(colorRes);
	}

}
