private int dip2px(float dpValue) {
    final float scale = mContext.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
}


private static boolean isLollipop() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
}
