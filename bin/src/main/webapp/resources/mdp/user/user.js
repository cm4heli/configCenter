$(function()
{
	var userPage = $("#userPage");
	if (userPage.length == 1)
	{
		userPage.page(
		{
			total : 1000,
			firstBtnText : '首页',
			lastBtnText : '尾页',
			prevBtnText : '上一页',
			nextBtnText : '下一页'
		}).on("pageClicked", function(event, pageNumber)
		{
			alert(pageNumber);
		});
	}
});