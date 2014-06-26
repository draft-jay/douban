douban
======

搜索豆瓣电影信息，无需登录，有历史记录功能
IDE：ADT——android2.2~android4.3
进度：
  完成第一阶段：从豆瓣网搜索电影信息，并以列表形式显示在主界面。
  完成第二阶段：用动态布局把影人信息显示在电影详情页面，点击影人照片可跳转到影人详情页面。
  四个阶段完成，全部功能实现。


主要功能：
  在搜索框输入关键字，点击GO按钮，即可从豆瓣网获得电影信息。。
  可进行关键字模糊搜索（豆瓣网实现），每次显示10条结果。
  点击图片条目跳转到详细页面。
  历史记录功能:浏览过的影人/影片，点击条目跳转详细页面。

BUG：

  1.可以拿到豆瓣网的数据，也通过baseAdapter以arrrayList<>方式填充了数据，就是显示不到listview上。没有报错。尝试在handler的最后执行notifyDataSetChanged()，没有效果。
    已解决:在重写了baseAdapter.getCount()之后就突然可以使用了。
  2.ArrayList<>中的数据都是最后一条添加的
    已解决：arrayList<Movie>填充数据时候，每次都重新new一个Movie。
  3.连续搜索的结果都保存在arrayList<Movie>里，导致上次的搜索结果不能被覆盖掉。
     已解决：定义出类的引用变量时不new出arrayList<>,填充数据时再new出，以达到覆盖效果。
  
可改进：
  历史记录目前是在线加载，而加载速度没有优化，换成本地加载体验会好很多。
  
  
主要技术：
  Json解析、Http请求、异步加载、多线程下载图片、豆瓣API

小结：

  没有使用第三方sdk、jar包，因为无需那么多功能，并且想体验手动开发过程。
  在相关软件的熟悉和json解析花了不少时间，原因主要应该是长时间编程之后的疲劳导致注意力下降。
  主要技术问题都已解决，预计一天至两天即可完成剩余功能。
  第二阶段完成的很顺利，可能因为休息得比较好，应用的整体框架已经有了。
  
  
  四天的连续开发，对Android常用组件、逻辑更加熟悉，比如有关列表显示的自定义适配器，对开放API的使用，还有不同布局的动态加载等等。顺便找资料的能力也有点提升：百度搜到的大部分是相互转载，并且都是很初级问题，stackoverflow就容易找到想要的结果
  


  
  
