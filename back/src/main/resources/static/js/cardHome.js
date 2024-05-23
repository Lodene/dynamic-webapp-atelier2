


var userConnect = ""

$(document ).ready(function(){
    
    // $.ajax("/api/user",{success:function(user){
    // 	userConnect = user.name;
    //     $("#userNameId").html(user.name);
    //     $("#money span").html(user.money);

    // },error:function(a){
    //     console.log("error",a);
    //     $("#userNameId").html(`<a href="formSample.html">Login</a>`);
    //     $("#money").hide();
    // }})



    $("#playButtonId").click(function(){
        alert("Play button clicked ");
        
    });    
    $("#buyButtonId").click(function(){
    		window.location.href="marketPlace.html";    		

    });    
    $("#sellButtonId").click(function(){
	    	window.location.href="cardList.html";
    });    
});

