package cn.jishiyu11.xjsjd.activity.FaceIDCard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.megvii.idcardquality.bean.IDCardAttr;

import cn.jishiyu11.xjsjd.R;

/**
 * Created by binghezhouke on 15-8-12.
 */
public class ResultNewActivity extends Activity {
	private ImageView mIDCardImageView;
	private ImageView mPortraitImageView;
	private TextView mIDCardSize;
	private TextView mPortraitSize;
	IDCardAttr.IDCardSide mIDCardSide;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resutl);

		mIDCardSide = getIntent().getIntExtra("side", 0) == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
				: IDCardAttr.IDCardSide.IDCARD_SIDE_BACK;
		init();
	}

	void init() {
		mIDCardImageView = (ImageView) findViewById(R.id.result_idcard_image);
		mPortraitImageView = (ImageView) findViewById(R.id.result_portrait_image);

		mIDCardSize = (TextView) findViewById(R.id.result_idcard_size);
		mPortraitSize = (TextView) findViewById(R.id.result_portrait_size);
		{
			byte[] idcardImgData = getIntent().getByteArrayExtra("idcardImg");
			Bitmap idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0,
					idcardImgData.length);
			mIDCardImageView.setImageBitmap(idcardBmp);
			mIDCardSize.setText(idcardBmp.getWidth() + "_"
					+ idcardBmp.getHeight());
		}
		if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
			byte[] portraitImgData = getIntent().getByteArrayExtra(
					"portraitImg");
			Bitmap img = BitmapFactory.decodeByteArray(portraitImgData, 0,
					portraitImgData.length);
			mPortraitImageView.setImageBitmap(img);
			mPortraitSize.setText(img.getWidth() + "_" + img.getHeight());
		}
	}
}