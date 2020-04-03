# varsql

	varsql websqltool sqltool 

# 개발환경
- java : 1.8
- eclipse : Version: 2020-03 (4.15.0)
- gradle : 6.0.1

# 사용방법 
프로그램 주석 지우기(java, jsp, javascript, css, html, xml, property)
--java
CommentRemoveConverter cru = new CommentRemoveConverter();		
String result = cru.convert(cont, CommentRemoveConverter.CommentType.JAVA);

--javascript
CommentRemoveConverter cru = new CommentRemoveConverter();		
String result = cru.convert(cont, CommentRemoveConverter.CommentType.JAVASCRIPT);


--SQL
SQLCommentRemoveConverter cru = new SQLCommentRemoveConverter();
String result = cru.convert(cont, DBType.MSSQL);

