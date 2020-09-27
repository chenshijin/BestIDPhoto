package com.csj.bestidphoto.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.csj.bestidphoto.view.CustomLinkMovementMethod;
import com.lamfire.utils.StringUtils;
import com.maoti.lib.utils.LogUtil;

public class ViewUtil {

    public static int getViewWidth(View v){
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    public static int getViewHeight(View v){
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    public static void matchAll(Context context, ImageView imageView, int pwidth, int pheight) {
        int width, height;//ImageView调整后的宽高
        //获取屏幕宽高
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);

        int sWidth = metrics.widthPixels;
        int sHeight = metrics.heightPixels;

        //获取图片宽高
        Drawable drawable = imageView.getDrawable();
        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();

        //屏幕宽高比,一定要先把其中一个转为float
        float sScale = (float) sWidth / sHeight;
        //图片宽高比
        float dScale = (float) dWidth / dHeight;
        /*
        缩放比
        如果sScale>dScale，表示在高相等的情况下，控屏幕比较宽，这时候要适应高度，缩放比就是两则的高之比，图片宽度用缩放比计算
        如果sScale<dScale，表示在高相等的情况下，图片比较宽，这时候要适应宽度，缩放比就是两则的宽之比，图片高度用缩放比计算
         */
        float scale = 1.0f;
        if (sScale > dScale) {
            scale = (float) dHeight / sHeight;
            height = sHeight;//图片高度就是屏幕高度
            width = (int) (dWidth * scale);//按照缩放比算出图片缩放后的宽度
        } else if (sScale < dScale) {
            scale = (float) dWidth / sWidth;
            width = sWidth;
            height = (int) (dHeight / scale);//这里用除
        } else {
            //最后两者刚好比例相同，其实可以不用写，刚好充满
            width = sWidth;
            height = sHeight;
        }
        //重设ImageView宽高
        imageView.getLayoutParams().width = width;
        imageView.getLayoutParams().height = height;
        //这样就获得了一个既适应屏幕又适应内部图片的ImageView，不用再纠结该给ImageView设定什么尺寸合适了
    }

    /**
     * 设置textview中部分字体的样式
     *
     * @param tv
     * @param hlightAndClickText
     * @param color
     * @param isunderline
     * @param listener
     */
    public static void setTextViewStyleAndOnClick(Context ctx, TextView tv, String hlightAndClickText, int color, int textsize, int style, boolean isunderline,
                                                  View.OnClickListener listener) {
        SpannableString ss;
        if (tv != null && hlightAndClickText != null) {
            String tvs = tv.getText().toString();
            ss = new SpannableString(tvs);
            int beginindex = tvs.indexOf(hlightAndClickText);
            if (beginindex == -1) {
                return;
            }
            int hlightAndClickTextlength = hlightAndClickText.length();
            if (listener != null) {
                ss.setSpan(new MyClickableSpan(listener),// Span接口用于实现对文本的修饰的具体内容
                        beginindex,// 修饰的起始位置
                        beginindex + hlightAndClickTextlength,// 修饰的结束位置
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(color != -1){
                ss.setSpan(new ForegroundColorSpan(color), beginindex, beginindex
                        + hlightAndClickTextlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (isunderline) {
                ss.setSpan(new UnderlineSpan(), beginindex, beginindex + hlightAndClickTextlength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // dip，如果为true，表示前面的字体大小单位为dip
            if (textsize != -1) {
                ss.setSpan(new AbsoluteSizeSpan(textsize, true), beginindex, beginindex + hlightAndClickTextlength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第二个参数boolean
            }

            // Typeface.NORMAL正常
            // Typeface.ITALIC斜体
            // Typeface.BOLD_ITALIC粗斜体
            ss.setSpan(new StyleSpan(style == -1 ? Typeface.NORMAL : style), beginindex, beginindex + hlightAndClickTextlength,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Typeface.BOLD粗体

//            //匹配表情
//            String zhengze = "\\[[^\\]]+\\]";
//            // 通过传入的正则表达式来生成一个pattern
//            Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
//            try {
//                ss = FaceConversionUtil.getInstace().dealExpression(ctx, ss, sinaPatten, 0, 2);
//            } catch (Exception e) {
//                Mylog.e("dealExpression", e.getMessage());
//            }
            tv.setText(ss);
            // 添加这一句，点击事件，拨号，http，发短信的超链接才能执行.
            if (listener != null) {
                tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
            }
        }
    }

    /**
     * 2016年2月26日
     * 描述:各个数组大小要一致
     * void
     * Administrator
     *
     * @param ctx
     * @param tv
     * @param hlightAndClickText
     * @param color
     * @param textsize
     * @param style
     * @param isunderline
     * @param listener
     */
    public static void setTextViewStyleAndOnClick(Context ctx, TextView tv, String[] hlightAndClickText, int[] color, int[] textsize, int[] style,
                                                  boolean[] isunderline, View.OnClickListener[] listener, View.OnClickListener alltextOnClickListener) {
        if (tv != null && hlightAndClickText != null) {
            SpannableString ss;
            String tvs = tv.getText().toString();
            String tvs_c = tv.getText().toString();
            ss = new SpannableString(tvs);
            int hascut = 0;
            for (int j = 0; j < hlightAndClickText.length; j++) {
                int beginindex = tvs_c.indexOf(hlightAndClickText[j]);
                if (beginindex == -1) {
                    continue;
                }
                int hlightAndClickTextlength = hlightAndClickText[j].length();

                if (listener[j] != null) {
                    ss.setSpan(new MyClickableSpan(listener[j]),// Span接口用于实现对文本的修饰的具体内容
                            beginindex + hascut,// 修饰的起始位置
                            beginindex + hlightAndClickTextlength + hascut,// 修饰的结束位置
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (color[j] != -1) {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(ctx.getResources().getColor(color[j]));
                    ss.setSpan(fcs, beginindex + hascut, beginindex + hlightAndClickTextlength + hascut, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (isunderline[j]) {
                    UnderlineSpan uls = new UnderlineSpan();
                    ss.setSpan(uls, beginindex + hascut, beginindex + hlightAndClickTextlength + hascut, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (textsize[j] != -1) {
                    ss.setSpan(new AbsoluteSizeSpan(textsize[j], true), beginindex + hascut, beginindex + hlightAndClickTextlength + hascut,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第二个参数boolean
                }

                // dip，如果为true，表示前面的字体大小单位为dip
                StyleSpan ssp = new StyleSpan(style[j] == -1 ? Typeface.NORMAL : style[j]);
                ss.setSpan(ssp, beginindex + hascut, beginindex + hlightAndClickTextlength + hascut, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Typeface.BOLD粗体
                // Typeface.NORMAL正常
                // Typeface.ITALIC斜体
                // Typeface.BOLD_ITALIC粗斜体

                hascut += beginindex + hlightAndClickTextlength;
                if (hascut < tvs.length()) {
                    tvs_c = tvs.substring(hascut);
                }
            }

            if (alltextOnClickListener != null) {
                MyClickableSpan mcs2 = new MyClickableSpan(alltextOnClickListener);
                ss.setSpan(mcs2,// Span接口用于实现对文本的修饰的具体内容
                        0,// 修饰的起始位置
                        tvs.length(),// 修饰的结束位置
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            ss.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0, tvs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置背景色透明

            tv.setText(ss);
            if (listener != null) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public static void setTextViewStyleAndOnClick(Context ctx, TextView tv, String[] hlightAndClickText, int color, int textsize, int style,
                                                  boolean isunderline, View.OnClickListener listener) {
        if (tv != null && hlightAndClickText != null) {
            SpannableString ss;
            String tvs = tv.getText().toString();
            String tvs_c = tv.getText().toString();
            if(com.lamfire.utils.StringUtils.isEmpty(tvs_c)){
                return;
            }
            ss = new SpannableString(tvs);
            int hascut = 0;
            for (final String url : hlightAndClickText) {
                int beginindex = StringUtils.isEmpty(url)?-1:tvs_c.indexOf(url);
                if (beginindex == -1) {
                    break;
                }
                int hlightAndClickTextlength = url.length();

                if (listener != null) {
                    ss.setSpan(new MyClickableSpan(listener),// Span接口用于实现对文本的修饰的具体内容
                            beginindex + hascut,// 修饰的起始位置
                            beginindex + hlightAndClickTextlength + hascut,// 修饰的结束位置
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (color != -1) {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(color);
                    ss.setSpan(fcs, beginindex + hascut, beginindex + hlightAndClickTextlength + hascut, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (isunderline) {
                    UnderlineSpan uls = new UnderlineSpan();
                    ss.setSpan(uls, beginindex + hascut, beginindex + hlightAndClickTextlength + hascut, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (textsize != -1) {
                    ss.setSpan(new AbsoluteSizeSpan(textsize, true), beginindex + hascut, beginindex + hlightAndClickTextlength + hascut,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第二个参数boolean
                }

                // dip，如果为true，表示前面的字体大小单位为dip
                StyleSpan ssp = new StyleSpan(style == -1 ? Typeface.NORMAL : style);
                ss.setSpan(ssp, beginindex + hascut, beginindex + hlightAndClickTextlength + hascut, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Typeface.BOLD粗体
                // Typeface.NORMAL正常
                // Typeface.ITALIC斜体
                // Typeface.BOLD_ITALIC粗斜体

                hascut += beginindex + hlightAndClickTextlength;
                if (hascut < tvs.length()) {
                    tvs_c = tvs.substring(hascut);
                }
            }

            tv.setText(ss);
        }
    }

    /**
     * 格式化超链接文本内容并设置点击处理
     * */
    private static CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for(final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }

    /*
     * 判断点击是否在view上
     */
    public static boolean isTouchView(View v, MotionEvent ev) {
        if (v == null) {
            return false;
        }
        boolean is = false;
        int[] l2 = {0, 0};
        v.getLocationInWindow(l2);
        LogUtil.i("ViewUtil", "点击view在屏幕上的坐标 x = " + l2[0] + "  y = " + l2[1]);
        int left2 = l2[0], top2 = l2[1], bottom2 = top2 + v.getHeight(), right2 = left2 + v.getWidth();
        LogUtil.i("ViewUtil", "点击view ev.getX() = " + ev.getX() + "  ev.getY() = " + ev.getY());
        LogUtil.i("ViewUtil", "点击view left2 = " + left2 + "  top2 = " + top2 + "  bottom2 = " + bottom2 + "  right2 = " + right2);
        if (ev.getX() > left2 && ev.getX() < right2 && ev.getY() > top2 && ev.getY() < bottom2) {
            is = true;
        }
        return is;
    }

    /**
     * 设置点击超链接对应的处理内容
     * */
    private static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {

            }
        };

        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    /**
     * 用于更改文字点击的事件和效果
     */
    private static class MyClickableSpan extends ClickableSpan {
        private View.OnClickListener mOnClickListener;

        public MyClickableSpan(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(widget);
            }

            if (widget != null && widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(widget.getContext().getResources().getColor(android.R.color.transparent));//方法重新设置文字背景为透明色
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
//            //设置文本的颜色
//            ds.setColor(Color.RED);
//            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
//            ds.setUnderlineText(false);
        }

    }

}
