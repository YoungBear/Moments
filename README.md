#说明

##概述
程序使用MVC结构，实现类微信朋友圈的app，只是一个静态页面，没有实现点击等交互。可以使用上拉和下拉刷新操作，下拉刷新，会重新请求数据，并且只显示前5条动态信息，执行上拉刷新的时候，再多显示5条数据，直到全部显示。

##使用到的第三方框架
- Volley请求网络数据，并简单封装,VolleyController
- 使用Glide加载网络图片
- 使用gson格式化json格式数据
- 使用SuperRecyclerView实现上拉和下拉刷新的组件
- 自定义View CommentListView实现多个评论布局
- 自定义View MultiImageView实现仿微信朋友圈的多图片布局

##构建说明
下载.zip包文件，解压缩后，使用Android Studio,选择import工程，然后即可运行。