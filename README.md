# Project
Android万能Drawable，多种状态模式、形状、填充模式、描边模式、自定义剪切等等，旨在替代手写独立的drawable.xml。<br/>
Android universal drawable, supports multiple state mode/shape/fill mode/stroke mode/custom clipping, etc. Aiming to replace separate drawable.xml written manually.

# Preview
黑色字体是默认值。<br/>
The black font means the value is default.
![image](https://github.com/yintaibing/UniversalDrawable/blob/master/screenshot/preview.jpg)

# Import
- Gradle
```groovy
compile 'me.yintaibing:universaldrawable:1.0.1'
```
- Maven
```xml
<dependency>
  <groupId>me.yintaibing</groupId>
  <artifactId>universaldrawable</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

# Usage
提供xml方式和Java方式（下方attrs.xml中显示了所有可配置参数）。<br/>
控件库提供多种控件类，你也可以仿照UniversalDrawableXXX的代码扩展你自己的控件。一行代码搞定。<br/>
XML and Java are both supported(you can see all configurable attributes in attrs.xml below).<br/>
The library, UniversalDrawableXXX, provides kinds of widgets: View/TextView/Button/CheckBox/ImageView/ViewGroup/FrameLayout/RelativeLayout/LinearLayout, and you can extends your own widget class by copying the code of UniversalDrawableXXX and you just need write only one line of code.
<br/>
<br/>
tip1: 使用多状态模式时，请注意同时加上android:clickable=true/android:checkable=true。<br/>
If you're using clickable/checkable UniversalDrawableSet, plz set android:clickable=true/android:checkable=true.<br/>
tip2:
The "fillMode" supports flags combination: color|linearGradient, color|bitmap. In this case, the color will work as a ColorFilter, so giving an alpha value to the color is recommended.<br/>
tip3:
UniversalDrawableImageView with "wrap_content" or "adjustBounds=true" may not work good...plz avoid to use it so.<br/>
```xml
<me.yintaibing.universaldrawable.view.UniversalDrawableView
    android:id="@+id/universal_view"
    android:layout_width="200dp"
    android:layout_height="200dp"
    android:clickable="true"
    app:bg_bitmap="@android:drawable/ic_delete"
    app:bg_colorNormal="#880000ff"
    app:bg_colorPressed="#880066ff"
    app:bg_colorStroke="#ff0000"
    app:bg_fillMode="color|bitmap"
    app:bg_radius="10dp"
    app:bg_scaleType="fitXY"
    app:bg_shape="round"
    app:bg_stateMode="clickable"
    app:bg_strokeWidth="2dp" />
```
```java
// stateless
UniversalDrawableFactory.createStateless();
// stateful
UniversalDrawableSet set = UniversalDrawableFactory.createClickable();//createCheckable()
set.shape(UniversalDrawable.SHAPE_CIRCLE);// common attrs
set.theNormal().colorFill(Color.BLACK);// different attrs
set.thePressed().colorFill(Color.GRAY);
//set.theChecked()...
//set.theUnchecked()...

// clip, you can call Clipper.invalidate() and Drawable.invalidateSelf() to redraw
UniversalDrawableFactory.createStateless()
	.clip(new Clipper() {
	    // false: stroke not follow clipping, true: follow
	    @Override
	    public boolean buildClipPath(Path clipPath, RectF bounds, Attributes attrs) {
	        UniversalDrawable.makeDrawPath(clipPath, bounds, attrs);
	        float radius = bounds.height() * 0.5f;
	        clipPath.addCircle(bounds.left, bounds.top, radius, Path.Direction.CW);
	        clipPath.setFillType(Path.FillType.EVEN_ODD);
	        return false;
	    }
	})
	.asBackground(view);
```

# attrs.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- UniversalDrawable属性定义，大部分场景下是用于XML布局文件，为了方便区分，采用bg_前缀 -->
    <declare-styleable name="UniversalDrawable">
        <!-- 状态模式-->
        <attr name="bg_stateMode">
            <enum name="stateless" value="0"/><!-- 无状态（默认） -->
            <enum name="clickable" value="1"/><!-- 点击模式，类似于Button -->
            <enum name="checkable" value="2"/><!-- 选择模式，类似于CheckBox -->
        </attr>

        <!-- 形状 -->
        <attr name="bg_shape">
            <enum name="rect" value="0"/><!-- 矩形（默认） -->
            <enum name="round" value="1"/><!-- 圆角矩形 -->
            <enum name="semicircle" value="2"/><!-- 水平方向两端是半圆 -->
            <enum name="circle" value="3"/><!-- 圆形 -->
        </attr>

        <!-- 填充模式，bitmap模式支持与另外2种模式同时使用 -->
        <attr name="bg_fillMode">
            <flag name="color" value="1"/><!-- 纯色填充 -->
            <flag name="linearGradient" value="2"/><!-- 线性渐变填充 -->
            <flag name="bitmap" value="4"/><!-- 图片填充 -->
        </attr>

        <!-- 填充图的缩放类型 -->
        <attr name="bg_scaleType">
            <enum name="center" value="0"/>
            <enum name="centerCrop" value="1"/><!-- 对齐控件width/height中的大者 -->
            <enum name="fitCenter" value="2"/><!-- 对齐控件width/height中的小者 -->
            <enum name="fitXY" value="3"/><!-- 对齐控件长宽 -->
        </attr>

        <!-- 描边模式 -->
        <attr name="bg_strokeMode">
            <enum name="none" value="0"/>
            <enum name="solid" value="1"/><!-- 实线描边 -->
            <enum name="dash" value="2"/><!-- 断续线描边 -->
        </attr>
        <attr name="bg_radius" format="dimension"/><!-- 圆角半径/形状为圆时的半径 -->
        <attr name="bg_radiusLeftTop" format="dimension"/>
        <attr name="bg_radiusLeftBottom" format="dimension"/>
        <attr name="bg_radiusRightTop" format="dimension"/>
        <attr name="bg_radiusRightBottom" format="dimension"/>
        <attr name="bg_strokeWidth" format="dimension"/><!-- 描边宽度 -->
        <attr name="bg_strokeDashSolid" format="dimension"/><!-- 断续线描边时，每一段实线的长度 -->
        <attr name="bg_strokeDashSpace" format="dimension"/><!-- 断续线描边时，每一段空白的长度 -->
        <attr name="bg_colorStroke" format="color"/><!-- 描边颜色 -->

        <attr name="bg_colorNormal" format="color"/><!-- 各种状态的颜色 -->
        <attr name="bg_colorDisabled" format="color"/>
        <attr name="bg_colorPressed" format="color"/>
        <attr name="bg_colorUnchecked" format="color"/>
        <attr name="bg_colorChecked" format="color"/>

        <attr name="bg_colorGradientStart" format="color"/><!-- 渐变填充的颜色和方向 -->
        <attr name="bg_colorGradientEnd" format="color"/>
        <attr name="bg_linearGradientOrientation">
            <enum name="left_right" value="0"/>
            <enum name="top_bottom" value="1"/>
            <enum name="right_left" value="2"/>
            <enum name="bottom_top" value="3"/>
        </attr>

        <attr name="bg_bitmap" format="reference"/><!-- 填充图资源id -->

        <attr name="bg_alphaFill" format="fraction"/><!-- 填充画笔的透明度 -->
        <attr name="bg_alphaStroke" format="fraction"/><!-- 描边画笔的透明度 -->

        <attr name="bg_asImageDrawable" format="boolean"/><!-- 是否作为ImageDrawable设给ImageView -->
        <attr name="bg_asForeground" format="boolean"/><!-- 是否作为Foreground设给View -->
    </declare-styleable>
</resources>
```