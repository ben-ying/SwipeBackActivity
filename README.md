SwipeBackActivity
=====
SwipeBackActivity is for users to swipe right to back activity like WeChat.

Usage
----
```
public class HomeActivity extends SwipeBackActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      // set shadow before super.onCreate, or not set for default.
      setShadowRes(R.drawable.shadow);
      setShadowWidth(10);
      
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_view_pager);
      setTitle(getClass().getSimpleName());
      // set translateX
      setTranslationX(300);
      // set the left sensing area
      setSwipeSize(60);
  }
}
```
Screenshot
-----
![image](https://github.com/ben-ying/SwipeBackActivity/blob/master/screenshot/swipe-back-activity.gif)

