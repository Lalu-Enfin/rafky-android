package com.rafiky.connect.utils;

import static androidx.core.content.FileProvider.getUriForFile;

import static com.rafiky.connect.utils.Constants.KEY_MEETING_ID;
import static com.rafiky.connect.utils.Constants.KEY_USER_EMAIL;

import static com.rafiky.connect.utils.Constants.KEY_USER_NAME;


import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.rafiky.connect.R;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By Athul on 12-10-2019.
 */
public class AppUtils {


   /* public static InputFilter inPutFilter(Context mContext,String blockCharacterSet) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            Log.e("inPutFilter-", blockCharacterSet);
            if (source != null && !blockCharacterSet.isEmpty() && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        };
        return filter;
    }*/

    public static Dialog showProgressDialog(Context context){
        Dialog m_Dialog = new Dialog(context);
        m_Dialog.setContentView(R.layout.custom_progress_view);
        m_Dialog.setCancelable(false);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return m_Dialog;
    }

    public static boolean isUserLoggedIn(Context context) {
        SharedPreferenceData preferenceData = new SharedPreferenceData(context);
        String userName = preferenceData.getString(KEY_USER_NAME);
        Log.e("userName : ", userName);
        String emailID = preferenceData.getString(KEY_USER_EMAIL);
        Log.e("emailID : ", emailID);
        String meetingID = preferenceData.getString(KEY_MEETING_ID);
        Log.e("meetingID : ", meetingID);
        if (userName.length() > 0 && emailID.length() > 0 && meetingID.length() > 0) {
            //user logged in
            return true;
        } else {
            //user not logged in
            return false;
        }
    }

    public static boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static int getMonthIneger(String month) {
        SimpleDateFormat parser = new SimpleDateFormat("MMM", Locale.getDefault());
        Date date;
        try {
            date = parser.parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return 0;
    }


    public static File moveFile(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
            return newFile;
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }


    public static Uri getCacheImagePath(Context mContext,String fileName) {
        File path = new File(mContext.getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(mContext, mContext.getPackageName() + ".provider", image);
    }

    public static Uri getCacheChatPath(Context mContext,String fileName) {
        File path = new File(mContext.getExternalCacheDir(), "chat");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(mContext, mContext.getPackageName() + ".provider", image);
    }

    public static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static long reduceDateByYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -year);
        return cal.getTimeInMillis();
    }

