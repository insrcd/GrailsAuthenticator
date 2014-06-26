<!--
  To change this license header, choose License Headers in Project Properties.
  To change this template file, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Testing</title>
    </head>
    <body>
        <h1>Authenticator Test</h1>
        First Register with Google Authenticator by using this QR Code:
        <br/>
        <img src='${url}'/>
        <br/>
        <p>Now enter in a sample code</p>
        <form action="<g:createLink action="register" controller="authenticator"/>" method="post">
            <input name="code"/><button type="submit">Submit</button>
        </form>
    </body>
</html>
