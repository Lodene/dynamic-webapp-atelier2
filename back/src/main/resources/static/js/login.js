var userConnect = "";

$(document).ready(function() {
    $("#loginSubmit").click(function(event) {
        event.preventDefault();
        var username = $("#username").val();  
        var password = $("#password").val();  
        
        $.ajax({
            url: "/user/login",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ username: username, password: password }),
            success: function(result) {
                console.log(result);
                // Stockage du JWT dans le sessionStorage
                sessionStorage.setItem("jwt", result.jwt);
                alert('Vous êtes bien connecté');
                // Redirection vers une nouvelle page après la connexion
                window.location.replace("./cardList.html");
            },
            error: function(error) {
                console.log(error);
                alert('Identifiants incorrects');
            }
        });
    });
});