    public static String getDeviceDensity(Context context){
        String deviceDensity = "";
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                deviceDensity =  0.75 + " ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                deviceDensity =  1.0 + " mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                deviceDensity =  1.5 + " hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                deviceDensity =  2.0 + " xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                deviceDensity =  3.0 + " xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                deviceDensity =  4.0 + " xxxhdpi";
                break;
            default:
                deviceDensity = "Not found";
        }
        return deviceDensity;
    }

    public static boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString().trim();
        return TextUtils.isEmpty(str);
    }


    /**
     * Method for validating the email pattern
     *
     * @param email Email address
     * @return True-valid address else return false.
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }



    public static void showDialog(final Context context, String title, String message, boolean isLogoutRequired) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_alert_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        // set the custom dialog components - text and button
        TextView heading = dialog.findViewById(R.id.titleTextView);
        TextView errors = dialog.findViewById(R.id.errorTextView);
        heading.setText(title);
        if(message.trim().equalsIgnoreCase("")){
            message= context.getResources().getString(R.string.something_went_wrong_text_p);
        }
        errors.setText(message);
        Button cancelButton = dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void showDialogCloseButton(final Context context, String title, String message, boolean isLogoutRequired) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_alert_dialog_cross);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        // set the custom dialog components - text and button
        TextView heading = dialog.findViewById(R.id.titleTextView);
        TextView errors = dialog.findViewById(R.id.errorTextView);
        ImageButton closeButton = dialog.findViewById(R.id.ib_cloase_button);
        heading.setText(title);
        if(message.trim().equalsIgnoreCase("")){
            message= context.getResources().getString(R.string.something_went_wrong_text_p);
        }
        errors.setText(message);
        Button cancelButton = dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(v -> {

                dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {

                dialog.dismiss();
        });
        dialog.show();
    }


    public static void showDialogCustom(final Context context, String title, String message,boolean calcelable, boolean isLogoutRequired) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_alert_dialog_custom);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(calcelable);
        // set the custom dialog components - text and button
        TextView heading = dialog.findViewById(R.id.titleTextView);
        TextView errors = dialog.findViewById(R.id.errorTextView);
        ImageButton closeButton = dialog.findViewById(R.id.ib_cloase_button);
        heading.setText(title);
        if(message.trim().equalsIgnoreCase("")){
            message= context.getResources().getString(R.string.something_went_wrong_text_p);
        }
        errors.setText(message);
        Button cancelButton = dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(v -> {

                dialog.dismiss();
        });

        closeButton.setOnClickListener(v -> {

                dialog.dismiss();
        });
        dialog.show();
    }



    public static String getAssetJsonData(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("myjson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        Log.e("data", json);
        return json;

    }

    public static String getFormatedDateTime(String dateStr, String strReadFormat, String strWriteFormat, String strTimeWriteFormate) {
        String formateDate="";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(dateStr);
            formateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(myDate);


        } catch (ParseException e) {
            Log.e("Error Date at Whisp", e.getMessage());

        }

        String formattedDate = formateDate;
        String formattedDateTime = formateDate;

        DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.getDefault());
        DateFormat writeTimesFormat = new SimpleDateFormat(strTimeWriteFormate, Locale.getDefault());

        Date date = null;
        Date date2 = null;

        try {
            date = readFormat.parse(formateDate);
            date2 = readFormat.parse(formateDate);
        } catch (ParseException e) {
            Log.e("Error : ", e.getMessage());
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
            formattedDateTime = writeTimesFormat.format(date2);
        }

        return formattedDate + " " + formattedDateTime;
    }

    public static String getFormatedDateTime(String dateStr, String strReadFormat, String strWriteFormat) {

        String formattedDate = dateStr;
        DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.getDefault());

        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
            Log.e("Error : ",e.getMessage());
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }


    public static String changeImageFileExtentionToWebp(String path){
        try {
            Pattern ext = Pattern.compile("(?<=.)\\.[^.]+$");
            String fileNameWithOutExt = ext.matcher(path).replaceAll("");
            return fileNameWithOutExt+".webp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static File createRandomFileInPhotoDirectory(Context mContext) throws IOException {
        String imageFileName = "IMG_" + System.currentTimeMillis() + "_";
        File storageDir =
                mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static String getFileExtensionFromFileName(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openAppUrl(Context context, String packageName) {
        // you can also use BuildConfig.APPLICATION_ID
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + packageName));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
            context.startActivity(webIntent);
        }
    }

    public static boolean checkMobileNumberFromString(String textValue){
        Pattern pattern = Pattern.compile("(\\+\\d{1,3}[- ]?)?[\\d ]{10}");
        Matcher matcher = pattern.matcher(textValue);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            return  true ;
        }else {
            return false ;
        }
    }


    ////////////////////compress image file//////////////////////////

    public static String compressImages(String imageUri, Context mContext) {

        String filePath = getRealPathFromURI(imageUri,mContext);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 1080.0f;
        float maxWidth = 800.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSizes(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {

//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename(scaledBitmap,mContext);
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static String getFilename(Bitmap myBitmap, Context mContext) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imgDirectory = new File(mContext.getFilesDir()+"/"+"weedeo");
        // have the object build the directory structure, if needed.
        if (!imgDirectory.exists()) {
            imgDirectory.mkdirs();
        }
        try {
            File f = new File(imgDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String getRealPathFromURI(String contentURI, Context mContext) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, new String[]{
                OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE
        }, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSizes(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    public static String saveImage(Bitmap myBitmap, Context mContext) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imgDirectory = new File(String.valueOf(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        // have the object build the directory structure, if needed.
        if (!imgDirectory.exists()) {
            imgDirectory.mkdirs();
        }
        try {
            File f = new File(imgDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
            // return getUriForFile(mContext, mContext.getPackageName() + ".provider", f);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
    /////////////////////////////////////////////////////

    //date change 2022-02-28T08:30:00.000Z
    public static Date getDateFormat(String sampleDate){
        Date formatedDate = null;
        try {
            SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            formatedDate = format.parse(sampleDate);
            Log.e("getDateFormat:", "" + formatedDate);
        }catch (Exception e){
            e.printStackTrace();
        }
       return formatedDate ;
    }
    //date getCurrentDate 2022-02-28T08:30:00.000Z
    public static Date getCurrentDate(){
        Date curentDate = null;
        try {
            SimpleDateFormat current = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String formattedDate = current.format(new Date());
            curentDate = current.parse(formattedDate);
            Log.e("curentDate", "" + curentDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return curentDate ;
    }

}
