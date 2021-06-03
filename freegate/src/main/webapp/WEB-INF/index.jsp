<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<html>
<head>
    <title>上传测试</title>
</head>
<body>
<div align="center">
 请上传一个文件<br>
<form id="form1" method="post" action="upload" enctype="multipart/form-data">
    <li>
    <input id="file" type="file" name="file"/>
    </li>
    <br>
    <li>
    <input id="upload" type="submit" value="upload" onclick="return submit1();"/> &nbsp;&nbsp; <a href="download">download</a>
    </li>
</form>
<script>
function submit1(){
	var fileObject = document.getElementById("file");
    if(fileObject.value == null||fileObject.value == "") {
        alert("file is null ,please select a local file to upload");
        return false;
    }
    document.getElementById("upload").disabled = true;
    document.getElementById("form1").submit();
    return true;
}
</script>
</div>
</body>
</html>
