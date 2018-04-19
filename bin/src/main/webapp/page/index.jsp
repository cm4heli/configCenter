<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Example</title>
    <link rel="stylesheet" href="../resources/bootstrap/css/bootstrap.min.css">
    <script src="../resources/jquery/jquery.js"></script>
    <script src="../resources/bootstrap/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="../resources/autosuggest.js-master/autosuggest.css">
    <script src="../resources/autosuggest.js-master/autosuggest.js"></script>

</head>
<body style="padding-top: 30px;">
<div class="form-group col-md-4 col-xs-10 col-md-offset-1 col-xs-offset-1">
    <input type="text" class="form-control" id="t1" placeholder="æç­æç¤ºé¿åº¦ä¸º1, æå¤èæ³3ä¸ª,è¯·è¾å¥ä¸­å½å°å,ä¾å¦'åäº¬'"/>
</div>

<div class="form-group col-md-4 col-xs-10 col-md-offset-1 col-xs-offset-1">
    <input type="text" class="form-control" id="t2" placeholder="é»è®¤æä½"/>
</div>

<script>
    $(document).ready(function () {
        $("#t1").autosuggest({
            url: 'city.jsp',
            minLength: 1,
            maxNum: 10,
            align: 'center',
            method: 'post',
            highlight: true,
            immediate: true,
            queryParamName: 'city',
            nextStep: function () {
                alert("hello world!");
            },
            split: ' ',
            open: function(){
                console.log("start open");
            },
            close: function(){
                console.log("start close");
            }
        });

        $("#t2").autosuggest({
            url: 'city.jsp',
            method: 'post',
            queryParamName: 'city',
            firstSelected: true
        });
    });
</script>


</body>
</html>



