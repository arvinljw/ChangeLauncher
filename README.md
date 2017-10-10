## 简介

最近遇到了这么个需求，动态改变桌面图标，这个功能本身还是比较常见，比如天猫等app在各种剁手节的时候基本都会换个应景的图标以及整体风格，而这里只讨论图标的更换，查阅了一些资料，这里就介绍一下我认为最便捷的方式。

## 原理

原理其实很简单就是利用<activity-alias>标签，控制其enabled属性，用来显示与否。

## 实现

实现之前，先简单描述一下需求：

*App默认图标是Android机器人图标，然后还有另外两个图标icon_1和icon_2，通过按钮点击设置变成哪个图标，也有一个还原图标的功能。*

[例子－效果.apk](https://github.com/arvinljw/ChangeLauncher/blob/master/app/app-release.apk
)

**实现步骤：**

* 增加对应个数的<activity-alias>标签
* 增加布局喝对应点击事件
* 代码控制显示哪个图标

接下来就一步一步的来实现：

**1、增加对应个数的<activity-alias>标签**

这里还有两个图标，所以我们再增加两个<activity-alias>标签，这个标签是在AndroidManifest.xml的<application>标签内的，和<activity>标签同一级，其中一个代码如下：

```
<activity-alias
    android:name=".MainActivity1"
    android:enabled="false"
    android:icon="@mipmap/icon_1"
    android:label="@string/app_name"
    android:targetActivity=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
    </intent-filter>
</activity-alias>
```

这里需要注意一下他的这几个属性：

| 属性        | 含义           |
| ------------- |:-------------|
| name      | 可任意取值，只要能保证是唯一标识即可，为了方便管理建议有规律一些  | 
| targetActivity      | 这个属性的值就是代表指向的是哪个Activity，而这个标签本身代表是该Activity的别名，记得指向的Activity要在该标签之前申明，否则可能运行不起来      |  
| icon | 指的是该别名对应的应用图标      |  
| label         | 指的是该别名对应的应用名字         |  
| enabled         | 默认是true，true就会显示在桌面上，这里为了保证桌面只显示一个图标，则<activity-alias>中的属性都是false，而在之后代码中动态控制这个属性，来显示和隐藏对应的图标         | 

至于<intent-filter>，这个和Activity的没有区别,其实完全可以把<activity-alias>当作Activity组件来看，只是不是真身，是别名罢了。

另一个也是同理的，这里就不介绍了。

**2、增加布局和对应点击事件**

这一点其实没啥好说的，布局就是一个竖直方向的LinearLayout，里边有三个Button，通过其onClick属性，设置点击相应的方法，当然你也可以获取到这些Button再设置OnClickListener，代码比较简单，文末有Demo地址。

**3、代码控制显示哪个图标**

这一步其实也就是调用PackageManager中的一个方法即可，方法如下：

```
private void changeLauncher(String name) {
    PackageManager pm = getPackageManager();
    //隐藏之前显示的桌面组件
    pm.setComponentEnabledSetting(getComponentName(),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    //显示新的桌面组件
    pm.setComponentEnabledSetting(new ComponentName(MainActivity.this, name),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
}
```

都是调用PackageManager的setComponentEnabledSetting方法，第一个参数表示操作的组件是哪个，第二个参数表示显示还是隐藏，第三个组件表示是否关掉app。

## TIPS

1、这里有个技巧，建议不要直接点了切换按钮就去执行切换图标，因为执行切换图标之后始终会关闭这次打开的app，所以我们 **可以点击之后先记录下要换成哪个图标，在程序退出的时候再切换图标**，这样一来就不会关闭该app了。

2、细心的朋友会发现，在调试阶段，我改了这个app的启动图标，再执行代码启动，发现启动不了，其实这是因为代码中默认启动那个组件和修改后的那个组件不一致了，所以就启动不了，而**对于程序的更新和安装是没有影响的。**

3、这种方式动态更换图标，更换的时候不是，切换之后就马上生效的，据我观察应该和手机性能有关，在执行该操作之后，会在几秒钟之内改变图标，但是对于普通桌面图标的改变，该缺点还是可以接受的，毕竟不是用户手动触发，也不影响体验。

## DEMO

[例子－传送门](https://github.com/arvinljw/ChangeLauncher)

## 感谢

[Android之动态更换桌面图标](http://blog.csdn.net/qq_16628781/article/details/69054325)

