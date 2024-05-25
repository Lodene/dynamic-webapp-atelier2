$(document).ready(function () {
  $("#registerSubmit").click(function (event) {
    event.preventDefault();

    var username = $("#username").val();
    var password = $("#password").val();
    var name = $("#name").val();

    $.ajax({
      url: "/user/new",
      method: "PUT",
      contentType: "application/json",
      data: JSON.stringify({
        username: username,
        password: password,
        name: name,
      }),
      success: function (result) {
        console.log(result);
        alert("vous avez bien été enregistré");
        window.location.href = "/formSample.html";
      },
      error: function (error) {
        console.error("There was a problem with the registration:", error);
      },
    });
  });
});
