<html>
    <head>
        <meta name="layout" content="container"/>
        <title>Authenticator Check</title>
    </head>
    <body>
        <h1>Authenticator Check</h1>
        
        <p>Enter the code that is displayed on your authenticator application: </p>
        <form id="registerForm" action="<g:createLink action="authenticate" controller="authenticator"/>" method="post">


            <div class="input-group col-lg-6">
                <input name="code" class="form-control"/>
                <span class="btn btn-info input-group-addon flat" type="submit" onclick="require(['jquery'],function($){$('#registerForm').submit()})">Submit</span>
            </div>
        </form>
    </body>
</html>